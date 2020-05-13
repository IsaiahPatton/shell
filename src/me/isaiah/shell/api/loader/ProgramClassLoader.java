package me.isaiah.shell.api.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JInternalFrame;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.ClassRemapper;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.Toast;

final class ProgramClassLoader extends URLClassLoader {

    private final ProgramLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<>();
    public JProgram plugin;
    public Class<?> jarClass;

    private static HashMap<String, String> wrappers;

    @SuppressWarnings("unchecked")
    ProgramClassLoader(final ProgramLoader loader, final ClassLoader parent, final String name, final URI file) throws Exception {
        super(new URL[] {file.toURL()}, parent);
        addWrappers();

        String cp = System.getProperty("java.class.path");
        System.setProperty("java.class.path", cp + File.pathSeparator + new File(file).getAbsolutePath());

        this.loader = loader;
        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(name, true, this);
            } catch (ClassNotFoundException e) { throw new Exception("Cannot find main class '" + name + "'", e); }

            Class<? extends JProgram> pluginClass = null;
            try {
                pluginClass = jarClass.asSubclass(JProgram.class);
            } catch (ClassCastException e) {
                try {
                    pluginClass = (Class<? extends JProgram>) jarClass.asSubclass(JInternalFrame.class); // Try super class
                } catch (ClassCastException e1) {
                    try {
                        JFrameWrapper f = (JFrameWrapper) jarClass.newInstance();
                        plugin = f.jp;
                        return;
                    } catch (ClassCastException e2) {
                        e2.printStackTrace();
                        String[] s = {};
                        jarClass.getMethod("main", String[].class)
                            .invoke(null, (Object)s);
                        Toast.show("java.lang.ClassCastException:\n\n " + jarClass.getName() + " cannot be cast to:\n"
                                + "\tJProgram, JInternalFrame, or JFrame", 3000, 500, 79, null);
                    }
                }
            }

            plugin = pluginClass.newInstance();
        } catch (IOException e) { e.printStackTrace(); }
    }

    ProgramClassLoader(final ProgramLoader loader, final ClassLoader parent, final String name, final URI file, boolean a) throws Exception {
        super(new URL[] {file.toURL()}, parent);
        addWrappers();

        this.loader = loader;
        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(name, true, this);
            } catch (ClassNotFoundException e) { throw new Exception("Cannot find main class '" + name + "'", e); }

            this.jarClass = jarClass;
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void addWrappers() {
        if (wrappers != null)
            return;
        wrappers = new HashMap<>();

        wrappers.put("javax.swing.JFrame", "me.isaiah.shell.api.loader.JFrameWrapper");
        wrappers.put("javax.swing.UIManager", "me.isaiah.shell.api.loader.UIManagerWrapper");
        wrappers.put("javax.swing.JWindow", "me.isaiah.shell.api.loader.JWindowWrapper");
    }

    public byte[] getByteCode(String caller) throws IOException{
        String resource = caller.replace('.', '/')+".class";
        InputStream is = getResourceAsStream(resource);
        ClassReader cr = new ClassReader(is);

        ClassVisitor visitor = new BCMerge(Opcodes.ASM5);
        cr.accept(visitor, 0);
        return cr.b;
    }

    public class BCMerge extends ClassVisitor{
        public BCMerge(int api) {
            super(api);
        }

        public MethodVisitor visitMethod(int access, String name, String desc,String signature, String[] exceptions) {
            if (cv != null)
                return new MyMethodVisit(cv.visitMethod(access, name, desc, signature, exceptions));
            return null;
        }
    }

    public class MyMethodVisit extends MethodVisitor{
        public MyMethodVisit(MethodVisitor methodVisitor) {
            super(Opcodes.ASM5, methodVisitor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (classes.containsKey(name))
            return classes.get(name);
        Class<?> clazz = loadClass0(name);
        classes.put(name, clazz);
        return clazz;
    }

    public Class<?> loadClass0(String name) throws ClassNotFoundException {
        if (name.startsWith("java") || wrappers.containsValue(name) || name.startsWith("sun.") || name.contains("resource"))
            return super.loadClass(name);

        byte[] bytecode = null;
        try {
            bytecode = getByteCode(name);
        } catch (IOException e1) {
            System.err.println(e1.getMessage() + " / " + name);
            return super.loadClass(name);
        }
        byte[] remappedBytecode;

        try {
            remappedBytecode = rewriteDefaultPackageClassNames(bytecode);
        } catch (IOException e) {
            throw new RuntimeException("Could not rewrite class " + name);
        }

        return defineClass(name, remappedBytecode, 0, remappedBytecode.length);
    }

    public byte[] rewriteDefaultPackageClassNames(byte[] bytecode) throws IOException {
        ClassReader classReader = new ClassReader(bytecode);
        ClassWriter classWriter = new ClassWriter(classReader, 0);

        Remapper remapper = new DefaultPackageClassNameRemapper();
        classReader.accept(new ClassRemapper(classWriter, remapper), 0);

        return classWriter.toByteArray();
    }

    class DefaultPackageClassNameRemapper extends Remapper {
        @Override
        public String map(String typeName) {
            return (wrappers.containsKey(typeName.replace('/','.')) ? wrappers.get(typeName.replace('/','.')).replace('.','/') : typeName);
        }
    }

    @Override protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        Class<?> result = classes.get(name);

        if (result == null) {
            if (checkGlobal) result = loader.getClassByName(name);

            if (result == null) {
                result = super.findClass(name);

                if (result != null) loader.setClass(name, result);
            }

            classes.put(name, result);
        }
        return result;
    }


    Set<String> getClasses() {
        return classes.keySet();
    }
}