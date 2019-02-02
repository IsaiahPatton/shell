package me.isaiah.shell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import me.isaiah.shell.api.JProgram;

public class ZDesktopPane extends JDesktopPane {

    private static final long serialVersionUID = 1L;
    private Image img = null;
    private JFrame f;
    public final JPanel open = new JPanel();

    public List<JInternalFrame> list = new ArrayList<>();

    public ZDesktopPane(JFrame parent) {
        super();
        this.f = parent;
        this.addMouseListener(MouseClick.click(e -> StartMenu.stop()));
        open.setOpaque(false);
    }

    public void setLAF(String className) {
        Multithreading.run(() -> {
            try {
                UIManager.setLookAndFeel(className);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e1) {
                e1.printStackTrace();
            }
            SwingUtilities.updateComponentTreeUI(Main.f);
            Main.p.removeAll();

            Desktop.reset();
            Desktop.init();
        });
    }

    public void setBackground(Image img) {
        this.img = img.getScaledInstance(f.getWidth(), f.getHeight(), 0);

        // "Refresh" Screen
        JInternalFrame b = new JInternalFrame();
        b.setName("DESKTOP_ICON");
        b.setLocation(0, 0);
        b.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        b.setVisible(true);
        b.setSize(getWidth(), getHeight());
        b.validate();
        add(b);
        moveToBack(b);
        remove(b);
    }

    public void add(JProgram j, int width, int height) {
        j.setIconifiable(true);
        j.setSize(width, height);
        add(j);
    }

    @Override
    public void addImpl(Component j, Object constraints, int index) {
        StartMenu.stop();

        j.setVisible(true);
        super.addImpl(j, constraints, index);
        moveToFront(j);

        if ((j instanceof JInternalFrame || j instanceof JProgram) &&
                !(j instanceof StartMenu || j instanceof Notification)) {

            JInternalFrame jp = (JInternalFrame) j;
            if ((jp.getName() == null || !jp.getName().equalsIgnoreCase("DESKTOP_ICON"))
                    && !jp.isIcon() && !list.contains(jp)) {

                ImageIcon i = new ImageIcon();
                if (jp.getFrameIcon() != null)
                    i.setImage( ((ImageIcon) jp.getFrameIcon()).getImage().getScaledInstance(24, 24, 0) );

                JButton l = new JButton(i);
                l.setBackground(Color.GRAY);
                l.setOpaque(true);
                l.setBorder(BorderFactory.createEmptyBorder(7, 5, 7, 5));
                l.setText(jp.getTitle());

                open.add(l);
                jp.addInternalFrameListener(new InternalFrameAdapter(){
                    public void internalFrameClosing(InternalFrameEvent e) { 
                        l.setIcon(null);
                        l.setText(null);
                        l.setVisible(false);
                        Timer ti = new Timer(10, a -> open.remove(l));
                        ti.setRepeats(false);
                        ti.start();
                    }
                    public void internalFrameIconified(InternalFrameEvent e) { 
                        jp.getDesktopIcon().setVisible(false);
                        jp.setVisible(false);
                        list.add(jp);
                    }
                    public void internalFrameDeiconified(InternalFrameEvent e) {
                        list.remove(jp);
                        Timer ti = new Timer(1000, a -> open.validate());
                        ti.setRepeats(false);
                        ti.start();
                    }
                });
                l.addMouseListener(MouseClick.click(e -> {
                    try {
                        jp.setIcon(false);
                    } catch (PropertyVetoException e1) { e1.printStackTrace(); }
                    jp.setVisible(true);
                    jp.toFront();
                }));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (null != img)
            g.drawImage(img, 0, 0, null);

        g.setColor(Color.LIGHT_GRAY);
        g.drawString("ZDE v" + Main.VERSION, getWidth() - 100, 20);
    }

}