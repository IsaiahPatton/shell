package com.fungus_soft.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fungus_soft.ui.ModernButton;
import com.fungus_soft.ui.ModernScrollPane;
import com.fungus_soft.ui.ModernTextField;
import com.fungus_soft.ui.WrapLayout;

import com.fungus_soft.desktop.Icon;
import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramFileTypeOpener;
import com.fungus_soft.desktop.api.ProgramInfo;
import com.fungus_soft.desktop.api.Toast;

// TODO Create advanced File Manager
@ProgramInfo(name = "File Explorer")
public class FileExplorer extends JProgram {

    private static final long serialVersionUID = 1L;

    public FileExplorer() {
        this(new File(System.getProperty("user.home")));
    }

    public FileExplorer(File folder) {
        JPanel pan = new JPanel(new WrapLayout());
        JPanel pa = new JPanel();
        ModernScrollPane sp = new ModernScrollPane(pan, 22, 30);
        sp.THUMB_COLOR = Color.DARK_GRAY;

        JTextField field = new ModernTextField(folder.getAbsolutePath());
        JButton back = new ModernButton("<");
        back.addActionListener(a -> {
            File f = new File(field.getText());
            if (f.isDirectory() && f.getParent() != null) field.setText(f.getParentFile().getAbsolutePath());
            field.getKeyListeners()[0].keyReleased(null);
        });

        JPanel pa2 = new JPanel();
        pa2.add(back, BorderLayout.WEST);
        pa2.add(field, BorderLayout.EAST);
        back.setPreferredSize(new Dimension(back.getPreferredSize().width, field.getHeight() - 30));
        pa2.setLayout(new BoxLayout(pa2, BoxLayout.X_AXIS));
        pa.add(pa2);

        for (File fil : folder.listFiles()) {
            Icon ic = new Icon(fil, false, Color.BLACK, false);
            ic.addActionListener(l -> {
                if (fil.isDirectory()) {
                    field.setText(fil.getAbsolutePath());
                    field.getKeyListeners()[0].keyReleased(null);
                } else newExplorer(fil);
            });
            ic.setMaximumSize(new Dimension(200, 200));
            pan.add(ic);
        }
        field.setSize(field.getWidth(), 100);
        field.setMaximumSize(new Dimension(100000, 100));
        field.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                File z = new File(field.getText());
                if (z.exists()) {
                    if (z.isDirectory()) {
                        pan.removeAll();
                        pan.validate();

                        for (File fi : z.listFiles()) {
                            Icon ic = new Icon(fi, false, Color.BLACK, false);
                            ic.addActionListener(l -> {
                                if (fi.isDirectory()) {
                                    field.setText(fi.getAbsolutePath());
                                    field.getKeyListeners()[0].keyReleased(null);
                                } else newExplorer(fi);
                            });
                            pan.add(ic);
                        }
                        pan.validate();
                        sp.setViewportView(pan);
                    } else newExplorer(z);
                }
            }});
        pa.add(sp);
        pa.setLayout(new BoxLayout(pa, BoxLayout.Y_AXIS));
        setSize(650, 450);

        setContentPane(pa);
    }

    public static void newExplorer(File file) {
        if (file.isDirectory()) {
            FileExplorer e = new FileExplorer(file);
            Main.p.add(e, e.getWidth(), e.getHeight());
        } else {
            String name = file.getName();
            if (name.endsWith(".exe"))
                Toast.show("Unsupported File Type", 4000);
            else if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".jpeg")) 
                Main.p.add(new ImageViewer(file));
            else if (name.endsWith(".txt") || name.endsWith(".text") || name.endsWith(".html"))
                Main.p.add(new Notepad(file));
            else if (name.endsWith(".jar")) Main.pm.loadProgram(file, true, true);

            else Main.p.add(new ProgramFileTypeOpener(file));
        }
    }

}