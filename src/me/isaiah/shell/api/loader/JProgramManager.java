package me.isaiah.shell.api.loader;

import java.io.File;
import java.net.URI;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import me.isaiah.shell.Main;
import me.isaiah.shell.StartMenu;
import me.isaiah.shell.api.DemoInfo;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;
import me.isaiah.shell.api.Toast;

public class JProgramManager {

    public ProgramClassLoader classLoader;

    public void loadProgram(File f) {
        loadProgram(f, false, true);
    }

    public void loadProgram(File f, boolean b, boolean menu) {
        if (!f.isDirectory() && f.getName().endsWith(".jar")) {
            try (JarFile jar = new JarFile(f)) {
                Manifest m = jar.getManifest();
                String main = m.getMainAttributes().getValue(Name.MAIN_CLASS);
                classLoader = new ProgramClassLoader(new ProgramLoader(), getClass().getClassLoader(), main, f.toURI());

                JProgram program = classLoader.plugin;
                ProgramInfo i = program.getClass().getAnnotation(ProgramInfo.class);
                ProgramInfo info = i == null ? info = DemoInfo.class.getAnnotation(ProgramInfo.class) : i;
                String name = info.name();

                if (name.equalsIgnoreCase("JFrame")) name = f.getName().substring(0, f.getName().indexOf(".jar"));

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