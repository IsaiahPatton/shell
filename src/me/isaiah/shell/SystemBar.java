package me.isaiah.shell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;
import me.isaiah.shell.bartray.TaskBarTray;

@ProgramInfo(name = "SystemBar", version = "1")
public class SystemBar extends JProgram {

    private static final long serialVersionUID = 1L;
    public static SystemBar get;
    
    private final JPanel p;
    private final JButton menu;
    public WindowBar wb;

    public SystemBar() {
        SystemBar.get = this;
        wb = new WindowBar();
        this.setUndecorated(true);
        this.setOpaque(false);
        this.setDisplayInSystemBar(false);

        menu = new JButton("Menu");
        menu.setBackground(Color.BLACK);
        menu.setForeground(Color.WHITE);

        menu.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), menu.getBorder()));
        menu.addMouseListener(Utils.click(e -> StartMenu.start()));

        p = new JPanel(new BorderLayout());
        p.setMaximumSize(new Dimension(10000, 35));
        setContentPane(p);
        p.add(menu, BorderLayout.WEST);

        p.add(wb, BorderLayout.CENTER);
        p.add(new TaskBarTray(), BorderLayout.EAST);
        p.setBackground(Color.BLACK);

        setSize(new Dimension(Main.p.getWidth(), getPreferredSize().height + 10));
        this.setBorder(null);
        this.setVisible(true);
        this.setLocation(0, Main.p.getHeight() - this.getHeight());

        Main.f.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setSize(new Dimension(Main.p.getWidth(), getPreferredSize().height + 10));
                setLocation(0, Main.p.getHeight() - getHeight());
            }
        });
    }

    @Override
    public void setSelected(boolean b) {
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    public static void setButtonBackground(Color c) {
        get.menu.setBackground(c);
    }

    public static void setPanelBackground(Color c) {
        get.p.setBackground(c);
    } 

}