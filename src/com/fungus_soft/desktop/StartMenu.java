package com.fungus_soft.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.fungus_soft.desktop.Main;
import com.fungus_soft.programs.*;
import com.fungus_soft.ui.ModernScrollPane;

import com.fungus_soft.desktop.Utils.IClick;
import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;
import me.isaiah.shell.programs.ChromeBrowser;
import me.isaiah.shell.programs.WebBrowser;
import com.fungus_soft.desktop.theme.DefaultIconPack;
import com.fungus_soft.desktop.theme.IconPack;

@ProgramInfo(name = "Menu")
public class StartMenu extends JProgram {

    private static boolean debug = false;
    public static void main(String[] args) {
        if (!debug) {
            Main.main(args);
            return;
        }
        IconPack.setIconPack(new DefaultIconPack());
        JInternalFrame inf = new StartMenu();
        JFrame f = new JFrame();
        f.setContentPane(inf);
        f.setDefaultCloseOperation(3);
        f.setSize(inf.getPreferredSize());
        f.setVisible(true);
    }

    public static TreeMap<String, ArrayList<Class<? extends JProgram>>> nameMap = new TreeMap<>();

    public static void addProgram(Class<? extends JProgram> a) {
        String title = a.getAnnotation(ProgramInfo.class).name().toLowerCase().substring(0,1);
        ArrayList<Class<? extends JProgram>> list = nameMap.getOrDefault(title, new ArrayList<Class<? extends JProgram>>());
        list.add(a);
        nameMap.put(title, list);
    }

    private static final long serialVersionUID = 1L;
    protected static StartMenu i;
    public static boolean isOpen = false;
    private final File root = new File(System.getProperty("user.home") + File.separator + "desktop");
    private final JPanel p, tiles, t;
    private final JMenuBar ba;
    private List<JPanel> lets;

    public static void start() {
        if (isOpen) {
            try {
                i.setClosed(true);
                i.fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSING);
                Main.p.remove(i);
            } catch (PropertyVetoException e) { e.printStackTrace(); }
            return;
        }

        if (null == i) new StartMenu();

        Main.p.add(i);
        Main.p.moveToFront(i);
        isOpen = true;

