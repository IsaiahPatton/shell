package me.isaiah.shell.bartray;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Timer;

import com.fungus_soft.desktop.SystemBar;
import com.fungus_soft.desktop.Utils;

@Deprecated
public class PowerIcon extends TrayIcon {

    private static final long serialVersionUID = 1L;
    public int left;
    public int pers;
    public boolean hasBattery;

    public PowerIcon() {
        super("Power", null);

        Timer t = new Timer(10000, a -> {
            Utils.runAsync(() -> {
                String[] txt = system(getOS() == OS.WINDOWS ? 
                        "WMIC PATH Win32_Battery Get EstimatedChargeRemaining" :
                         "upower -i $(upower -e | grep '/battery') | grep --color=never -E percentage|xargs|cut -d' ' -f2|sed s/%//", true);
                if (txt[2].trim().length() < 1) {
                    return;
                }
                left = Integer.valueOf(txt[2].trim());
                pers = (left * 16) / 100;
                setToolTipText("Battery left: " + left + "%");
                repaint();
            });
        });
        this.setForeground(Color.WHITE);
        t.setInitialDelay(1);
        t.start();
        SystemBar.get.validate();
    }

    @Override
    public void paint(Graphics g) {
        g.drawLine(4, 0, getWidth() - 8, 0);
        g.drawLine(4, getHeight() - 3, getWidth() - 8, getHeight() - 3);
        g.drawLine(4, 0, 4, getHeight() - 3);
        g.drawLine(getWidth() - 8, 0, getWidth() - 8, getHeight() - 3);
        g.fillRect(getWidth() - 8, 3, 4, 7);

        g.fillRect(6, 2, pers + 6, getHeight() - 6);
    }

    public boolean hasBattery() {
        return false;
    }

}