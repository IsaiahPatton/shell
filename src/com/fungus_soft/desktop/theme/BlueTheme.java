package com.fungus_soft.desktop.theme;

import java.awt.Color;

import jthemes.ThemeUtils;
import jthemes.themes.ModernLunaTheme;
import com.fungus_soft.desktop.StartMenu;
import com.fungus_soft.desktop.SystemBar;
import com.fungus_soft.desktop.api.Toast;

public class BlueTheme extends Theme {

    @Override
    public void apply() {
        ThemeUtils.setCurrentTheme(new ModernLunaTheme());
        IconPack.setIconPack(new DefaultIconPack());
        SystemBar.setPanelBackground(new Color(0, 60, 200, 240));
        SystemBar.setButtonBackground(new Color(0, 153, 51));
        StartMenu.setColors(Color.BLACK, new Color(59, 108, 145, 246), new Color(0,75,190, 200), new Color(115,25,25, 250), new Color(0, 75, 190));
        Toast.BACKGROUND = new Color(50, 120, 255);
        Toast.FOREGROUND = Color.WHITE;
    }

}