package me.isaiah.shell;

import java.awt.Color;
import java.io.File;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class Desktop {

    private static int x = 10, y = 14;
    public static void init() {

        File desktop = new File(System.getProperty("user.home"), "desktop");

        JDesktopPane p = Main.p;
        for (File f : desktop.listFiles()) {
            JInternalFrame ic = new JInternalFrame();
            Icon i = new Icon(f, true);
            ic.setBackground(new Color(0,0,0,0));
            ic.setOpaque(false);

            if (y > (p.getHeight() - 70) && p.getHeight() > 2) {
                y = 14;
                x = x + 73;
            }

            ic.setLocation(x, y);
            ic.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
            ic.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            ((BasicInternalFrameUI)ic.getUI()).setNorthPane(i);

            ic.setBorder(null);
            ic.setVisible(true);
            ic.pack();
            y = y + ic.getHeight();
            p.add(ic);
            p.setComponentZOrder(ic, 0);
            p.moveToBack(ic);
        }

    }

}