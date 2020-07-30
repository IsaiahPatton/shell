package com.fungus_soft.desktop.api.loader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.StartMenu;
import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;
import com.fungus_soft.desktop.api.Toast;

public class JProgramManager {

    public ProgramClassLoader classLoader;

    public void loadProgram(File f) {
        loadProgram(f, false, true);
    }

    public void loadProgram(File f, boolean b, boolean menu) {
        new Thread(() -> {
        if (!f.isDirectory() && f.getName().endsWith(".jar")) {
            try (JarFile jar = new JarFile(f)) {
                Manifest m = jar.getManifest();
                String main = m.getMainAttributes().getValue(Name.MAIN_CLASS);
                classLoader = new ProgramClassLoader(new ProgramLoader(), getClass().getClassLoader(), main, f.toURI());
                if (classLoader.plugin == null)
                    return;

                JProgram program = classLoader.plugin;
                ProgramInfo i = program.getClass().getAnnotation(ProgramInfo.class);
                ProgramInfo info = i == null ? info = JProgram.class.getAnnotation(ProgramInfo.class) : i;
                String name = info.name();

                if (!Main.pr.contains(f.getAbsolutePath())) {
                    Main.pStorage.getParentFile().mkdirs();
                    Main.pStorage.createNewFile();
                    Main.pr.add(f.getAbsolutePath());
                    Toast.show("Registered \"" + name + "\" to Programs menu", 5000, 300, 60, null);
                }

                if (menu)
                    StartMenu.addProgram(program.getClass());

                if (b) {
                    program.setVisible(true);
                    program.setSize(info.width(), info.height());
                    Main.p.add(program);
                }
            } catch (Exception e) {
                 Toast.show("ProgramManager: Unable to start\n '" + f.getName() + "':" + e.getLocalizedMessage(), 2500);
            }
        }
        }).start();
    }

    public void loadProgramNon(File f, boolean b, boolean menu) {
        new Thread(() -> {
        if (!f.isDirectory() && f.getName().endsWith(".jar")) {
            try (JarFile jar = new JarFile(f)) {
                Manifest m = jar.getManifest();
                String main = m.getMainAttributes().getValue(Name.MAIN_CLASS);
                classLoader = new ProgramClassLoader(new ProgramLoader(), getClass().getClassLoader(), main, f.toURI(), true);
                Class<?> jarClass = classLoader.jarClass;
                Method meth = jarClass.getMethod("main", String[].class);
                String[] params = null; // init params accordingly
                meth.invoke(null, (Object) params); // static method doesn't have an instance
            } catch (Exception e) {
                e.printStackTrace();
                 Toast.show("ProgramManager: Unable to start\n '" + f.getName() + "':" + e.getLocalizedMessage(), 2500);
            }
        }
        }).start();
    }

    public JProgram getFromURI(String main, URI uri) {
        try {
            classLoader = new ProgramClassLoader(new ProgramLoader(), getClass().getClassLoader(), main, uri);
            return classLoader.plugin;
        } catch (Exception e) {
            Toast.show("ProgramManager: Unable to start program:\n" + e.getLocalizedMessage(), 2500);
            return null;
        }
    }
    
    public Class<?> loadFromNonJProgramURI(String main, URI uri) {
        try {
            classLoader = new ProgramClassLoader(new ProgramLoader(), getClass().getClassLoader(), main, uri, true);
            return classLoader.jarClass;
        } catch (Exception e) {
            Toast.show("ProgramManager: Unable to start program:\n" + e.getLocalizedMessage(), 2500);
            return null;
        }
    }

}