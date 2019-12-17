package me.isaiah.shell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jthemes.ThemeUtils;
import jthemes.themes.ModernTheme;
import me.isaiah.shell.api.JWebApp;
import me.isaiah.shell.programs.console.AdminConsole;
import me.isaiah.shell.programs.FileExplorer;
import me.isaiah.shell.programs.ImageViewer;
import me.isaiah.shell.programs.NotePad;
import me.isaiah.shell.programs.ProgramFileTypeOpener;
import me.isaiah.shell.theme.DefaultIconPack;
import me.isaiah.shell.theme.IconPack;

public class Main {

    public static final String NAME = "Desktop Envirement";
    public static final String VERSION = "0.6-Dev";

    public static boolean isLowMemory;

    public static final String INFO = NAME + " version " + VERSION + "<br><br>Running on Java %s<br>"
            + "Heap size: %s<br>Low Memory Mode: " + isLowMemory;

    public static final Runtime r = Runtime.getRuntime();
    public static int ram = (int) (r.maxMemory() / 1024 / 1024);
    private static String mem;

    public static JFrame f = new JFrame();
    public static final ZDesktopPane p = new ZDesktopPane(f);

    //public static JProgramManager pm;
    protected static File pStorage = new File(new File(new File(System.getProperty("user.home")),"shell"), "programs.dat");

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
    public static void init() {
        //WebLookAndFeel.install ();
        ThemeUtils.setCurrentTheme(new ModernTheme());
        IconPack.setIconPack(new DefaultIconPack());
        AdminConsole.init();

        if (ram < 256)
            System.err.println("JVM memory of " + ram + "mb is < than the required 256mb for web browsing");

        isLowMemory = ram <= 96;

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

        // pm = new JProgramManager();
        //for (String s : pr) {
        //    try {
        //        pm.loadProgram(new File(s));
        //    } catch (Exception e) { System.err.println("Program Manager Unable to load '" + s + "':" + e.getLocalizedMessage());}
        //}

        p.setVisible(true);

        JPanel base = new JPanel() {

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
        f.setContentPane(base);
        f.setMinimumSize(new Dimension(800, 600));
        f.pack();

        f.setVisible(true);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // pm = new JProgramManager();
        Desktop.init();
        setDefaultBackground(base);

        // new UpdateCheck();
        base.validate();
        base.repaint();
        f.validate();
        p.add(new SystemBar());
    }

    public static void setDefaultBackground(JPanel base) {
        try {
            p.setBackground(ImageIO.read(Main.class.getClassLoader().getResource("res/background/material-design-1920x1200-stock-yellow-shapes-material-hd-14202.jpg")));
        } catch (IOException e1) { e1.printStackTrace(); }
    }

    public static void main(String[] args) {
        init();
    }

    public static String getUrlSource(String url) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream(), "UTF-8"))) {
            String line;
            StringBuilder a = new StringBuilder();
            while ((line = in.readLine()) != null) a.append(line);

            return a.toString();
        } catch (IOException e) { return "internet"; }
    }

    public static void newFileExplorer(File file) {
        if (file.isDirectory()) {
            FileExplorer e = new FileExplorer(file);
            p.add(e, e.getWidth(), e.getHeight());
        } else {
            String name = file.getName();
            if (name.endsWith(".exe"))
                JOptionPane.showInternalMessageDialog(p, "Unsupported File type", "Explorer", 0);
            else if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".jpeg")) 
                new ImageViewer(file);

            else if (name.endsWith(".txt") || name.endsWith(".text") || name.endsWith(".html"))
                p.add( new NotePad(file) );
            //else if (name.endsWith(".jar")) pm.loadProgram(file, true);

            else p.add(new ProgramFileTypeOpener(file));
        }
    }

    public static final void about() {
        JWebApp w = new JWebApp("<title>About</title><div style='background-color:#e6e6e6;padding-bottom:8px;padding-right:14px;"
                + "padding-left:14px;'><h1>" + NAME + "</h1><hr>" + String.format(INFO, 
                        System.getProperty("java.version"), mem) + "</div>");
        w.setResizable(false);
        w.setMaximizable(false);
        w.setVisible(true);
        p.add(w);
    }

}