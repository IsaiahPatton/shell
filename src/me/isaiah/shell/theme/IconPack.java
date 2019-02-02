package me.isaiah.shell.theme;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IconPack {

    public Image user;
    public Image welcome;

    public ImageIcon folder;
    public ImageIcon blank;
    public ImageIcon fxprogram;

    private static IconPack inst;

    public static Image get(String name, boolean scale) throws IOException {
        Image i = ImageIO.read(DefaultIconPack.class.getClassLoader().getResourceAsStream(name));
        if (scale) i = i.getScaledInstance(40, 40, 0);
        return i;
    }

    public static IconPack get() {
        return inst;
    }

    public static void setIconPack(IconPack i) {
        inst = i;
    }

}