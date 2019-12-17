package me.isaiah.shell;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import static me.isaiah.shell.Main.p;

public class Desktop {

    private static int x, y;
    public static List<String> hidden = Arrays.asList(".exe", ".lnk", "desktop.ini");
    private static int pX, pY;

    public static void reset() {
        x = 10;
        y = 10;
    }

    public static void init() {
        reset();
        File desktop = new File(System.getProperty("user.home"), "desktop");

        new Thread(() -> {
            for (File f : desktop.listFiles()) {
                boolean stop = false;
                for (String hid : hidden)
                    if (f.getName().endsWith(hid)) stop = true;
                if (stop) continue;

                Icon i = new Icon(f, true, Color.WHITE, true);
                JInternalFrame ic = new JInternalFrame();
                ic.setBackground(new Color(0,0,0,0));
                ic.setOpaque(false);
                ic.setName("DESKTOP_ICON");

                if (y > (p.getHeight() - 130) && p.getHeight() > 2) {
                    y = 10;
                    x = x + 75;
                }

                ic.setLocation(x, y);
                BasicInternalFrameUI ui = ((BasicInternalFrameUI)ic.getUI());
                ui.setNorthPane(null);
                ic.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
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
                y += ic.getHeight() + 14;
                p.add(ic);
                p.setComponentZOrder(ic, 0);
                if (p.getPosition(ic) > 0)
                    p.moveToBack(ic);
                p.validate();
                ic.setSize(62, ic.getHeight());
            }
        }, "DesktopInit").start();
    }

}