package me.isaiah.shell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class StandaloneConsole extends DebugConsole {
    private static final long serialVersionUID = 1L;

    public StandaloneConsole() {
        DebugConsole.init();
        JFrame fr = new JFrame();

        area.setBackground(Color.BLACK);
        area.setForeground(Color.LIGHT_GRAY);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane p = new JScrollPane(area);
        JTextField f = new JTextField();
        f.setMaximumSize(new Dimension(100000, 100));
        Commands c = new Commands(area, fr);
        f.addActionListener(l -> { c.onCommand(f,l); f.setText(""); });
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        pan.add(p);
        pan.add(f);

        fr.setContentPane(pan);
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fr.setSize(800, 450);
        fr.validate();
        fr.setVisible(true);
    }

    public static void main(String[] args) {
        new StandaloneConsole();
    }

}