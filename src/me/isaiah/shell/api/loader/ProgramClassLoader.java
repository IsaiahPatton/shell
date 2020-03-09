package me.isaiah.shell.api.loader;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.Toast;

final class ProgramClassLoader extends URLClassLoader {
    private final ProgramLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<>();
    public JProgram plugin;
    public Class<?> jarClass;

    @SuppressWarnings("unchecked")
    ProgramClassLoader(final ProgramLoader loader, final ClassLoader parent, final String name, final URI file) throws Exception {
        super(new URL[] {file.toURL()}, parent);

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
                        JFrame f = (JFrame) jarClass.newInstance();
                        JProgram pro = new JProgram(f.getTitle() + " ~ Port");
                        pro.setContentPane(f.getContentPane());
                        if (f.getJMenuBar() != null) pro.setJMenuBar(f.getJMenuBar());
                        f.setVisible(false);
                        f.setSize(f.getMaximumSize());
                        f.dispose();
                        plugin = pro;
                        return;
                    } catch (ClassCastException e2) {
                        e2.printStackTrace();
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

        this.loader = loader;
        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(name, true, this);
            } catch (ClassNotFoundException e) { throw new Exception("Cannot find main class '" + name + "'", e); }

            this.jarClass = jarClass;
        } catch (IOException e) { e.printStackTrace(); }
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