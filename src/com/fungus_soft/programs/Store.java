package com.fungus_soft.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.codebrig.journey.JourneyBrowserView;
import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.Utils;
import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramDownloader;
import com.fungus_soft.desktop.api.ProgramInfo;
import com.fungus_soft.desktop.api.ProgramDownloader.DownloadAction;
import com.fungus_soft.desktop.theme.IconPack;
import com.fungus_soft.programs.zunozap.ZunoZapFx;
import com.fungus_soft.ui.ModernButton;
import com.fungus_soft.ui.ModernScrollPane;

import jthemes.WindowPane;

@ProgramInfo(name = "Store (Beta)", width=900, height=550)
public class Store extends JProgram {

    private static final long serialVersionUID = 1L;
    private JLabel title;

    public enum Catagory {
        GENERAL, OFFICE, GAMES, INTERNET
    }

    public HashMap<String, ProgramListing> nameToId;
    private ImageIcon BLANK = null;

    public Store() {
        this.nameToId = new HashMap<>();
        try {
            // TODO Use data base instead of hardcoding
            addListing(new ProgramListing("ZunoZap Browser", "Fungus Software", "A Chrome powered web browser", new URL("https://avatars1.githubusercontent.com/u/20327341"),
                    Catagory.INTERNET, "https://zunozap.fungus-soft.com/zunozap/builds/ZunoZap-0.9.jar"));
            addListing(new ProgramListing("The Incredible Game", "Fungus Software", "A 2D Block Game", new URL("https://fungus-soft.com/games/incredible-game/title.png"),
                    Catagory.GAMES, "https://github.com/IsaiahPatton/blockgame/releases/download/1.0/blockgame-indev.jar"));
            addListing(new ProgramListing("File Manager", "javadev on Github", "A java/swing basic File Manager", null,
                    Catagory.GENERAL, "https://github.com/javadev/file-manager/raw/master/filemanager.jar"));
            addListing(new ProgramListing("DrJava", "drjava", "An Java IDE", new URL("https://a.fsdn.com/allura/p/drjava/icon?1513717491"),
                    Catagory.GENERAL, "https://downloads.sourceforge.net/project/drjava/1.%20DrJava%20Stable%20Releases/drjava-beta-20190813-220051/drjava-beta-20190813-220051.jar"));
            addListing(new ProgramListing("Lobo Web Browser", "lobo", "An open-source Java based browser", new URL("https://avatars3.githubusercontent.com/u/6113075"),
                    Catagory.INTERNET, "https://github.com/LoboEvolution/LoboEvolution/releases/download/1.0/LoboEvo.jar"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel all = new JPanel(new BorderLayout());
        JPanel p = new JPanel();

        WindowPane wp = this.getTitleBar();
        JButton back = new ModernButton("<");
        super.setTitle("");
        title = new JLabel("Store (Beta)");

        back.addActionListener(l -> {
            all.getComponents()[0].setName("close");
            all.removeAll();
            all.add(p, BorderLayout.CENTER);
            validate();
            repaint();
        });

        JPanel bp = new JPanel();
        bp.setOpaque(false);
        bp.add(back);
        bp.add(title);
        title.setForeground(Color.LIGHT_GRAY);
        back.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), new EmptyBorder(3,8,3,8)));
        back.setBackground(new Color(50,50,50));
        back.setForeground(Color.LIGHT_GRAY);

        wp.add(bp, BorderLayout.WEST);

        try {
            BLANK = new ImageIcon(IconPack.get("res/icons/blankfile.png", false).getScaledInstance(48, 48, 0));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        for (ProgramListing pl : nameToId.values()) {
            ModernButton btn = new ModernButton(pl.name) {
                int i = 0;
                @Override
                public void paint(Graphics g) {
                    super.paint(g);
                    if (Main.isInstalled(pl.name)) {
                        g.setFont(g.getFont().deriveFont(10f));
                        drawRotate((Graphics2D)g, 8, 36, 320, "Installed");
                        repaint();
                    }
                }
            };
            btn.addActionListener(l -> {
                all.removeAll();
                all.add(new ListingPanel(pl), BorderLayout.CENTER);
                validate();
            });
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            Utils.runAsync(() -> {
                ImageIcon icon = null;
                try {
                    btn.setIcon(BLANK);
                    while (!pl.iconReady) Thread.sleep(10);
                    icon = null == pl.icon ? new ImageIcon(IconPack.get("res/icons/blankfile.png", false).getScaledInstance(48,48,0)) : new ImageIcon(pl.icon);
                    btn.setIcon(icon);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            if (Main.isInstalled(pl.name))
                btn.setBackground(new Color(220,220,220));
            btn.setMinimumSize(new Dimension(148,108));
            btn.setMaximumSize(btn.getMinimumSize());
            btn.setPreferredSize(btn.getMinimumSize());
            p.add(btn);
        }
        all.add(p, BorderLayout.CENTER);
        this.setContentPane(new ModernScrollPane(all));
    }

    public static void drawRotate(Graphics2D g2d, double x, double y, int angle, String text) {    
        g2d.translate((float)x,(float)y);
        g2d.rotate(Math.toRadians(angle));
        g2d.drawString(text,0,0);
        g2d.rotate(-Math.toRadians(angle));
        g2d.translate(-(float)x,-(float)y);
    }    

    public void addListing(ProgramListing c) {
        nameToId.put(c.name, c);
    }

    public class ProgramListing {
        public String name;
        public String author;
        public String desc;
        public Image icon;
        public Catagory cat;
        public String jarLocation;
        public boolean iconReady;

        public ProgramListing(String name, String author, String desc, Object icon, Catagory cat, String jarLocation) {
            this.name = name;
            this.author = author;
            this.desc = desc;
            this.iconReady = false;
            Utils.runAsync(() -> {
                try {
                    this.icon = icon == null ? null : ImageIO.read((URL)icon);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.icon = icon == null ? null : this.icon.getScaledInstance(48, 48, 0);
                this.iconReady = true;
            });
            this.cat = cat;
            this.jarLocation = jarLocation;
        }
    }

    @ProgramInfo()
    public class ListingPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private boolean finished;

        public ListingPanel(ProgramListing pl) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JPanel a = new JPanel();
            if (null != pl.icon) a.add(new JLabel(new ImageIcon(pl.icon)));
            JLabel name = (JLabel)a.add(new JLabel(pl.name));
            name.setFont(name.getFont().deriveFont(32f));
            a.setMaximumSize(new Dimension(Integer.MAX_VALUE, 138));
            this.add(a);
            JPanel desp = new JPanel();
            JTextArea des = new JTextArea(pl.desc);
            des.setEditable(false);
            des.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true),new EmptyBorder(8,26,8,26)));
            des.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            desp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 101));
            desp.add(des);
            this.add(desp);

