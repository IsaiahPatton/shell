package me.isaiah.shell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;
import me.isaiah.shell.programs.Browser;
import me.isaiah.shell.programs.Calc;
import me.isaiah.shell.programs.Console;
import me.isaiah.shell.programs.MineSweeper;
import me.isaiah.shell.programs.NotePad;
import me.isaiah.shell.programs.ProgramManager;
import me.isaiah.shell.programs.ZunoZapSwing;
import me.isaiah.shell.theme.IconPack;

@ProgramInfo(name = "Menu")
public class StartMenu extends JProgram {

    private static final long serialVersionUID = 1L;
    protected static StartMenu i;
    public static boolean isOpen = false;
    protected static final JMenu programs = new JMenu("Programs");
    private final File root = new File(System.getProperty("user.home") + File.separator + "desktop");

    public static void start() {
        if (isOpen) {
            try {
                i.setClosed(true);
                i.fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSING );
            } catch (PropertyVetoException e) { e.printStackTrace(); }
            return;
        }

        if (null == i) new StartMenu();

        Main.p.add(i, null, 0);
        isOpen = true;

        i.effect();
    }

    public static void stop() {
        if (null == i || !isOpen) return;

        isOpen = false;
        i.dispose();
    }

    public void effect() {
        new Thread(() -> {
            for (int i = Main.taskbar.getY(); i > (Main.taskbar.getY() - getHeight()); i--) {
                setLocation(0, i--);
                setSize(this.getPreferredSize());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e1) { e1.printStackTrace(); }
                validate();
            }
        }).start();

        setLocation(0, Main.taskbar.getY() - getHeight());
        setSize(this.getPreferredSize());
        validate();
    }

    public StartMenu() {
        super("Menu", false, false, false);
        StartMenu.i = this;
        Font f = new JLabel().getFont();
        JPanel p = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                g.setColor(Color.LIGHT_GRAY.brighter());
                g.setFont(f);
                g.drawString("Welcome " + System.getProperty("user.name"), 58, 48);
                g.drawImage(IconPack.get().user, 8, 13, null);
                super.paint(g);
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        BasicInternalFrameUI ui = ((BasicInternalFrameUI)getUI());
        ui.setNorthPane(null);
        Color black = new Color(0,0,0);
        p.setOpaque(false);
        setBackground(black);

        JLabel usr = new JLabel();
        usr.setBorder(BorderFactory.createEmptyBorder(50, 8, 30, 8));
        p.add(usr);

        JMenuBar ba = new JMenuBar();

        programs.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        ZDesktopPane z = Main.p;
        programs.setMenuLocation(programs.getLocation().x + 80, programs.getLocation().y);
        programs.add("Web Browser").addActionListener(l -> Browser.run());
        programs.add("Web Browser Lite (recommended)").addActionListener(l -> z.add(new ZunoZapSwing(), 600, 400));
        programs.add("File Explorer").addActionListener(l -> Main.newFileExplorer(root));
        programs.add("NotePad").addActionListener(l -> z.add(new NotePad(null), 500, 400));
        programs.add("Termanal").addActionListener(l -> z.add(new Console(), 850, 500));
        programs.add("Calcalator").addActionListener(l -> z.add(new Calc(), 200, 200));
        programs.add("Minesweeper").addActionListener(l -> z.add(new MineSweeper(), 250, 350));

        JMenu sys = new JMenu("System");
        sys.setMenuLocation(sys.getLocation().x + 80, sys.getLocation().y);
        sys.add("Program Manager").addActionListener(l -> z.add(new ProgramManager(), 500, 500));
        sys.add("Admin Termanal").addActionListener(l -> z.add(new DebugConsole(), 850, 500));
        sys.add("Task Manager").addActionListener(l -> Main.taskManager());

        JPanel tiles = new JPanel();
        tiles.setMinimumSize(new Dimension(100,400));

        ((Tile)tiles.add(new Tile("calc"))).onClick(l -> z.add(new Calc(), 200, 200));
        ((Tile)tiles.add(new Tile("tilefold"))).onClick(l -> Main.newFileExplorer(root));
        ((Tile)tiles.add(new Tile("web"))).onClick(l -> z.add(new ZunoZapSwing(), 600, 400));

        ba.add(sys);
        ba.add(programs);

        ba.add(Box.createVerticalStrut(5));
        ba.add(new JMenu(" About ")).addMouseListener(MouseClick.click(e -> Main.about()));
        ba.add(new JMenu(" Exit ")).addMouseListener(MouseClick.click(e -> System.exit(0)));

        ba.setLayout(new GridLayout(0,1));

        JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.X_AXIS));
        b.add(ba);
        b.add(Box.createHorizontalStrut(4));
        b.add(tiles);
        p.add(b);
        ba.setBackground(Color.LIGHT_GRAY);
        setContentPane(p);

        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) { stop(); }
        });

        putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        setVisible(true);
        pack();
        Dimension size = this.getPreferredSize();
        this.setPreferredSize(new Dimension(size.width + 50, size.height + 100));

        Main.p.add(this);
        Main.p.moveToFront(this);
    }

    class Tile extends JComponent {

        private static final long serialVersionUID = 1L;
        private Image i;

        public Tile(String txt) {
            try {
                setIcon(txt + ".png");
            } catch (IOException e) { e.printStackTrace(); }
        }

        @Override
        public void paint(Graphics g) {
            g.drawImage(i, 0, 0, getWidth(), getWidth(), null);
        }

        public void onClick(IClick c) {
            this.addMouseListener(MouseClick.click(c));
        }

        public void setIcon(String name) throws IOException {
            this.i = ImageIO.read(Icon.class.getClassLoader().getResourceAsStream("tiles/" + name));
        }

        @Override
        public Dimension getPreferredSize() {
            int s = i.getWidth(null);
            return new Dimension(s,s);
        }

    }

}