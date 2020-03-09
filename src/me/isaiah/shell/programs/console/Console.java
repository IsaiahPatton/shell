package me.isaiah.shell.programs.console;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import me.isaiah.shell.Main;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;
import me.isaiah.shell.api.Toast;

@ProgramInfo(name = "Command Prompt")
public class Console extends JProgram {

    private static final long serialVersionUID = 1L;
    protected JTextPane area;
    private File currentPath;

    private List<String> commandHistory;
    private int history;

    public Console() {
        this(true);
    }

    public Console(boolean reset) {
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

        setContentPane(new JScrollPane(area));
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
                add(System.getProperty("user.dir"));
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
            case "run":
                system(command.substring(3).trim().split(" "), false);
                break;
            case "threads":
                add("Thread Name | State | ID", Color.CYAN);
                for (Thread t : Thread.getAllStackTraces().keySet())
                    add(t.getName() + " | " + t.getState() + " | " + t.getId());
                break;
            case "note":
                Toast.show(command, 4500);
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