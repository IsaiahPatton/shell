package me.isaiah.shell;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.programs.Browser;

final class ProgramClassLoader extends URLClassLoader {
    private final ProgramLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<>();
    public JProgram plugin;

    @SuppressWarnings("unchecked")
    ProgramClassLoader(final ProgramLoader loader, final ClassLoader parent, final String name, final File file) throws Exception {
        super(new URL[] {file.toURI().toURL()}, parent);

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
                    // Try upperlevel class
                    pluginClass = (Class<? extends JProgram>) jarClass.asSubclass(JInternalFrame.class);
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
                        if (jarClass.getName().equalsIgnoreCase("me.isaiah.zunozap.Launcher")) {
                            Main.showNotification("Launching ZunoZap Browser", 2000, 200, 45);
                            Browser.run();
                        } else {
                            e2.printStackTrace();
                            Main.showNotification("java.lang.ClassCastException:\n\n " + jarClass.getName() + " cannot be cast to:\n"
                                + "\tJProgram, JInternalFrame, or JFrame", 3000, 500, 79);
                        }
                    }
                }
            }

            plugin = pluginClass.newInstance();
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