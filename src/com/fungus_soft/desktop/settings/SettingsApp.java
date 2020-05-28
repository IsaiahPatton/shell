package com.fungus_soft.desktop.settings;

import javax.swing.JPanel;

import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;
import com.fungus_soft.programs.PersonalizationSettings;
import com.fungus_soft.ui.ModernButton;

@ProgramInfo(name = "Settings", version="1.0", authors="Fungus Software", width=600, height=400)
public class SettingsApp extends JProgram {

    private static final long serialVersionUID = 1L;
    private JPanel[] panels;

    public SettingsApp() {
        this.panels = new JPanel[] {new PersonalizationPanel()};
        JPanel p = new JPanel();

        for (JPanel pan : panels) {
            ModernButton btn = new ModernButton(pan.getClass().getAnnotation(ProgramInfo.class).name());
            btn.addActionListener(l -> {
                setContentPane(pan);
                validate();
            });
            p.add(btn);
        }
        this.setContentPane(p);
    }

}