package com.fungus_soft.desktop;

import static com.fungus_soft.desktop.Main.p;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class Desktop {

    private static int x, y;
    public static List<String> hidden = Arrays.asList(".exe", ".lnk", "desktop.ini");
    private static int pX, pY;

    public static void reset() {
        x = y = 16;
    }

    public static void init() {
        reset();
        File desktop = new File(System.getProperty("user.home"), "desktop");

        for (File f : desktop.listFiles()) {
            boolean stop = false;
            for (String hid : hidden)
                if (f.getName().endsWith(hid)) stop = true;
            if (stop) continue;

            Icon i = new Icon(f, true, Color.WHITE, true);
            JInternalFrame ic = new JInternalFrame();
            ic.setOpaque(false);
            ic.setName("DESKTOP_ICON");

            if (y > (Main.f.getHeight() - 120) && Main.f.getHeight() > 2) {
                y = 18;
                x = x + 80;
            }

            ic.setLocation(x, y);
            ((BasicInternalFrameUI)ic.getUI()).setNorthPane(null);
            ic.setContentPane(i);

            MouseAdapter m = new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    pX = me.getX();
                    pY = me.getY();
                }
                public void mouseDragged(MouseEvent me) {
                    ic.setLocation(ic.getLocation().x + me.getX() - pX, ic.getLocation().y + me.getY() - pY);
                }
            };

            i.addMouseListener(m);
            i.addMouseMotionListener(m);

            ic.setBorder(null);
            ic.setVisible(true);
            ic.pack();
            y += ic.getHeight() + 28;

            p.setComponentZOrder(p.add(ic), 0);
            if (p.getPosition(ic) > 0)
                p.moveToBack(ic);

            ic.setSize(64, ic.getHeight());
        }
    }

}