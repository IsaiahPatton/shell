package me.isaiah.shell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
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
import java.util.Random;

import javax.imageio.ImageIO;
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

import javafx.embed.swing.JFXPanel;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.programs.Browser;
import me.isaiah.shell.programs.ProgramManager;

public class Main {

    public static final String NAME = "Z Desktop Envirement";
    public static final String VERSION = "0.5-dev";
    public static final Runtime r = Runtime.getRuntime();
    public static final int ram = (int) ((r.maxMemory() / 1024) / 1024);
    private static String mem;
    public static JPanel taskbar = new JPanel();
    public static final ZDesktopPane p = new ZDesktopPane();

    private static JProgramManager pm;
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
    private static JFrame f;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        DebugConsole.init();
        if (ram < 64) System.err.println("JVM memory (" + ram + " MB) is not > 64 MB for good proformance!");

        if (ram < 256)
            System.err.println("JVM max memory of " + ram + " MB does not meet the required 256 MB for web browsing");

        double m = ram;
        if (m > 1023) {
            m = m / 1024;
            String ms = String.valueOf(m);
            if (ms.split("[.]")[1].length() > 3) mem = Double.valueOf(ms.substring(0, ms.indexOf(".") + 2)) + " GB";
            else mem = m + " GB";
        } else mem = m + " MB";

        System.out.println("Version " + VERSION);

        f = new JFrame();
        p.setBackground(new Color(51, 153, 255));

        JMenuBar b = new JMenuBar();

        b.add(new JMenu(" Exit ")).addMouseListener(new MouseClick() { @Override public void click(MouseEvent e) {System.exit(0);}});
        b.add(new JMenu(" About ")).addMouseListener(new MouseClick(){ @Override public void click(MouseEvent e) {about();}});

        JMenu sys = new JMenu("System");
        sys.add("Program Manager").addActionListener(l -> start(new ProgramManager(), 500, 500));
        sys.add("DebugConsole").addActionListener(l -> start(new DebugConsole(), 850, 500));
        b.add(sys);

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
                File program = new File(s);
                System.err.println(program.getAbsolutePath());

