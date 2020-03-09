package me.isaiah.shell.theme;

import java.awt.Color;
import java.awt.Dimension;

import jthemes.ThemeUtils;
import jthemes.themes.AeroTheme;
import me.isaiah.shell.Main;
import me.isaiah.shell.StartMenu;
import me.isaiah.shell.SystemBar;

public class WindowsTheme extends Theme {

    @Override
    public void apply() {
        ThemeUtils.setCurrentTheme(new AeroTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setSized(new Dimension(Main.p.getWidth(), 35));
        SystemBar.setPanelBackground(new Color(164, 194, 217));
        SystemBar.setButtonBackground(new Color(160, 190, 210));
        StartMenu.setColors(Color.BLACK, new Color(15,15,15,250), new Color(0,0,0, 200), new Color(25,25,25, 250), new Color(20,20,20));
    }

}
