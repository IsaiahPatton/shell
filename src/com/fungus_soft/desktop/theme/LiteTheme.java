package com.fungus_soft.desktop.theme;

import java.awt.Color;

import jthemes.themes.JTheme;
import jthemes.themes.JThemeInfo;
import jthemes.themes.Resource;

@JThemeInfo(name="LiteSeven", version=1, dataPath="aero")
public class LiteTheme implements JTheme {

    @Override
    public Resource getTitleBar() {
        return new Resource(new Color(236, 240, 244));
    }

    @Override
    public Resource getBorder() {
        return new Resource(new Color(187, 193, 208));
    }

}