package me.isaiah.shell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import me.isaiah.shell.api.JWebApp;
import me.isaiah.shell.api.loader.JProgramManager;
import me.isaiah.shell.programs.console.AdminConsole;
import me.isaiah.shell.theme.DefaultTheme;
import me.isaiah.shell.theme.Theme;

public class Main {

    public static final String NAME = "Fungus Desktop Envirement";
    public static final String VERSION = "0.6-Dev";

    public static boolean isLowMemory;

    public static int ram = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
    private static String mem;

    public static JFrame f = new JFrame();
    public static final ZDesktopPane p = new ZDesktopPane(f);
    public static JPanel base;

    public static JProgramManager pm;
    public static File pStorage = new File(new File(new File(System.getProperty("user.home")),"shell"), "programs.dat");

    public static ArrayList<String> pr = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;
        @Override public boolean add(String z) {
            boolean b = super.add(z);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(pStorage);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(pr);
                oos.close();
            } catch (IOException e) { e.printStackTrace(); }
            return b;
        }
    };

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        isLowMemory = ram <= 128;

        double m = ram;
        if (m >= 1024) {
            m = m / 1024;
            String ms = String.valueOf(m);
            if (ms.split("[.]")[1].length() > 3) mem = Double.valueOf(ms.substring(0, ms.indexOf(".") + 2)) + " GB";
            else mem = m + " GB";
        } else mem = m + " MB";

        p.setOpaque(false);

        if (pStorage.exists()) {
            try {
                FileInputStream fis = new FileInputStream(pStorage);
                ObjectInputStream ois = new ObjectInputStream(fis);
                pr = (ArrayList<String>) ois.readObject();
                ois.close();
            } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
        }

        pm = new JProgramManager();

        JPanel loading = new LoadingScreen();

        base = new JPanel() {

            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (!isLowMemory && null != p.img)
                    g.drawImage(p.img, 0, 0, null);

                if (isLowMemory)
                    g.drawString("LOW MEMORY MODE [" + mem + "]", (getWidth() / 2) - 70, getHeight() / 2);

                g.setColor(Color.GRAY);
                g.drawString("Version " + Main.VERSION, getWidth() - (102 + (Main.VERSION.length() * 3)), 18);
                g.drawString("Java Verison: " + System.getProperty("java.version"), getWidth() - 151, 33);
                g.drawString("JVM Memory: " + mem, getWidth() - 151, 47);
            }
        };
        base.setBackground(new Color(60, 123, 250));
        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));
        base.add(p);

        f.setUndecorated(true);
        f.setContentPane(loading);
        f.setMinimumSize(new Dimension(800, 600));
        f.pack();

        f.setVisible(true);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Desktop.init();
        setDefaultBackground(base);

        Theme.setCurrentTheme(new DefaultTheme());
        AdminConsole.init();
        f.validate();
    }

    public static void setDefaultBackground(JPanel base) {
        try {
            p.setBackground(ImageIO.read(Main.class.getClassLoader().getResource("res/background/material-design-1920x1200-stock-yellow-shapes-material-hd-14202.jpg")));
        } catch (IOException e1) { e1.printStackTrace(); }
    }

    public static final void about() {
        JWebApp w = new JWebApp("<title>About</title><div style='background-color:#e6e6e6;padding:8px 16px 8px 16px;'><h2>" + NAME + "</h2>"
                + "<div style='padding:16px 0 8px 12px'>Version " + VERSION + "<br>Java version: " + Utils.getJavaVersion()
                + "<br>Heap size: " + mem + "</div><br><small>&copy; 2020 Fungus Software - https://fungus-soft.com/</small></div>");
        w.setResizable(false);
        w.setMaximizable(false);
        //w.setSize(307,190);
        w.setVisible(true);
        p.add(w);
    }

}