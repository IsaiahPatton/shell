package me.isaiah.shell.programs;

import javax.swing.UIManager;

import com.zunozap.Browser;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name = "Web Browser (Copper version)", version="UNRELEASED", authors="Copper Contributers", width=900, height=700)
public class CopperBrowser extends JProgram {

    private static final long serialVersionUID = 1L;

    public CopperBrowser() {
        System.setProperty("swing.systemlaf", UIManager.getCrossPlatformLookAndFeelClassName()); // Make Copper not use the SystemLAF

        Browser b = new Browser();
        setContentPane(b.getContentPane());
        setTitle("ZunoZap Web Browser - Copper renderer development test");
        setVisible(true);
    }

}