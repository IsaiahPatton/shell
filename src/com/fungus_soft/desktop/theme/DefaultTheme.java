package com.fungus_soft.desktop.theme;

import java.awt.Color;
import java.awt.Dimension;

import jthemes.ThemeUtils;
import jthemes.themes.ModernTheme;
import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.StartMenu;
import com.fungus_soft.desktop.SystemBar;
import com.fungus_soft.desktop.api.Toast;

public class DefaultTheme extends Theme {

    @Override
    public void apply() {
        ThemeUtils.setCurrentTheme(new ModernTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setSized(new Dimension(Main.p.getWidth(), 48));
        SystemBar.setPanelBackground(new Color(0,0,0,250));
        SystemBar.setButtonBackground(Color.BLACK);
        StartMenu.setColors(Color.BLACK, new Color(15,15,15,250), new Color(40,40,40), new Color(24,24,24, 250), new Color(20,20,20));
        Toast.BACKGROUND = Color.BLACK;
        Toast.FOREGROUND = Color.WHITE;
    }

}