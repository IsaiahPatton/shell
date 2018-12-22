package me.isaiah.shell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import me.isaiah.shell.api.JProgram;

public class DebugConsole extends JProgram {
    private static final long serialVersionUID = 1L;
    protected static JTextPane area = new JTextPane();
    
    public DebugConsole() {
        this(false);
    }

    public DebugConsole(boolean canClear) {
        super("Console Output");
        area.setBackground(Color.BLACK);
        area.setForeground(Color.LIGHT_GRAY);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane p = new JScrollPane(area);
        JTextField f = new JTextField();
        f.setMaximumSize(new Dimension(1000000, 100));
        Commands c = new Commands(area, this);
        f.addActionListener(l -> { if (canClear || !(f.getText().startsWith("cls") || f.getText().startsWith("clear"))) {
            c.onCommand(f,l); f.setText("");}});
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        pan.add(p);
        pan.add(f);
        setContentPane(pan);
    }

    public static void init() {
        PrintStream m = System.out;
        StyledDocument d = area.getStyledDocument();
        System.setOut(new PrintStream(new OutputStream() {
            @Override public void write(int b) throws IOException {
                m.write(b);
                StyledDocument d = area.getStyledDocument();
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
        System.out.println("ZunoZap OS [Version " + Main.VERSION + "]");
        System.out.println("(C) 2018 ZunoZap Contributors");
    }
}