package me.isaiah.shell.theme;

import java.awt.Color;

import jthemes.ThemeUtils;
import jthemes.themes.ModernLunaTheme;
import me.isaiah.shell.SystemBar;

public class BlueTheme extends Theme {

    @Override
    public void apply() {
        ThemeUtils.setCurrentTheme(new ModernLunaTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setPanelBackground(new Color(0, 90, 205));
        SystemBar.setButtonBackground(new Color(0, 153, 51));
    }

}
