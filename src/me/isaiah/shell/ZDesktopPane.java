package me.isaiah.shell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import me.isaiah.shell.api.JProgram;

public class ZDesktopPane extends JDesktopPane {

    private static final long serialVersionUID = 1L;
    private Image img = null;
    private JFrame f;

    public ZDesktopPane(JFrame parent) {
        super();
        this.f = parent;
        this.addMouseListener(MouseClick.click(e -> StartMenu.stop()));
    }

    public void setBackground(Image img) {
        this.img = img.getScaledInstance(f.getWidth(), f.getHeight(), 0);

        // "Refresh" Screen
        JInternalFrame b = new JInternalFrame();
        b.setLocation(0, 0);
        b.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        b.setVisible(true);
        b.setSize(Main.p.getWidth(), Main.p.getHeight());
        b.validate();
        Main.p.add(b);
        Main.p.moveToBack(b);
        Main.p.remove(b);
    }

    public void add(JProgram j, int width, int height) {
        j.setIconifiable(true);
        j.setSize(width, height);
        add(j);
    }

    @Override
    public void addImpl(Component j, Object constraints, int index) {
        StartMenu.stop();

        j.setVisible(true);
        super.addImpl(j, constraints, index);
        moveToFront(j);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (null != img)
            g.drawImage(img, 0, 0, null);

        g.setColor(Color.LIGHT_GRAY);
        g.drawString("ZDE v" + Main.VERSION, getWidth() - 100, 20);
    }

}