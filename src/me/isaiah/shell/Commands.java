package me.isaiah.shell;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import me.isaiah.shell.api.JProgram;

public class Commands {

    private JTextPane area;
    private Container window;
    public Commands(JTextPane p, Container host) {
        this.area = p;
        this.window = host;
    }

    public final void onCommand(JTextField f, ActionEvent l) {
        String command = f.getText().toLowerCase();
        String[] args = command.split(" ");
        if (command.endsWith(".com") || command.endsWith(".exe")) {
            system(args, true);
            f.setText("");
            return;
        }
        switch (args[0]) {
            case "version":
            case "ver":
                add("Version: " + Main.VERSION);
                break;
            case "java":
                system(args, true);
                break;
            case "dir":
                add(System.getProperty("user.dir"));
                break;
            case "echo":
                add(command.substring(4).trim());
                break;
            case "system":
                for (Object s : System.getProperties().keySet()) {
                    if (args.length == 1 || ((String)s).startsWith(args[1])) {
                        add(s + "", Color.CYAN);
                        c(" --> " + System.getProperty((String) s));
                    }
                }
                break;
            case "cls":
            case "clear":
                area.setText("ZDE Console [Version " + Main.VERSION + "]\n(C) 2018 Contributors");
                break;
            case "title":
                if (window instanceof JFrame) { // Standalone Console
                    JFrame p = (JFrame) window;
                    if (args.length == 1) add("Window title: " + p.getTitle());
                    else p.setTitle(command.substring("title ".length()));
                } else {
                    JProgram p = (JProgram) window;
                    if (args.length == 1) add("Window title: " + p.getTitle());
                    else p.setTitle(command.substring("title ".length()));
                }
                break;
            case "run":
                System.out.println(command.substring(3).trim());
                system(command.substring(3).trim().split(" "), false);
                break;
            case "threads":
                add("Thread Name | State | ID", Color.CYAN);
                for (Thread t : Thread.getAllStackTraces().keySet())
                    add(t.getName() + " | " + t.getState() + " | " + t.getId());
                break;
            case "updateinfo":
                new UpdateCheck();
                break;
            case "darkmode":
                add("Dark mode: " + (Main.dark = !Main.dark));
                Color t = new Color(31, 70, 250);
                Main.taskbar.setBackground(Main.dark ? t.darker() : t);
                break;
            case "help":
                add("===== Help =====", Color.CYAN);
                add("HELP       Display this message");
                add("VERSION    Prints system version");
                add("JAVA       Java Runtime command");
                add("DIR        Prints current dir");
                add("ECHO       Prints text");
                add("SYSTEM     Prints system propertites");
                add("CLS        Clears the screen");
                add("TITlE      Changes the title");
                add("RUN        Run a outside program");
                add("THREADS    Show simple info about all Threads");
                add("UPDATEINFO Runs the Update checker");
                add("DARKMODE   Enables/Disables experimental Dark Theme");
                break;
            default:
                add("Unknown command: " + args[0], Color.red);
                break;
        }
    }

    private void add(String content) {
        add(content, Color.LIGHT_GRAY);
    }

    private void c(String content) {
        c(content, Color.LIGHT_GRAY);
    }

    private void add(String content, Color c) {
        StyledDocument d = area.getStyledDocument();
        Style style = d.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setForeground(style, c);
        try {
            d.insertString(d.getLength(), "\n" + content, style);
        } catch (BadLocationException e) { e.printStackTrace(); }
    }
    
    private StyledDocument c(String content, Color c) {
        StyledDocument d = area.getStyledDocument();
        Style style = d.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setForeground(style, c);
        try {
            d.insertString(d.getLength(), content, style);
        } catch (BadLocationException e) { e.printStackTrace(); }
        return d;
    }

    private void system(String[] args, boolean block) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args).redirectErrorStream(true);
            Process process = processBuilder.start();
            try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
                String readLine;
                while ((readLine = processOutputReader.readLine()) != null) add(readLine);

                try {
                    if (block) process.waitFor();
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
            process.destroy();
        } catch (IOException e) { e.printStackTrace(); }
    }

}