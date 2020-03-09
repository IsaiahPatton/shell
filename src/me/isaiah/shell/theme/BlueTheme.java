package me.isaiah.shell.theme;

import java.awt.Color;

import jthemes.ThemeUtils;
import jthemes.themes.ModernLunaTheme;
import me.isaiah.shell.StartMenu;
import me.isaiah.shell.SystemBar;

public class BlueTheme extends Theme {

    @Override
    public void apply() {
        ThemeUtils.setCurrentTheme(new ModernLunaTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setPanelBackground(new Color(0, 90, 205));
        SystemBar.setButtonBackground(new Color(0, 153, 51));
        StartMenu.setColors(Color.BLACK, new Color(165, 208, 222), new Color(0,0,0, 200), new Color(25,25,25, 250), new Color(0, 75, 190));
    }

}
