package com.fungus_soft.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.fungus_soft.desktop.Main;

import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;
import me.isaiah.shell.bartray.TaskBarTray;

@ProgramInfo(name = "SystemBar", version = "1")
public class SystemBar extends JProgram {

    private static final long serialVersionUID = 1L;
    public static SystemBar get;
    
    private final JPanel p;
    private final JButton menu;
    public WindowBar wb;

    public static Color barColor;
    public static Color btnBg;
    public static Dimension size;

    public SystemBar() {
        SystemBar.get = this;
        wb = new WindowBar();
        this.setUndecorated(true);
        this.setOpaque(false);
        this.setDisplayInSystemBar(false);
        if (size == null)
            size = new Dimension(Main.p.getWidth(), getPreferredSize().height + 10);

        menu = new JButton() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paint(g);
                int x = (menu.getWidth()/2);
                int y = (menu.getHeight()/2) - 4;
                if (StartMenu.isOpen) {
                    g.drawLine(x - 8, y, x + 8, y + 12);
                    g.drawLine(x - 8, y + 12, x + 8, y);
                } else {
                    for (int i = 0; i < 12; i+=4)
                        g.drawLine(x - 8, y+i, x + 8, y + i);
                }
            }
        };
        menu.setPreferredSize(new Dimension(64, size.height));
        menu.setBackground(btnBg);
        menu.setForeground(Color.WHITE);

        menu.setBorder(BorderFactory.createEmptyBorder(5, 17, 5, 17));
        menu.addMouseListener(Utils.click(e -> StartMenu.start()));

        p = new JPanel(new BorderLayout());
        p.setMaximumSize(new Dimension(10000, 35));
        setContentPane(p);
        p.add(menu, BorderLayout.WEST);

        p.add(wb, BorderLayout.CENTER);
        p.add(new TaskBarTray(), BorderLayout.EAST);
        p.setBackground(barColor);

        setSize(size != null ? size : (size = new Dimension(Main.p.getWidth(), getPreferredSize().height + 10)));
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

    public static void setSized(Dimension d) {
        size = d;
        if (get != null) {
            get.setSize(d);
            get.setLocation(0, Main.p.getHeight() - get.getHeight());
            get.validate();
        }
    }

    public static void setButtonBackground(Color c) {
        btnBg = c;
        if (get != null)
            get.menu.setBackground(c);
    }

    public static void setPanelBackground(Color c) {
        barColor = c;
        if (get != null)
            get.p.setBackground(c);
    } 

}