            JButton run = new ModernButton();
            if (Main.isInstalled(pl.name)) {
                run.setText("Launch");
                run.addActionListener(l -> {
                    if (pl.name.equalsIgnoreCase("ZunoZap Browser")) {
                        ProgramInfo i = ZunoZapFx.class.getAnnotation(ProgramInfo.class);
                        Main.p.add(new ZunoZapFx(), i.width(), i.height());
                        return;
                    }
                    File f = new File(new File(Main.pStorage.getParentFile(), "downloadedPrograms"), pl.jarLocation.substring(pl.jarLocation.lastIndexOf("/")+1));
                    f.getParentFile().mkdir();
                    Main.pm.loadProgramNon(f, true, true);
                });
            } else {
                run.setText("Install");
                run.addActionListener(l -> {
                    run.setEnabled(false);

                    File f = new File(new File(Main.pStorage.getParentFile(), "downloadedPrograms"), pl.jarLocation.substring(pl.jarLocation.lastIndexOf("/")+1));
                    f.getParentFile().mkdir();

                    ProgramDownloader dl = new ProgramDownloader(pl.jarLocation, f.getParentFile());
                    DownloadAction act = () -> {
                        if (dl.finished) {
                            try {
                                finished = true;
                                Main.pr.add(pl.name);
                                Main.pm.loadProgramNon(f, true, true);
                            } catch (Exception e) {e.printStackTrace();}
                        }
                    };

                    if (f.exists()) {
                        dl.finished = true;
                        act.onFinished();
                    } else {
                        try {
                            System.out.println("test");
                            dl.start(act);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            JPanel p = new JPanel();
            p.add(run);
            this.add(p);
        }
    }

    @Override
    public void setTitle(String title) {
        super.setTitle("");
        if (this.title != null) this.title.setText(title);
    }

}