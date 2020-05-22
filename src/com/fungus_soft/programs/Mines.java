package com.fungus_soft.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.fungus_soft.ui.ModernButton;

import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;
import com.fungus_soft.desktop.api.Toast;

@ProgramInfo(name = "Mines", version="1.0", authors="Fungus Software", width=350,height=475)
public class Mines extends JProgram {

    private static final long serialVersionUID = 1L;

    private Random r;
    private int mineCount;
    private HashMap<Point, JButton> map;
    private Timer t;
    private JPanel p;

    public Mines() {
        r = new Random();

        p = new JPanel(new BorderLayout());
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controls.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        ((JButton)controls.add(new ModernButton("New Game"))).addActionListener(l -> newGame());

        p.add(controls, BorderLayout.NORTH);
        p.add(layoutGame(), BorderLayout.CENTER);
        this.setContentPane(p);
        this.validate();
    }

    public void newGame() {
        p.remove(1);
        p.add(layoutGame(), BorderLayout.CENTER);
        this.validate();
    }

    public JPanel layoutGame() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(8,8));
        mineCount = 0;
        if (t != null) {
            t.cancel();
            t.purge();
        }
        t = new Timer();

        map = new HashMap<>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                boolean mine = r.nextInt(6 + (mineCount*2)) == 0 && mineCount < 8;
                JButton btn = (JButton) p.add(new JButton());
                btn.setFont(btn.getFont().deriveFont(12f));
                if (mine) {
                    btn.setName("*");
                    mineCount++;
                } else btn.setName("safe");

                btn.setBackground(Color.YELLOW);
                map.put(new Point(x,y), btn);
                int xF = x;
                int yF = y;
                btn.addActionListener(a -> {
                    if (btn.getName().equalsIgnoreCase("*")) {
                        for (Component c : p.getComponents()) {
                            if (c.getName().equalsIgnoreCase("*")) {
                                c.setBackground(Color.RED);
                                ((JButton)c).setText("*");
                            }
                        }
                        Toast.show("Mines: Game Over!", 2500);
                        btn.setEnabled(false);
                    }
                    if (btn.getName().equalsIgnoreCase("safe")) {
                        ArrayList<JButton> btns = new ArrayList<>();
                        btns.add(map.getOrDefault(new Point(xF-1, yF-1), new JButton()));
                        btns.add(map.getOrDefault(new Point(xF, yF-1), new JButton()));
                        btns.add(map.getOrDefault(new Point(xF+1, yF-1), new JButton()));
                        btns.add(map.getOrDefault(new Point(xF-1, yF), new JButton()));
                        btns.add(map.getOrDefault(new Point(xF+1, yF), new JButton()));
                        btns.add(map.getOrDefault(new Point(xF-1, yF+1), new JButton()));
                        btns.add(map.getOrDefault(new Point(xF, yF+1), new JButton()));
                        btns.add(map.getOrDefault(new Point(xF+1, yF+1), new JButton()));
                        int mines = 0;
                        for (JButton b : btns)
                            if (b.getName() != null && b.getName().equalsIgnoreCase("*"))
                                mines++;

                        if (mines == 0 && !a.getActionCommand().equalsIgnoreCase("autopress")) {
                            for (JButton b : btns) {
                                if (b.getName() != null && b.getName().equalsIgnoreCase("safe") && b.getText().length() < 1) {
                                    t.schedule(new TimerTask() {

                                        @Override public void run() {
                                            if (!b.getName().contains("disable"))
                                                b.getActionListeners()[0].actionPerformed(new ActionEvent(b, a.getID(), "authopress"));
                                            b.setName("disable");
                                            b.setEnabled(false);
                                        }
                                        
                                    }, 40);
                                }
                            }
                        }
                        if (mines > 0) btn.setText("" + mines);
                        btn.setBackground(new Color(240,240,240));
                        btn.setEnabled(false);
                    }
                    int yellow = 0;
                    for (Component c : p.getComponents())
                        if (c.getBackground() == Color.YELLOW)
                            yellow++;

                    if (yellow == mineCount)
                        Toast.show("Congrads! You won!", 2500);
                });
            }
        }
        return p;
    }

}