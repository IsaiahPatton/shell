package me.isaiah.shell.theme;

import java.awt.Color;

import jthemes.ThemeUtils;
import jthemes.themes.ModernTheme;
import me.isaiah.shell.SystemBar;

public class DefaultTheme extends Theme {

    @Override
    public void apply() {
        ThemeUtils.setCurrentTheme(new ModernTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setPanelBackground(Color.BLACK);
        SystemBar.setButtonBackground(Color.BLACK);
    }

}
