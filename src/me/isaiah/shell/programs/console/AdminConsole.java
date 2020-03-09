package me.isaiah.shell.programs.console;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import me.isaiah.shell.Main;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name = "Command Prompt - Admin")
public class AdminConsole extends Console {

    private static final long serialVersionUID = 1L;

    public AdminConsole() {
        super(false);
    }

    public static void init() {
        PrintStream m = System.out;
        area = new JTextPane();
        StyledDocument d = area.getStyledDocument();

        System.setOut(new PrintStream(new OutputStream() {
            @Override public void write(int b) throws IOException {
                m.write(b);
                Style style = d.getStyle(StyleContext.DEFAULT_STYLE);
                StyleConstants.setForeground(style, Color.LIGHT_GRAY);
                try {
                    d.insertString(d.getLength(), String.valueOf((char) b), style);
                } catch (BadLocationException e) { e.printStackTrace(); }
            }}));

        PrintStream err = System.err;
        System.setErr(new PrintStream(new OutputStream() {
            @Override public void write(int b) throws IOException {
                err.write(b);
                Style style = d.getStyle(StyleContext.DEFAULT_STYLE);
                StyleConstants.setForeground(style, Color.RED);
                try {
                    d.insertString(d.getLength(), String.valueOf((char) b), style);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }}));

        System.out.println(Main.NAME + " [Version " + Main.VERSION + "]");
        System.out.println("(C) 2018-2020 Fungus Software & contributors");
    }

}