package me.isaiah.shell;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name="Notification")
public class Notification extends JProgram {

    private static final long serialVersionUID = 1L;
    private JTextArea cont;
    public static int shown;

    public Notification(String content, int ms) {
        super("Notification", false, true, false);
        this.setVisible(true);
        this.cont = new JTextArea(content);
        this.cont.setMargin(new Insets(12, 12, 12, 12));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.cont.setEditable(false);
        this.setContentPane(cont);
        this.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);

        BasicInternalFrameUI ui = ((BasicInternalFrameUI)getUI());
        ui.setNorthPane(null);

        setBackground(Color.BLACK);
        cont.setBackground(Color.BLACK);
        cont.setForeground(Color.WHITE);

        this.setLocation(this.getRes().x, this.getRes().y + (100 * shown));
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

    public Rectangle getRes() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    }

}