package com.fungus_soft.desktop.theme;

import java.awt.Color;
import java.awt.Dimension;

import jthemes.ThemeUtils;
import jthemes.themes.AeroTheme;
import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.StartMenu;
import com.fungus_soft.desktop.SystemBar;

public class WindowsTheme extends Theme {

    @Override
    public void apply() {
        ThemeUtils.setCurrentTheme(new AeroTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setSized(new Dimension(Main.p.getWidth(), 48));
        SystemBar.setPanelBackground(new Color(143, 179, 218, 248));
        SystemBar.setButtonBackground(new Color(160, 190, 210));
        StartMenu.setColors(Color.BLACK, new Color(110,156,226,250), new Color(120,160,190, 220), new Color(120,168,208, 250), new Color(160, 190, 242));
    }

}