package me.isaiah.shell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.text.DefaultCaret;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.JWebApp;
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

    // Config values used for custom versions
    public static boolean undecorated = true;
    public static boolean fullscreen = true;
    public static boolean visible = true;

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

        if (ram < 12) throw new OutOfMemoryError("JVM -Xmx < 12mb");
        if (ram < 96) System.err.println("JVM memory (" + ram + " MB) is not > 96 MB for good proformance!");

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
        menu.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GREEN.darker(), 3), menu.getBorder()));
        menu.addMouseListener(MouseClick.click(e -> StartMenu.start()));

        JLabel t = new JLabel();
        t.setText(getTime());
        t.setOpaque(true);
        t.setForeground(new Color(198, 198, 198));
        t.setBackground(new Color(37, 138, 252));
        t.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 4));

        Timer timer = new Timer(20000, al -> t.setText(getTime()));
        timer.start();

        taskbar.setMaximumSize(new Dimension(10000, 50));
        taskbar.setLayout(new BorderLayout());
        taskbar.add(menu, BorderLayout.WEST);
        taskbar.add(t, BorderLayout.EAST);
        taskbar.setBackground(new Color(31, 70, 250));

        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));
        base.add(p);
        base.add(taskbar);
        taskbar.setPreferredSize(new Dimension(taskbar.getPreferredSize().width, taskbar.getPreferredSize().height + 10));

        f.setUndecorated(undecorated);
        f.setContentPane(base);
        f.pack();
        if (visible) f.setVisible(true);
        if (fullscreen) f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        pm = new JProgramManager();
        Desktop.init();
        setDefaultBackground(base);

        new UpdateCheck();

        f.validate();
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
                try { newNotePad(file); } catch (IOException e) { e.printStackTrace(); showNotification(e.getMessage(), 5000); }
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

    protected static void emptyNotePad() {
        File desktop = new File(new File(System.getProperty("user.home")), "Desktop");
        try {
            newNotePad(new File(desktop, "New-Doc-" + new Random().nextInt(20) + ".txt"));
        } catch (IOException e) { e.printStackTrace(); showNotification(e.getMessage(), 5000); }
    }

    public static void newNotePad(File file) throws IOException {
        JProgram inf = new JProgram("[NotePad] " + file.getName());
        String text = "";
        int i = 0;
        if (file != null && file.exists()) for (String s : Files.readAllLines(file.toPath())) {
            if (i == 1) text += "\n" + s;
            if (i == 0) {
                text += s;
                i++;
            }
        }

        JPanel pa = new JPanel();
        JTextArea a = new JTextArea(text);
        a.setMargin(new Insets(5, 8, 5, 8));
        pa.setLayout(new BoxLayout(pa, BoxLayout.Y_AXIS));
        a.setWrapStyleWord(true);
        a.setSize(200, 300);
        JMenuBar m = new JMenuBar();
        JMenu mf = new JMenu("File");
        mf.add("Save").addActionListener(l -> {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) {
                file.createNewFile();
                for (String line : a.getText().split("\n")) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) { showNotification(e.getMessage(), 3500); e.printStackTrace(); }
        });
        
        mf.add("Open as JWebApp").addActionListener(l -> {
            JWebApp w = new JWebApp(a.getText());
            w.setVisible(true);
            Main.p.add(w);
        });

        if (dark) {
            m.setBackground(Color.LIGHT_GRAY);
            a.setBackground(Color.DARK_GRAY.darker());
            a.setForeground(Color.LIGHT_GRAY);
        }

        pa.add(new JScrollPane(a));
        m.add(mf);
        inf.setContentPane(pa);
        inf.setClosable(true);
        inf.setJMenuBar(m);
        inf.setSize(new Dimension(520,500));
        inf.setVisible(true);
        p.add(inf);
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
            while ((line = proOut.readLine()) != null) s += line + "\n";

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