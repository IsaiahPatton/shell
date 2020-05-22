package com.fungus_soft.desktop.api.loader;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class JWindowWrapper extends JFrameWrapper {

    private static final long serialVersionUID = 1L;

    public JWindowWrapper() {
        super();
    }

    public JWindowWrapper(String title) {
        super(title);
    }

    @Override
    public void setTitle(String title) {
        jp.setTitle(title);
    }

    @Override
    public void validate() {
        jp.validate();
    }

    @Override
    public void repaint() {
        jp.repaint();
    }

    @Override
    public void setSize(Dimension d) {
        jp.setSize(d);
    }

    @Override
    public Dimension getSize() {
        return jp.getSize();
    }

    @Override
    public void setPreferredSize(Dimension d) {
        jp.setSize(d);
    }

    @Override
    public void addKeyListener(KeyListener l) {
        jp.addKeyListener(l);
    }

    @Override
    public void addMouseListener(MouseListener l) {
        jp.addMouseListener(l);
    }

    @Override
    public void setFocusable(boolean b) {
        jp.setFocusable(b);
    }

    @Override
    public Point getLocationOnScreen() {
        return jp.getLocationOnScreen();
    }

    @Override
    public void setContentPane(Container c) {
        jp.setContentPane(c);
    }

    @Override
    public void setJMenuBar(JMenuBar m) {
        jp.setJMenuBar(m);
    }

    @Override
    public Container getContentPane() {
        if (jp.getContentPane() == null) {
            setContentPane(new JPanel());
        }
        return jp.getContentPane();
    }

    @Override
    public void setVisible(boolean b) {
        jp.setVisible(b);
    }

}
