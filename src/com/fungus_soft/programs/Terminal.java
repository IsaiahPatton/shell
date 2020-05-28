package com.fungus_soft.programs;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.fungus_soft.ui.ModernScrollPane;

import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;
import com.fungus_soft.desktop.api.Toast;

@ProgramInfo(name = "Command Prompt", width=800, height=450)
public class Terminal extends JProgram {

    private static final long serialVersionUID = 1L;
    protected JTextPane area;
    private File currentPath;

    private List<String> commandHistory;
    private int history;

    public Terminal() {
        this(true);
    }

    public Terminal(boolean reset) {
        if (reset) {
            area = new JTextPane();
            area.setText(Main.NAME + " [Version " + Main.VERSION + "]\n(C) 2018-2020 Fungus Software & contributors\n");
        }
        this.commandHistory = new ArrayList<>();
        this.currentPath = new File(System.getProperty("user.home"));

        area.setBackground(Color.BLACK);
        area.setForeground(Color.LIGHT_GRAY);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setCaretColor(Color.LIGHT_GRAY);
        add(currentPath.getAbsolutePath() + ">");

        area.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ev) {
                boolean isEnter = ev.getKeyCode() == KeyEvent.VK_ENTER;

                String[] txt = area.getText().split("\n");
                String last = txt[txt.length - 1];
                int caretInLast = area.getCaretPosition()-(area.getText().length() - last.length());

                if (ev.getKeyCode() != KeyEvent.VK_LEFT && ev.getKeyCode() != KeyEvent.VK_RIGHT) {
                    try {
                        if (caretInLast < area.getText().length() - last.length())
                        area.setCaretPosition(area.getText().length());
                    } catch (IllegalArgumentException e) {}
                } else if (ev.getKeyCode() == KeyEvent.VK_LEFT && caretInLast == last.indexOf(">")+1)
                        ev.consume();

                boolean isDown = ev.getKeyCode() == KeyEvent.VK_DOWN;
                if (ev.getKeyCode() == KeyEvent.VK_UP || isDown) {
                    ev.consume();
                    int point = commandHistory.size() - 1 - history;
                    if (commandHistory.size() > 0 && point >= 0) {
                        setLine(commandHistory.get(point));
                        if (isDown) history--;
                        else history++;
                    }
                }

                if (isEnter) {
                    history = 0;
                    onCommand(last.substring(last.indexOf(">") + 1));
                    ev.consume();
                }

                if (!last.contains(currentPath.getAbsolutePath() + ">") || isEnter) {
                    StyledDocument d = area.getStyledDocument();
                    Style style = d.getStyle(StyleContext.DEFAULT_STYLE);
                    StyleConstants.setForeground(style, Color.LIGHT_GRAY);
                    try {
                        d.insertString(d.getLength(), (isEnter ? "\n" : "") + currentPath.getAbsolutePath() + ">", style);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        setContentPane(new ModernScrollPane(area));
    }

    public void onCommand(String command) {
        this.commandHistory.add(command);
        String[] args = command.split(" ");

        switch (args[0]) {
            case "version":
            case "ver":
                add("Version: " + Main.VERSION);
                break;
            case "java":
                system(args, true);
                break;
            case "dir":
                File[] files = currentPath.listFiles();
                for (File f : files) {
                    add(new Date(f.lastModified()) + "\t" + (f.isDirectory() ? "<DIR>" : "") + "\t" + f.getName());
                }
                break;
            case "echo":
                add(command.substring(4).trim());
                break;
            case "system":
                for (Object s : System.getProperties().keySet()) {
                    if (args.length == 1 || ((String)s).startsWith(args[1])) {
                        add(s + "", Color.CYAN);
                        add(" --> " + System.getProperty((String) s), Color.LIGHT_GRAY);
                    }
                }
                break;
            case "cls":
            case "clear":
                area.setText(Main.NAME + " [Version " + Main.VERSION + "]\n\u00A92020 Fungus Software & contributors\n");
                break;
            case "title":
                if (args.length == 1) add("Window title: " + getTitle());
                else setTitle(command.substring("title ".length()));

                break;
            case "sys":
                system(command.substring(3).trim().split(" "), false);
                break;
            case "run":
                try {
                    Class<?> str = Class.forName(command.substring(3).trim());
                    ProgramInfo i = str.getAnnotation(ProgramInfo.class);
                    Main.p.add((JProgram)str.newInstance(), i.width(), i.height());
                } catch (Exception e) {
                    add("Could not run JProgram: " + e.getMessage(), Color.RED);
                }
                break;
            case "threads":
                add("Thread Name | State | ID", Color.CYAN);
                for (Thread t : Thread.getAllStackTraces().keySet())
                    add(t.getName() + " | " + t.getState() + " | " + t.getId());
                break;
            case "note":
                Toast.show(command, 4500);
                break;
            case "cd":
                if (args.length == 1) {
                    add(currentPath.getAbsolutePath());
                    break;
                }
                File newPath = new File(currentPath, args[1]);
                if (newPath.exists()) {
                    currentPath = newPath;
                    break;
                }
                newPath = new File(args[1]);
                if (newPath.exists()) {
                    currentPath = newPath;
                    break;
                }
                break;
            case "help":
                add("===== Help =====", Color.CYAN);
                add("HELP\tDisplay this message");
                add("VERSION\tPrints system version");
                add("JAVA\tJava Runtime command");
                add("DIR\tPrints current dir");
                add("ECHO\tPrints text");
                add("SYSTEM\tPrint system propertites");
                add("CLS\tClears the screen");
                add("TITlE\tChange the terminal title");
                add("SYS\tRun a outside program");
                add("RUN\tStart an instance of JProgram by classname");
                add("THREADS\tShow simple info about all Threads");
                add("CD\tChange current directory");
                break;
            default:
                add("Unknown command: " + args[0], Color.RED);
                break;
        }
    }

    private void add(String content) {
        add(content, Color.LIGHT_GRAY);
    }

    private void add(String content, Color c) {
        append("\n" + content, c);
    }
    
    private void append(String content, Color c) {
        StyledDocument d = area.getStyledDocument();
        Style style = d.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setForeground(style, c);
        try {
            d.insertString(d.getLength(), content, style);
        } catch (BadLocationException e) { e.printStackTrace(); }
    }

    private void setLine(String content) {
        String[] txt = area.getText().split("\n");
        String last = txt[txt.length - 1];
        String lastA = last.substring(0, last.indexOf(">"));

        StyledDocument d = area.getStyledDocument();
        Style style = d.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setForeground(style, Color.LIGHT_GRAY);

        try {
            int start = d.getLength() - (last.length() - lastA.length()) + 1;
            d.remove(start, d.getLength() - start);
            d.insertString(start, content, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void system(String[] args, boolean block) {
        try {
            Process process = new ProcessBuilder(args).redirectErrorStream(true).start();
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