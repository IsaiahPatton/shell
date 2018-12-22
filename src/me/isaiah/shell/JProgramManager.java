package me.isaiah.shell;

import java.io.File;
import java.util.ArrayList;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;
import me.isaiah.zunozap.plugin.PluginBase;

public class JProgramManager {

    public ArrayList<PluginBase> plugins = new ArrayList<>();
    public ArrayList<String> names = new ArrayList<>();
    public ProgramClassLoader classLoader;

    public void loadProgram(File f) {
        loadProgram(f, false);
    }

    public void loadProgram(File f, boolean b) {
        if (!f.isDirectory() && f.getName().endsWith(".jar")) {
            try (JarFile jar = new JarFile(f)) {
                Manifest m = jar.getManifest();
                String main = m.getMainAttributes().getValue(Name.MAIN_CLASS);
                classLoader = new ProgramClassLoader(new ProgramLoader(), getClass().getClassLoader(), main, f);

                JProgram program = classLoader.plugin;
                ProgramInfo i = program.getClass().getAnnotation(ProgramInfo.class);
                ProgramInfo info = i == null ? info = DemoInfo.class.getAnnotation(ProgramInfo.class) : i;
                String name = info.name();

                if (name.equalsIgnoreCase("JFrame")) name = f.getName().substring(0, f.getName().indexOf(".jar"));

                if (!Main.pr.contains(f.getAbsolutePath())) {
                    Main.pStorage.getParentFile().mkdirs();
                    Main.pStorage.createNewFile();
                    Main.pr.add(f.getAbsolutePath());
                    Main.showNotification("Registered \"" + name + "\" to Programs menu", 5000, 300, 60);
                }

                StartMenu.programs.add(name + " " + info.version()).addActionListener(l -> {
                    program.setVisible(true);
                    program.setSize(info.width(), info.height());
                    Main.p.add(program);
                });

                if (b) {
                    program.setVisible(true);
                    program.setSize(info.width(), info.height());
                    Main.p.add(program);
                }
            } catch (Exception e) {
                System.err.println("[ProgramManager]: Unable to start '" + f.getName() + "':" + e.getLocalizedMessage());
            }
        }
    }

}