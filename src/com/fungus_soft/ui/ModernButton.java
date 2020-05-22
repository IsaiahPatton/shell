package com.fungus_soft.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

public class ModernButton extends JButton {

    private static final long serialVersionUID = 1L;

    public ModernButton(String text) {
        this();
        this.setText(text);
    }

    public ModernButton(Icon i) {
        this();
        this.setIcon(i);
    }

    public ModernButton(Action a) {
        this();
        this.setAction(a);
    }

    public ModernButton() {
        super();
        this.setFocusable(false); // Because Swing adds an ugly border 
        this.setBackground(new Color(228,228,228));
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(215,215,215), 1), BorderFactory.createEmptyBorder(4,14,4,14)));
    }

    @Override
    public void paint(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
    }

}