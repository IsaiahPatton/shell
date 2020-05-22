package com.fungus_soft.desktop.api.loader;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UIManagerWrapper extends UIManager {

    private static final long serialVersionUID = 1L;

    public static void setLookAndFeel(LookAndFeel newLookAndFeel) throws UnsupportedLookAndFeelException {
    }

    public static void setLookAndFeel(String newLookAndFeel) throws UnsupportedLookAndFeelException {
    }

    public static LookAndFeel getLookAndFeel() {
        return UIManager.getLookAndFeel();
    }

}