package com.fungus_soft.desktop.settings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;
import com.fungus_soft.desktop.theme.IconPack;
import com.fungus_soft.ui.ModernButton;
import com.fungus_soft.ui.ModernScrollPane;

import jthemes.WindowPane;

@ProgramInfo(name = "Settings", version="1.0", authors="Fungus Software", width=600, height=400)
public class SettingsApp extends JProgram {

    private static final long serialVersionUID = 1L;
    private Class<?>[] panels;

    public SettingsApp() {
        this.panels = new Class<?>[] {PersonalizationPanel.class, NetworkPanel.class, AboutPanel.class};
        JPanel all = new JPanel(new BorderLayout());
        JPanel p = new JPanel();
        WindowPane wp = this.getTitleBar();
        JButton back = new ModernButton("<- Back to All Settings");
        back.setVisible(false);
        back.addActionListener(l -> {
            all.getComponents()[0].setName("close");
            all.removeAll();
            all.add(p, BorderLayout.CENTER);
            back.setVisible(false);
            validate();
        });

        JPanel bp = new JPanel();
        bp.setOpaque(false);
        bp.add(back);

        wp.add(bp, BorderLayout.CENTER);
        wp.add(Box.createHorizontalStrut(16), BorderLayout.WEST);

        for (Class<?> pan : panels) {
            ModernButton btn = new ModernButton(pan.getAnnotation(ProgramInfo.class).name());
            btn.addActionListener(l -> {
                all.removeAll();
                try {
                    all.add((Component) pan.newInstance(), BorderLayout.CENTER);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                back.setVisible(true);
                validate();
            });
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            try {
                ImageIcon icon = IconPack.getIcon("res/icons/menu/" + pan.getName() + ".png", false);
                if (icon == null)
                    icon = IconPack.get().blank;
                btn.setIcon(icon);
            } catch (IOException e2) {
                btn.setIcon(IconPack.get().blank);
                e2.printStackTrace();
            }
            p.add(btn);
        }
        all.add(p, BorderLayout.CENTER);
        this.setContentPane(new ModernScrollPane(all));
    }

}