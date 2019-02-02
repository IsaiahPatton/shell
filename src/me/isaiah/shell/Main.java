package me.isaiah.shell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
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
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.text.DefaultCaret;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.JWebApp;
import me.isaiah.shell.bartray.TaskBarTray;
import me.isaiah.shell.programs.NotePad;
import me.isaiah.shell.programs.ProgramFileTypeOpener;

public class Main {

    public static final String NAME = "Z Desktop Envirement";
    public static final String VERSION = "0.5-dev";
    public static final String INFO = "Version " + VERSION + " on Java %s<br>"
            + "Installed RAM:%s<p>Made possible by<br>- Calculator @ javacodex.com<br>- MineSweeper @ java2s.com</p>";

    public static final Runtime r = Runtime.getRuntime();
    public static final int ram = (int) r.maxMemory() / 1024 / 1024;
    public static JPanel taskbar = new JPanel();
    private static String mem;

    public static JFrame f = new JFrame();
    public static final ZDesktopPane p = new ZDesktopPane(f);

    public static boolean dark = false;

    public static JProgramManager pm;
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
        DebugConsole.init();

        if (ram < 250)
            System.err.println("JVM memory of " + ram + "mb is < than the required 256mb for web browsing");

        double m = ram;
        if (m >= 1024) {
            m = m / 1024;
            String ms = String.valueOf(m);
            if (ms.split("[.]")[1].length() > 3) mem = Double.valueOf(ms.substring(0, ms.indexOf(".") + 2)) + " GB";
            else mem = m + " GB";
        } else mem = m + " MB";

        p.setBackground(new Color(51, 153, 255));

        if (pStorage.exists()) {
            try {
                FileInputStream fis = new FileInputStream(pStorage);
                ObjectInputStream ois = new ObjectInputStream(fis);
                pr = (ArrayList<String>) ois.readObject();
                ois.close();
            } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
        }

        pm = new JProgramManager();
        for (String s : pr) {
            try {
                pm.loadProgram(new File(s));
            } catch (Exception e) { System.err.println("Program Manager Unable to load '" + s + "':" + e.getLocalizedMessage());}
        }

        p.setVisible(true);

        JPanel base = new JPanel();
        JButton menu = new JButton("Menu");
        menu.setBackground(Color.GREEN);
        menu.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GREEN.darker(), 2), menu.getBorder()));
        menu.addMouseListener(MouseClick.click(e -> StartMenu.start()));

        taskbar.setMaximumSize(new Dimension(10000, 50));
        taskbar.setLayout(new BorderLayout());
        taskbar.add(menu, BorderLayout.WEST);
        taskbar.add(p.open);
        taskbar.add(new TaskBarTray(), BorderLayout.EAST);
        taskbar.setBackground(new Color(31, 70, 250));

        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));
        base.add(p);
        base.add(taskbar);
        taskbar.setPreferredSize(new Dimension(taskbar.getPreferredSize().width, taskbar.getPreferredSize().height + 10));

        f.setUndecorated(true);
        f.setContentPane(base);
        f.pack();

        f.setVisible(true);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        pm = new JProgramManager();
        Desktop.init();
        setDefaultBackground(base);

        new UpdateCheck();

        f.validate();
    }

    public static void setDefaultBackground(JPanel base) {
        try {
            p.setBackground(ImageIO.read(Main.class.getClassLoader().getResource("bg.jpg")));
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

    public static void showNotification(String tex, int ms) {
        showNotification(tex, ms, 420, 110);
    }

    public static void showNotification(String tex, int ms, int width, int height) {
        showNotification(tex, new Font("Arial", Font.PLAIN, 13), ms, width, height);
    }

    public static void showNotification(String tex, Font fo, int ms, int width, int height) {
        Notification n = new Notification(tex, ms);
        n.setSize(width,height);
        n.getContent().setFont(fo);
        n.setLocation((f.getWidth() - width) - 5, ((f.getHeight() - height) - 50) - ((Notification.shown - 1) * (3 + height)));
        n.validate();
        p.add(n, width, height);
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
                newImageView(file);

            else if (name.endsWith(".txt") || name.endsWith(".text") || name.endsWith(".html"))
                p.add( new NotePad(file) );
            else if (name.endsWith(".jar")) pm.loadProgram(file, true);

            else
                p.add(new ProgramFileTypeOpener(file));
        }
    }

    public static void newImageView(File img) {
        JLabel l = new JLabel();
        try {
            l.setIcon(new ImageIcon(ImageIO.read(img)));
        } catch (IOException e) {
            l.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
        JProgram i = new JProgram("Image Viewer");
        i.setContentPane(new JScrollPane(l));
        i.pack();
        p.add(i);
    }

    protected static final void taskManager() {
        JProgram inf = new JProgram("Task Manager");
        JPanel pan = new JPanel();
        JTextArea a = new JTextArea();
        try {
            a.setText(getTasks());
        } catch (IOException | InterruptedException e1) { e1.printStackTrace(); }

        pan.add(new JScrollPane(a));
        a.setMargin(new Insets(0, 5, 5, 5));
        ((DefaultCaret)a.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        new Timer(4000, l -> {
            try { a.setText(getTasks()); } catch (IOException | InterruptedException e) { e.printStackTrace(); }
        }).start(); 

        a.setEditable(false);
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        inf.setContentPane(pan);
        p.add(inf, 550, 350);
    }

    private static final String getTasks() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("tasklist").redirectErrorStream(true);
        Process process = processBuilder.start();
        String s = "";
        try (BufferedReader proOut = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
            String line;
            while ((line = proOut.readLine()) != null) {
                List<String> list = Arrays.asList(line.split(" "));
                String ss = list.toString().substring(1).replaceAll(" ,", "");
                String[] ssa = ss.split(" ");
                ssa[ssa.length - 1] = ""; // Remove K

                if (ssa.length > 1) {
                    try {
                        int kb = Integer.valueOf(ssa[ssa.length - 2].replaceAll(",", ""));
                        ssa[ssa.length - 2] = kb > 1024 ? (kb / 1024) + "MB" : kb + "KB";
                    } catch (NumberFormatException e) {}
                }

                for (String str : ssa) {
                    while (str.length() < 30) str += " ";

                    s += "\t" + str.replace(",", "");
                }
                s += "\n";
            }

            process.waitFor();
        }
        process.destroy();
        return s;
    }

    protected static final void about() {
        JWebApp w = new JWebApp("<title>About</title><div style='background-color:#e6e6e6;padding-bottom:8px;padding-right:14px;"
                + "padding-left:14px;'><h1>" + NAME + "</h1><hr>" + String.format(INFO, 
                        System.getProperty("java.version"), mem) + "</div>");
        w.setResizable(false);
        w.setVisible(true);
        p.add(w);
    }

}