package me.isaiah.shell.bartray;

import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TaskBarTray extends JPanel {

    private static final long serialVersionUID = 1L;
    private static TaskBarTray inst;

    public TaskBarTray() {
        if (null != inst)
            return;

        this.setMaximumSize(new Dimension(10000, 50));

        Color bg = new Color(0,0,0, 150);
        setBackground(bg);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 4));
        inst = this;

        TrayIcon t = new TrayIcon(getTime(), null);
        t.setOpaque(true);
        t.setForeground(new Color(198, 198, 198));
        t.setBackground(bg);
        t.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 4));

        Timer timer = new Timer(20000, al -> t.setText(getTime()));
        timer.start();

        add(new InternetIcon());
        PowerIcon power = new PowerIcon();
        if (power.hasBattery())
            add(power);

        add(t);
    }

    public static TaskBarTray get() {
        return inst;
    }

    public static String getTime() {
        LocalDateTime d = LocalDateTime.now();

        int hour = d.getHour();
        String apm = " AM";
        if (hour > 13) {
            hour -= 12;
            apm = " PM";
        }
        if (hour == 0) hour = 12;

        return hour + ":" + d.getMinute() + apm + " " + d.getMonthValue() + "/" + d.getDayOfMonth() + "/" + d.getYear();
    }

}