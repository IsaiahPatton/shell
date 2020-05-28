package com.fungus_soft.programs;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;

@ProgramInfo(name = "Console Output")
public class ConsoleOutput extends JProgram {

    private static final long serialVersionUID = 1L;
    private static JTextPane i;

    public ConsoleOutput() {
        setContentPane(new JScrollPane(i));
    }

    public static void init() {
        i = new JTextPane();
        i.setBackground(Color.BLACK);
        i.setEditable(false);

        PrintStream m = System.out;
        StyledDocument d = i.getStyledDocument();

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
    }

}