package me.isaiah.shell.programs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import me.isaiah.shell.Main;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramDownloader;
import me.isaiah.shell.api.ProgramDownloader.DownloadAction;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name = "Minecraft Launcher", version="1.0", authors="Mojang, Xbox Game Studios", width=900, height=700)
public class Minecraft extends JProgram {

    private static final long serialVersionUID = 1L;

    public Minecraft() {
        File f = new File(new File(Main.pStorage.getParentFile(), "downloadedPrograms"), "minecraft.jar");
        f.getParentFile().mkdir();
        DownloadAction a = () -> {
            if (f.exists()) {
                Class<?> c = Main.pm.loadFromNonJProgramURI("net.minecraft.bootstrap.Bootstrap", f.toURI());
                try {
                    String[] args = {"test"};
                    c.getMethod("main", String[].class).invoke(null, (Object)args);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                setTitle("Minecraft Launcher - DEV TEST");
                setVisible(true);
            }
        };

        if (f.exists()) {
            a.onFinished();
        } else {
            try {
                new ProgramDownloader("https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar", f.getParentFile(), a);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}