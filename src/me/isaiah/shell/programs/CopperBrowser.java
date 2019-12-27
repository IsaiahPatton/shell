package me.isaiah.shell.programs;

import java.net.URISyntaxException;

import javax.swing.UIManager;

import me.isaiah.shell.Main;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;
import me.isaiah.shell.api.Toast;

@ProgramInfo(name = "Web Browser (Copper version)", version="UNRELEASED", authors="Copper Contributers", width=900, height=700)
public class CopperBrowser extends JProgram {

    private static final long serialVersionUID = 1L;

    public CopperBrowser() {
        System.setProperty("swing.systemlaf", UIManager.getCrossPlatformLookAndFeelClassName()); // Make Copper not use the SystemLAF

        try {
           JProgram b = Main.pm.getFromURI("com.zunozap.Browser", this.getClass().getClassLoader().getResource("jars/copper-indev.jar").toURI());
           setContentPane(b.getContentPane());
           setTitle("ZunoZap Web Browser - Copper renderer development test");
           setVisible(true);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.show("Unable to launch: " + e.getMessage(), 2500);
        }
    }

}