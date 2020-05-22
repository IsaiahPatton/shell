package com.fungus_soft.desktop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.fungus_soft.desktop.Main;

public class LoadingScreen extends JPanel {

    private static final long serialVersionUID = 1L;
    private Timer t = null;

    public LoadingScreen() {
        t = new Timer(10, a -> {
            if (Main.p.img != null) {
                Main.f.setContentPane(Main.base);
                Main.f.validate();
                Main.p.add(new SystemBar());
                t.stop();
            }
        });
        t.start();
    }

    @Override
    public void paint(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(50, 50, 50));
        int barSize = 60;
        g.fillRect(0, 0, getWidth(), barSize);
        g.fillRect(0, getHeight() - barSize, getWidth(), barSize);

        g.setColor(new Color(50, 104, 168));
        g.fillRect(0, barSize, getWidth(), getHeight() - barSize*2);

        float fontF = 64;
        int font = (int)fontF;
        String txt = "Welcome";
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(64f));
        g.drawString(txt, getWidth()/2 - (font/4)*txt.length() - 50, getHeight()/2 - (((font/4)*txt.length())/2) + 15);

        fontF = 16;
        font = 16;
        txt = "Loading";
        g.setFont(g.getFont().deriveFont(fontF));
        g.drawString(txt, getWidth()/2 - (font/4)*txt.length() - 35, getHeight()/2 - (((font/4)*txt.length())/2) + 25);
    }

}