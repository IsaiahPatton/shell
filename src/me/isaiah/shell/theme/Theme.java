package me.isaiah.shell.theme;

public abstract class Theme {
    
    private static Theme current;

    public static Theme getCurrentTheme() {
        return current;
    }

    public static void setCurrentTheme(Theme theme) {
        current = theme;
        current.apply();
    }

    public abstract void apply();

}