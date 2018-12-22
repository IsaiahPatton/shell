package me.isaiah.shell.programs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import me.isaiah.shell.FileExplorer;
import me.isaiah.shell.Main;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.JWebApp;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name = "Unknown File Type")
public class ProgramFileTypeOpener extends JProgram {

    private static final long serialVersionUID = 1L;

    public ProgramFileTypeOpener(File f) {

        JComboBox<String> cb = new JComboBox<>();
        cb.addItem("Notepad");
        cb.addItem("Image View");
        cb.addItem("File Explorer");
        cb.addItem("New JWebApp");

        JButton b = new JButton("Open as selected program");
        b.addActionListener(l -> {
            switch (cb.getSelectedIndex()) {
                case 0:
                    try { Main.newNotePad(f); } catch (IOException e) {e.printStackTrace(); }
                    break;
                case 1:
                    Main.newImageView(f);
                    break;
                case 2:
                    FileExplorer fe = new FileExplorer(f);
                    Main.p.add(fe, fe.getWidth(), fe.getHeight());
                    break;
                case 3:
                    String html = "";
                    try {
                        for (String s : Files.readAllLines(f.toPath())) html += s;
                    } catch (IOException e) { e.printStackTrace(); }
                    JWebApp w = new JWebApp(html);
                    w.setVisible(true);
                    Main.p.add(w);
                    break;
            }
            dispose();
        });
        
        JPanel p = new JPanel();
        p.add(cb);
        p.add(b);
        setContentPane(p);
        setVisible(true);
        pack();
    }

}