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

        ThemeUtils.setCurrentTheme(new LiteTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setSized(new Dimension(Main.p.getWidth(), 48));
        SystemBar.setPanelBackground(new Color(200,200,200,230));
        SystemBar.setButtonBackground(WHITE);
        StartMenu.setColors(Color.BLACK, new Color(190,190,190,250), new Color(200,200,200, 222), new Color(170,170,170, 250), new Color(210,210,210));
        Toast.BACKGROUND = Color.WHITE;
        Toast.FOREGROUND = Color.BLACK;
    }

}