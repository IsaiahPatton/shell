package com.fungus_soft.desktop.theme;

import java.awt.Color;
import java.awt.Dimension;

import jthemes.ThemeUtils;
import jthemes.themes.DarkTheme;
import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.StartMenu;
import com.fungus_soft.desktop.SystemBar;
import com.fungus_soft.desktop.api.Toast;

public class BlackTheme extends Theme {

    @Override
    public void apply() {
        ThemeUtils.setCurrentTheme(new DarkTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setSized(new Dimension(Main.p.getWidth(), 48));
        SystemBar.setPanelBackground(Color.BLACK);
        SystemBar.setButtonBackground(Color.BLACK);
        StartMenu.setColors(Color.BLACK, Color.BLACK, new Color(8,8,8), Color.BLACK, Color.BLACK);
        Toast.BACKGROUND = Color.BLACK;
        Toast.FOREGROUND = Color.LIGHT_GRAY;
    }

}