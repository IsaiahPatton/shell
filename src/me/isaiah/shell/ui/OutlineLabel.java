package me.isaiah.shell.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

public class OutlineLabel extends JLabel {

    private static final long serialVersionUID = 1L;
    public Color outlineColor;
    private boolean isPaintingOutline = false;
    private boolean forceTransparent = false;

    public OutlineLabel(String text) {
        super(text);
        this.outlineColor = Color.WHITE;
    }

    @Override
    public Color getForeground() {
        return isPaintingOutline ? outlineColor : super.getForeground();
    }

    @Override
    public boolean isOpaque() {
        return forceTransparent ? false : super.isOpaque();
    }

    @Override
    public void paint(Graphics g) {
        if (isOpaque())
            super.paint(g);

        forceTransparent = true;
        isPaintingOutline = true;
        g.translate(-1, -1); super.paint(g); // 1 
        g.translate( 1,  0); super.paint(g); // 2 
        g.translate( 1,  0); super.paint(g); // 3 
        g.translate( 0,  1); super.paint(g); // 4
        g.translate( 0,  1); super.paint(g); // 5
        g.translate(-1,  0); super.paint(g); // 6
        g.translate(-1,  0); super.paint(g); // 7
        g.translate( 0, -1); super.paint(g); // 8
        g.translate( 1,  0); // 9
        isPaintingOutline = false;
        super.paint(g);
        forceTransparent = false;

    }

}