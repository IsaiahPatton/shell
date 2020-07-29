package com.fungus_soft.desktop.theme;

public abstract class Theme {
    
    private static Theme current;
    private static Theme def;

    public static Theme getCurrentTheme() {
        if (def == null) def = new DefaultTheme();
        return current == null ? def : current;
    }

    public static void setCurrentTheme(Theme theme) {
        if (def == null) def = new DefaultTheme();
        current = theme;
        if (def != current)
            def.apply();
        current.apply();
    }

    public abstract void apply();

}