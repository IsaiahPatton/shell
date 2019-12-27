package me.isaiah.shell.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import me.isaiah.shell.Main;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name="Notification")
public class Toast extends JProgram {

    private static final long serialVersionUID = 1L;
    private JTextArea cont;
    public static int shown;
    
    public static void show(String tex, int ms) {
        show(tex, ms, 420, 110, null);
    }

    public static void show(String tex, int ms, int width, int height, Font fo) {
        JFrame f = Main.f;
        Toast n = new Toast(tex, ms);
        n.setSize(width,height);
        if (null != fo)
            n.getContent().setFont(fo);
        n.setLocation((f.getWidth() - width) - 5, ((f.getHeight() - height) - 50) - ((Toast.shown - 1) * (3 + height)));
        n.validate();
        Main.p.add(n, width, height);
    }

    public Toast(String content, int ms) {
        super("Notification", false, true, false);
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
        cont.setBackground(Color.BLACK);
        cont.setForeground(Color.WHITE);

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

    public JTextArea getContent() {
        return cont;
    }

}