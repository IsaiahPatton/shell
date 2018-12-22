package me.isaiah.shell;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class DefaultIconPack {

    public ImageIcon folder;
    public ImageIcon blank;
    public static Image user;
    public static Image welcome;

    public DefaultIconPack() {
        try {
            this.folder = new ImageIcon(get("folder.png", true));
            this.blank = new ImageIcon(get("blankfile.png", true));
            DefaultIconPack.user = get("user.png", false);
            DefaultIconPack.welcome = get("welcome.png", false);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static Image get(String name, boolean scale) throws IOException {
        Image i = ImageIO.read(Icon.class.getClassLoader().getResourceAsStream(name));
        if (scale) i = i.getScaledInstance(40, 40, 0);
        return i;
    }

}