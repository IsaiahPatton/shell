package me.isaiah.shell.api.loader;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import me.isaiah.shell.api.JProgram;

public class JFrameWrapper extends JFrame {

    public JProgram jp;

    public JFrameWrapper() {
        this.jp = new JProgram();
    }

    public JFrameWrapper(String title) {
        this();
        setTitle(title);
    }

    public JFrameWrapper(GraphicsConfiguration gc) {this();}
    public JFrameWrapper(String title, GraphicsConfiguration gc) { this(title); }

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
    public void addMouseMotionListener(MouseMotionListener l) {
        jp.addMouseMotionListener(l);
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
    public Point getLocation() {
        return jp.getLocation();
    }

    @Override
    public void setLocation(Point p) {
        jp.setLocation(p);
    }

    @Override
    public void setBounds(Rectangle r) {
        jp.setBounds(r);
    }

    @Override
    public Rectangle getBounds() {
        return jp.getBounds();
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
    public JMenuBar getJMenuBar() {
        return jp.getJMenuBar();
    }

    @Override
    public JRootPane getRootPane() {
        return jp.getRootPane();
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
