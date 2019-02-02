package me.isaiah.shell;

import java.awt.Color;
import java.io.File;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class Desktop {

    private static int x = 10, y = 0;

    public static void reset() {
        x = 10;
        y = 10;
    }

    public static void init() {
        new Thread(() -> {
            File desktop = new File(System.getProperty("user.home"), "desktop");

            JDesktopPane p = Main.p;
            for (File f : desktop.listFiles()) {
                Icon i = new Icon(f, true, Color.LIGHT_GRAY);
                JInternalFrame ic = new JInternalFrame();
                ic.setBackground(new Color(0,0,0,0));
                ic.setOpaque(false);
                ic.setName("DESKTOP_ICON");

                if (y > (p.getHeight() - 100) && p.getHeight() > 2) {
                    y = 0;
                    x = x + 75;
                }

                ic.setLocation(x, y);
                BasicInternalFrameUI ui = ((BasicInternalFrameUI)ic.getUI());
                ui.setNorthPane(i);

                ic.setBorder(null);
                ic.setVisible(true);
                ic.pack();
                y += ic.getHeight() / 1.3;
                p.add(ic);
                p.setComponentZOrder(ic, 0);
                if (p.getPosition(ic) > 0)
                    p.moveToBack(ic);
                p.validate();
                ic.setSize(62, 112);
            }
        }, "DesktopInit").start();
    }

}