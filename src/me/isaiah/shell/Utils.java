package me.isaiah.shell;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Utils {

    public static void runAsync(Runnable r) {
        new Thread(r).start();
    }

    public static MouseAdapter click(IClick e) {
        return new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e2) { e.click(e2); }
        };
    }

    public interface IClick {
        public void click(MouseEvent e);
    }

    public static String getJavaVersion() {
        double i = Double.valueOf(System.getProperty("java.class.version"));
        return (i - 44) + "&nbsp;<small>(" + System.getProperty("java.version") + "-" + System.getProperty("java.vendor") + ")</small>";
    }

}