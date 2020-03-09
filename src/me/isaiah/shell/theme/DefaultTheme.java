package me.isaiah.shell.theme;

import java.awt.Color;
import java.awt.Dimension;

import jthemes.ThemeUtils;
import jthemes.themes.ModernTheme;
import me.isaiah.shell.Main;
import me.isaiah.shell.StartMenu;
import me.isaiah.shell.SystemBar;

public class DefaultTheme extends Theme {

    @Override
    public void apply() {
        ThemeUtils.setCurrentTheme(new ModernTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setSized(new Dimension(Main.p.getWidth(), 45));
        SystemBar.setPanelBackground(Color.BLACK);
        SystemBar.setButtonBackground(Color.BLACK);
        StartMenu.setColors(Color.BLACK, new Color(15,15,15,250), new Color(0,0,0, 200), new Color(25,25,25, 250), new Color(20,20,20));
    }

}
