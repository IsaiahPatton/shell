package me.isaiah.shell.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import me.isaiah.shell.Icon;
import me.isaiah.shell.Main;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;
import me.isaiah.shell.api.Toast;
import me.isaiah.shell.theme.IconPack;

// TODO Create advanced File Manager
@ProgramInfo(name = "File Explorer")
public class FileExplorer extends JProgram {

    private static final long serialVersionUID = 1L;

    public FileExplorer() {
        this(new File(System.getProperty("user.home")));
    }

    public FileExplorer(File folder) {
        this.setFrameIcon(IconPack.get().folder);
        int le = folder.listFiles().length;
        JPanel pan = new JPanel(new GridLayout(le > 5 ? le / 5 : 3, 1));
        JPanel pa = new JPanel();

        JTextField field = new JTextField(folder.getAbsolutePath());
        JButton back = new JButton("<");
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
        field.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {
                File z = new File(field.getText());
                if (z.exists()) {
                    if (z.isDirectory()) {
                        boolean max = isMaximum();
                        pan.removeAll();
                        pan.validate();
                        int le2 = z.listFiles().length;
                        pan.setLayout(new GridLayout(le2 > 5 ? le2 / 5 : 3, 1));
                        if (!max) setSize(pa.getSize());
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
                        pack();
                        pan.validate();
                        if (max) try { setMaximum(true); } catch (PropertyVetoException e1) { e1.printStackTrace(); }
                    } else newExplorer(z);
                }}});
        JScrollPane sp = new JScrollPane();
        sp.setViewportView(pan);
        pa.add(sp);
        pa.setLayout(new BoxLayout(pa, BoxLayout.Y_AXIS));
        setSize(pa.getSize());
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
                new ImageViewer(file);

            else if (name.endsWith(".txt") || name.endsWith(".text") || name.endsWith(".html"))
                Main.p.add(new NotePad(file));
            else if (name.endsWith(".jar")) Main.pm.loadProgram(file, true, true);

            else Main.p.add(new ProgramFileTypeOpener(file));
        }
    }

}