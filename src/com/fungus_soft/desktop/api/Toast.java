package com.fungus_soft.desktop.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;

@ProgramInfo(name="Notification")
public class Toast extends JProgram {

    private static final long serialVersionUID = 1L;
    private JTextArea cont;
    public static int shown;

    public static Color BACKGROUND = Color.BLACK;
    public static Color FOREGROUND = Color.WHITE;
    
    public static Toast show(String tex, int ms) {
        return show(tex, ms, 420, 110, null);
    }

    public static Toast show(String tex, int ms, int width, int height, Font fo) {
        JFrame f = Main.f;
        Toast n = new Toast(tex, ms);
        n.setSize(width,height);
        if (null != fo)
            n.getContent().setFont(fo);
        n.setLocation((f.getWidth() - width) - 5, ((f.getHeight() - height) - 50) - ((Toast.shown - 1) * (3 + height)) - 18);
        n.validate();
        Main.p.add(n, width, height);
        return n;
    }

    public Toast(String content, int ms) {
        super("Notification", false, true, false);
        this.setUndecorated(true);
        this.setVisible(true);
        this.cont = new JTextArea(content);
        this.cont.setMargin(new Insets(12, 12, 12, 12));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.cont.setEditable(false);
        this.setContentPane(cont);
        this.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        this.setDisplayInSystemBar(false);

        BasicInternalFrameUI ui = ((BasicInternalFrameUI)getUI());
        ui.setNorthPane(null);

        setBackground(Color.BLACK);
        cont.setBackground(BACKGROUND);
        cont.setForeground(FOREGROUND);
        this.setBackground(BACKGROUND);

        this.setLocation(Main.p.getBounds().x, 100 * shown);
        this.validate();

        shown++;
        Timer timer = new Timer(ms, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shown--;
                setVisible(false);
                dispose();
                ((Timer)e.getSource()).stop();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
    }

    @Override
    public boolean isIconifiable() {
        return false;
    }

    public JTextArea getContent() {
        return cont;
    }

}