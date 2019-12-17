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
    public ImageIcon img;
    public ImageIcon text;

    private static IconPack inst;
    
    public static ImageIcon scale(ImageIcon icon, int width, int height) {
        Image i = icon.getImage();
        icon.setImage(i.getScaledInstance(width, height, 0));
        return icon;
    }

    public static Image get(String name, boolean scale) throws IOException {
        Image i = ImageIO.read(IconPack.class.getClassLoader().getResourceAsStream(name));
        if (scale) i = i.getScaledInstance(40, 40, 0);
        return i;
    }

    public static ImageIcon getIcon(String name, boolean scale) throws IOException {
        return new ImageIcon(get(name, scale));
    }

    public static IconPack get() {
        return inst;
    }

    public static void setIconPack(IconPack i) {
        inst = i;
    }

}