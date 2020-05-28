package com.fungus_soft.desktop.theme;

import java.awt.Color;
import java.awt.Dimension;

import jthemes.ThemeUtils;
import jthemes.themes.ModernTheme;
import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.StartMenu;
import com.fungus_soft.desktop.SystemBar;
import com.fungus_soft.desktop.api.Toast;

public class DefaultLightTheme extends Theme {

    @Override
    public void apply() {
        Color WHITE = new Color(220,220,220);

        ThemeUtils.setCurrentTheme(new ModernTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setSized(new Dimension(Main.p.getWidth(), 48));
        SystemBar.setPanelBackground(new Color(220,220,220,230));
        SystemBar.setButtonBackground(WHITE);
        StartMenu.setColors(Color.BLACK, new Color(215,215,215,250), new Color(200,200,200, 200), new Color(225,225,225, 250), new Color(220,220,220));
        Toast.BACKGROUND = Color.WHITE;
        Toast.FOREGROUND = Color.BLACK;
    }

}