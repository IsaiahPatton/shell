package me.isaiah.shell.programs;

import java.awt.Color;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import me.isaiah.shell.Main;
import me.isaiah.shell.api.JProgram;

public class ProgramManager extends JProgram {

    private static final long serialVersionUID = 1L;

    public ProgramManager() {
        super("Manage Programs");
        JPanel pane = new JPanel();

        for (String s : Main.pr) {
            try {
                File program = new File(s);
                JPanel p = new JPanel();
                p.add(new JTextField(program.getName()));
                p.add(new JButton("Unregister"));
                p.add(new JButton("Delete Forever"));
                p.setBorder(new LineBorder(Color.BLACK, 3, true));
                p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
                pane.add(p);
            } catch (NullPointerException e) { System.out.println("Null: " + s); e.printStackTrace();}
        }

        JScrollPane sc = new JScrollPane(pane);
        setContentPane(sc);
    }

}