package me.isaiah.shell;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.programs.ActiveDesktop;
import me.isaiah.shell.programs.Calc;
import me.isaiah.shell.programs.Console;
import me.isaiah.shell.programs.MineSweeper;
import me.isaiah.shell.programs.MiniBrowser;
import me.isaiah.shell.programs.ProgramManager;
import me.isaiah.shell.programs.ZunoZapSwing;

public class StartMenu extends JProgram {

    private static final long serialVersionUID = 1L;
    private static StartMenu i;
    private static boolean isOpen = false;
    protected static final JMenu programs = new JMenu("Programs");

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
        isOpen = false;
        i.dispose();
    }

    public void effect() {
        new Thread(() -> {
            for (int i = Main.taskbar.getY(); i > (Main.taskbar.getY() - getHeight()); i--) {
                setLocation(0, i--);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e1) { e1.printStackTrace(); }
                validate();
            }
        }).start();

        setLocation(0, Main.taskbar.getY() - getHeight());
        validate();
    }

    public StartMenu() {
        super("Start");

        StartMenu.i = this;
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        ((JLabel)p.add(new JLabel("Welcome " + System.getProperty("user.name")))).setBorder(
                BorderFactory.createEmptyBorder(10, 5, 15, 5));

        JMenuBar ba = new JMenuBar();

        programs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ba.add(new JMenu(" Exit ")).addMouseListener(new MouseClick() { @Override public void click(MouseEvent e) {System.exit(0);}});
        ba.add(new JMenu(" About ")).addMouseListener(new MouseClick(){ @Override public void click(MouseEvent e) {Main.about();}});

        final File root = new File(System.getProperty("user.home") + File.separator + "desktop");

        programs.setMenuLocation(programs.getLocation().x + 100, programs.getLocation().y);
        programs.add("Web Browser").addActionListener(l -> Main.startBrowser());
        programs.add("Web Browser Lite").addActionListener(l -> new ZunoZapSwing());
        programs.add("File Explorer").addActionListener(l -> Main.newFileExplorer(root));
        programs.add("NotePad").addActionListener(l -> Main.emptyNotePad());
        programs.add("Termanal").addActionListener(l -> Main.start(new Console(), 850, 500));
        programs.add("Task Manager").addActionListener(l -> Main.taskManager());
        programs.add("JEditorPane Browser").addActionListener(l -> Main.start(new MiniBrowser(), 300, 500));
        programs.add("Calcalator").addActionListener(l -> Main.start(new Calc(), 200, 200));
        programs.add("Minesweeper").addActionListener(l -> Main.start(new MineSweeper(), 250, 350));
        programs.add("Active Desktop Test").addActionListener(l -> new ActiveDesktop()); // Testing

        JMenu sys = new JMenu("System");
        sys.setMenuLocation(sys.getLocation().x + 100, sys.getLocation().y);
        sys.add("Program Manager").addActionListener(l -> Main.start(new ProgramManager(), 500, 500));
        sys.add("DebugConsole").addActionListener(l -> Main.start(new DebugConsole(), 850, 500));

        ba.add(sys);
        ba.add(programs);
        ba.setLayout(new GridLayout(0,1));
        p.add(ba);
        setContentPane(p);

        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) { stop(); }
        });

        putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.setMaximizable(false);
        this.setIconifiable(false);
        setVisible(true);
        pack();

        Main.p.add(this);
        Main.p.moveToFront(this);
    }

}