package me.isaiah.shell.theme;

import java.awt.Color;

import jthemes.ThemeUtils;
import jthemes.themes.AeroTheme;
import me.isaiah.shell.SystemBar;

public class WindowsTheme extends Theme {

    @Override
    public void apply() {
        ThemeUtils.setCurrentTheme(new AeroTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setPanelBackground(new Color(160, 190, 210));
        SystemBar.setButtonBackground(new Color(160, 190, 210));
    }

}
