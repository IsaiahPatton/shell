package me.isaiah.shell.bartray;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;

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

        setBackground(new Color(37, 138, 252));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 4));
        inst = this;

        TrayIcon t = new TrayIcon(getTime(), null);
        t.setOpaque(true);
        t.setForeground(new Color(198, 198, 198));
        t.setBackground(new Color(37, 138, 252));
        t.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 4));

        Timer timer = new Timer(20000, al -> t.setText(getTime()));
        timer.start();

        add(new InternetIcon());

        add(t);
    }

    public static TaskBarTray get() {
        return inst;
    }

    @SuppressWarnings("deprecation")
    public static String getTime() {
        Date d = new Date();
        String[] txt = d.toString().split(" ");
        int hour = d.getHours();
        String apm = " AM";
        if (hour > 13) {
            hour -= 12;
            apm = " PM";
        }

        txt[3] = "" + hour + ":" + d.getMinutes() + apm;
        txt[4] = "";

        String tx = "";
        for (String tx2 : txt) tx += tx2 + " ";

        return tx;
    }

}