                pm.loadProgram(program);
            } catch (Exception e) { System.err.println("[ProgramManager]: Unable to load '" + s + "':" + e.getLocalizedMessage());}
        }

        //b.add(programs);
        //f.setJMenuBar(b);

        p.setVisible(true);

        JPanel base = new JPanel();
        taskbar.setMaximumSize(new Dimension(10000, 50));

        taskbar.setLayout(new BorderLayout());
        JButton menu = new JButton("Menu");
        taskbar.add(menu, BorderLayout.WEST);
        taskbar.add(b, BorderLayout.EAST);
        menu.setBackground(Color.GREEN);
        b.setOpaque(false);
        taskbar.setBackground(new Color(31, 70, 250));

        menu.addMouseListener(new MouseClick() { @Override public void click(MouseEvent e) { StartMenu.start(); }});

        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));
        base.add(p);
        base.add(taskbar);
        taskbar.setPreferredSize(new Dimension(taskbar.getPreferredSize().width, taskbar.getPreferredSize().height + 10));

        f.setContentPane(base);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setUndecorated(true);
        f.setMaximumSize(new Dimension(500,500));
        f.pack();
        f.setVisible(true);

        pm = new JProgramManager();
        Desktop.init();

        String l = getUrlSource("https://api.github.com/repos/isaiahpatton/shell/releases/latest");
        if (!l.equalsIgnoreCase("internet")) {
            l = l.substring(l.indexOf("\"tag_name\":\"") + 12);
            l = l.substring(0, l.indexOf("\","));
        } else showNotification("Could not connect to api.github.com\nto get update infomation.", new Font("Arial", Font.PLAIN, 13),
                5000, 320, 60);

        if (!l.equalsIgnoreCase("internet") && !l.equalsIgnoreCase(VERSION))
            showNotification("A new update is out!\n  Current version: " + VERSION + "\n  Latest version: "
                    + l + "\nhttp://isaiahpatton.github.io/jshell", new Font("Arial", Font.BOLD, 14), 10000, 420, 110);
        f.validate();
        try {
            p.setBackground(ImageIO.read(new URL("https://avatars.mds.yandex.net/get-pdb/33827/369cb281-1bb7-448d-9df1-cbe68da08025/orig")));
        } catch (IOException e1) { e1.printStackTrace(); }
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
        n.setLocation((f.getWidth() - width) - 5, (f.getHeight() - height) - 50);
        n.validate();
        start(n, width, height);
    }

    public static String getInfo() {
        return "Version " + VERSION + " on Java " + System.getProperty("java.version") + "\n"
                + "Installed RAM: " + mem + "\n\nMade possible by:\n - Calculator @ javacodex.com\n - MineSweeper @ java2s.com";
    }

    protected static void newFileExplorer(File file) {
        if (file.isDirectory()) {
            FileExplorer e = new FileExplorer(file);
            start(e, e.getWidth(), e.getHeight());
        } else {
            String name = file.getName();
            if (name.endsWith(".exe"))
                JOptionPane.showInternalMessageDialog(p, "Unsupported File type", "Explorer", 0);
            
            if (name.endsWith(".png") || name.endsWith(".jpg")) newImageView(file);

            if (name.endsWith(".txt") || name.endsWith(".text") || name.endsWith(".html"))
                try { newNotePad(file); } catch (IOException e) { e.printStackTrace(); showNotification(e.getMessage(), 5000); }
            if (name.endsWith(".jar")) pm.loadProgram(file, true);
        }
    }
    
    protected static void newImageView(File img) {
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

    protected static void newNotePad(File file) throws IOException {
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

        pa.add(new JScrollPane(a));
        m.add(mf);
        inf.setContentPane(pa);
        inf.setClosable(true);
        inf.setJMenuBar(m);
        inf.setSize(new Dimension(520,500));
        inf.setVisible(true);
        p.add(inf);
    }

    @Deprecated
    protected static void startBrowser() {
        new JFXPanel(); // init JavaFX
        System.out.println("Starting ZunoZap");
        Browser.runAsProgram();
    }

    protected static final void taskManager() {
        JProgram inf = new JProgram("Task Manager");
        JPanel pan = new JPanel();
        JTextArea a = new JTextArea();
        try {
            a.setText(getTasks());
        } catch (IOException | InterruptedException e1) { e1.printStackTrace(); }
        JScrollPane bar = new JScrollPane();
        bar.setViewportView(a);
        a.setMargin(new Insets(0, 5, 5, 5));
        ((DefaultCaret)a.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        new Timer(4000, (l) -> {
            try { a.setText(getTasks()); } catch (IOException | InterruptedException e) { e.printStackTrace(); }
        }).start(); 
        a.setEditable(false);
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        pan.add(bar);
        inf.setContentPane(pan);
        start(inf, 550, 350);
    }

    private static final String getTasks() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("tasklist").redirectErrorStream(true);
        Process process = processBuilder.start();
        String s = "";
        try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
            String readLine;
            while ((readLine = processOutputReader.readLine()) != null) s += readLine + "\n";

            process.waitFor();
        }
        process.destroy();
        return s;
    }

    protected static final void about() {
        JProgram inf = new JProgram("About", false, true, false);
        JPanel pan = new JPanel();
        JTextArea n = new JTextArea(NAME);
        n.setFont(new Font(n.getFont().getName(), Font.BOLD, 24));
        JTextArea a = new JTextArea(getInfo());
        a.setEditable(false);
        a.setMargin(new Insets(12, 15, 15, 15));
        n.setEditable(false);
        n.setMargin(new Insets(15, 15, 1, 15));
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        pan.add(n);
        pan.add(a);
        inf.setSize(300, 300);
        inf.setContentPane(pan);
        inf.setVisible(true);
        inf.pack();
        p.add(inf);
    }

    public static final void start(JProgram j, int width, int height) {
        j.setIconifiable(true);
        j.setSize(width, height);
        p.add(j);
    }

}