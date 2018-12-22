package me.isaiah.shell;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name = "File Explorer")
public class FileExplorer extends JProgram {

    private static final long serialVersionUID = 1L;

    public FileExplorer(File folder) {
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
            Icon ic = new Icon(fil, false, Main.dark ? Color.WHITE : Color.BLACK);
            ic.addActionListener(l -> {
                if (fil.isDirectory()) {
                    field.setText(fil.getAbsolutePath());
                    field.getKeyListeners()[0].keyReleased(null);
                } else Main.newFileExplorer(fil);
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
                            Icon ic = new Icon(fi, false, Main.dark ? Color.WHITE : Color.BLACK);
                            ic.addActionListener(l -> {
                                if (fi.isDirectory()) {
                                    field.setText(fi.getAbsolutePath());
                                    field.getKeyListeners()[0].keyReleased(null);
                                } else Main.newFileExplorer(fi);
                            });
                            pan.add(ic);
                        }
                        pack();
                        pan.validate();
                        if (max) try { setMaximum(true); } catch (PropertyVetoException e1) { e1.printStackTrace(); }
                    } else Main.newFileExplorer(z);
                }}});
        JScrollPane sp = new JScrollPane();
        sp.setViewportView(pan);
        pa.add(sp);
        pa.setLayout(new BoxLayout(pa, BoxLayout.Y_AXIS));
        setSize(pa.getSize());
        setSize(650, 450);

        if (Main.dark) {
            pan.setBackground(Color.DARK_GRAY.darker());
            field.setBackground(Color.LIGHT_GRAY);
        }

        setContentPane(pa);
    }

}