        i.effect();
    }

    public static void stop() {
        if (null == i || !isOpen) return;

        isOpen = false;
        i.dispose();
    }

    public void effect() {
        int h = Main.p.getHeight() - SystemBar.get.getHeight();
        new Thread(() -> {
            boolean b = false;
            for (int i = h; i >= (h - getHeight()); i--) {
                setLocation(0, i--);
                setSize(this.getPreferredSize());
                try {
                    if ((b == !b) && !Main.isLowMemory) Thread.sleep(1);
                } catch (InterruptedException e1) { e1.printStackTrace(); }
                validate();
            }
        }).start();

        setSize(this.getPreferredSize());
        validate();
    }

    public static Color background = Color.BLACK;
    public static Color pBackground = new Color(15, 15, 15, 250);
    public static Color tilesBackground = new Color(0, 0, 0, 200);
    public static Color letBackground = new Color(25, 25, 25, 250);
    public static Color borderBackground = new Color(20, 20, 20);

    public static void setColors(Color bg, Color pBg, Color tiles, Color lets, Color border) {
        background = bg;
        pBackground = pBg;
        tilesBackground = tiles;
        borderBackground = border;
        if (null != i)
            i.refreshColors();
    }

    public void refreshColors() {
        p.setBackground(Main.isLowMemory ? new Color(15, 15, 15) : pBackground);
        t.setBackground(borderBackground);
        setBackground(background);
        tiles.setBackground(Main.isLowMemory ? Color.DARK_GRAY : tilesBackground);
        ba.setBackground(borderBackground);
        for (JPanel let : lets)
            let.setBackground(letBackground);
    }

    public StartMenu() {
        super("Menu", false, false, false);
        this.setUndecorated(true);
        StartMenu.i = this;

        lets = new ArrayList<>();
        p = new JPanel();
        p.setBackground(Main.isLowMemory ? new Color(15, 15, 15) : pBackground);
        p.setLayout(new BorderLayout());
        t = new JPanel();
        t.setBackground(borderBackground);
        t.setBorder(BorderFactory.createEmptyBorder(25,0,0,0));
        p.add(t, BorderLayout.NORTH);

        setBackground(background);

        ba = new JMenuBar();

        addProgram(CopperBrowser.class);
        addProgram(FileExplorer.class);
        addProgram(ConsoleOutput.class);
        addProgram(PersonalizationSettings.class);
        addProgram(TabbedTerminal.class);
        addProgram(WebBrowser.class);
        addProgram(Notepad.class);
        addProgram(Calculator.class);
        addProgram(Mines.class);
        addProgram(WebViewBrowser.class);
        addProgram(ChromeBrowser.class);

        this.setOpaque(false);
        tiles = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component add(Component c) {
                JPanel p = new JPanel();
                p.setOpaque(false);
                p.add(c);
                super.add(p);
                return c;
            }
        };
        tiles.setMinimumSize(new Dimension(100,450));
        tiles.setLayout(new GridLayout(0,3));
        tiles.setBackground(Main.isLowMemory ? Color.DARK_GRAY : tilesBackground);
        tiles.setOpaque(false);

        File mainDir = root;
        while (mainDir.getParent() != null)
            mainDir = mainDir.getParentFile();
        File mainDir_final = mainDir;

        ((Tile)tiles.add(new Tile("pc", "My PC"))).onClick(l -> Main.p.add(new FileExplorer(mainDir_final)));
        ((Tile)tiles.add(new Tile("folder", "File Explorer"))).onClick(l -> Main.p.add(new FileExplorer(mainDir_final)));
        ((Tile)tiles.add(new Tile("calc", "Calculator"))).onClick(l -> Main.p.add(new Calculator(), 245, 330));
        ((Tile)tiles.add(new Tile("web", "Web browser"))).onClick(l -> Main.p.add(new WebBrowser(), 1200, 800));

        JMenu me = new JMenu(System.getProperty("user.name"));
        me.setForeground(Color.white);
        ba.add(me).addMouseListener(Utils.click(e -> Main.p.add(new FileExplorer(new File(System.getProperty("user.home"))))));
        me.setIcon(new ImageIcon(IconPack.get().user.getScaledInstance(16, 16, 0)));
        JSeparator sep = (JSeparator) ba.add(new JSeparator(SwingConstants.VERTICAL));
        sep.setBackground(new Color(0,0,0,0));
        sep.setForeground(new Color(0,0,0,0));

        JMenu about = new JMenu("  About  ");
        about.setForeground(Color.white);
        ba.add(about).addMouseListener(Utils.click(e -> Main.about()));

        JMenu exit = new JMenu("  Exit  ");
        exit.setForeground(Color.white);
        ba.add(exit).addMouseListener(Utils.click(e -> System.exit(0)));

        ba.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        JPanel sort = new JPanel();
        sort.setLayout(new BoxLayout(sort, BoxLayout.Y_AXIS));
        sort.setOpaque(false);
        for (String s : nameMap.keySet()) {
            JPanel let = new JPanel();
            let.setBackground(letBackground);
            let.setLayout(new GridLayout(0, 1));
            let.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(4,2,16,2), s.toUpperCase()));
            ((TitledBorder)let.getBorder()).setTitleColor(Color.WHITE);
            for (Class<? extends JProgram> str : nameMap.get(s)) {
                String name = str.getAnnotation(ProgramInfo.class).name();
                JLabel l = (JLabel)let.add(new JLabel(name));
                try {
                    ImageIcon icon = IconPack.getIcon("res/icons/menu/" + name.replace(" ", "-") + ".png", false);
                    if (icon == null)
                        icon = IconPack.get().blank;
                    l.setIcon(IconPack.scale(icon, 20, 20));
                } catch (IOException e2) {
                    l.setIcon(IconPack.scale(IconPack.get().blank, 20, 20));
                    e2.printStackTrace();
                }
                l.setForeground(new Color(225,225,225));
                l.setBorder(new EmptyBorder(4,0,4,0));
                l.addMouseListener(Utils.click(m -> {
                    try {
                        ProgramInfo i = str.getAnnotation(ProgramInfo.class);
                        Main.p.add(str.newInstance(), i.width(), i.height());
                    } catch (InstantiationException | IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                }));
            }
            lets.add(let);
            sort.add(let);
        }
        JScrollPane sp = new ModernScrollPane(sort, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setMaximumSize(new Dimension(228,500));

        JPanel b = new JPanel();
        b.setOpaque(false);
        b.setLayout(new BoxLayout(b, BoxLayout.X_AXIS));
        b.add(sp);
        JPanel a = new JPanel();
        a.add(tiles);
        a.setOpaque(false);
        b.add(a);
        tiles.setMaximumSize(new Dimension(512, 900));
        p.add(b, BorderLayout.CENTER);
        p.add(ba, BorderLayout.SOUTH);
        ba.setBackground(borderBackground);
        setContentPane(p);

        addInternalFrameListener(new InternalFrameAdapter(){public void internalFrameClosing(InternalFrameEvent e) {stop();}});

        putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        setDisplayInSystemBar(false);
        setVisible(true);
        pack();
        this.setPreferredSize(new Dimension(589, 600));
        this.setBorder(null);
        sp.repaint();
    }

    public static boolean test = true;

    class Tile extends JComponent {

        private static final long serialVersionUID = 1L;
        private Image i;
        private String display;

        public Tile(String txt, String name) {
            this.display = name;
            try {
                setIcon(txt + ".png");
            } catch (IOException e) { e.printStackTrace(); }
            this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getWidth());
            g.drawImage(i, (this.getWidth() - 32) / 2, (this.getHeight() - 32) / 2, 32, 32, null);
            g.setColor(Color.WHITE);
            g.drawString(display, 4, getHeight() - 5);
        }

        public void onClick(IClick c) {
            this.addMouseListener(Utils.click(c));
        }

        public void setIcon(String name) throws IOException {
            this.i = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("res/icons/" + name));
        }

        @Override
        public Dimension getPreferredSize() {
            int s = 96;
            return new Dimension(s,s);
        }

    }

    public static void setBackgroundColor(Color c) {
        i.p.setBackground(c);
        i.tiles.setBackground(c);
    }

}