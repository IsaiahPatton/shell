package me.isaiah.shell.api.loader;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UIManagerWrapper extends UIManager {

    public static void setLookAndFeel(LookAndFeel newLookAndFeel) throws UnsupportedLookAndFeelException {
    }

    public static void setLookAndFeel(String newLookAndFeel) throws UnsupportedLookAndFeelException {
    }

    public static LookAndFeel getLookAndFeel() {
        return UIManager.getLookAndFeel();
    }

}