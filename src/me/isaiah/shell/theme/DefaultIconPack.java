package me.isaiah.shell.theme;

import java.io.IOException;

import javax.swing.ImageIcon;

public class DefaultIconPack extends IconPack {

    private static IconPack inst;
    
    public DefaultIconPack() {
        try {
            this.folder = new ImageIcon(get("folder.png", true));
            this.blank = new ImageIcon(get("blankfile.png", true));
            this.fxprogram = new ImageIcon(get("fxprogram.png", true));
            this.user = get("user.png", false);
            this.welcome = get("welcome.png", false);
        } catch (IOException e) { e.printStackTrace(); }
        inst = this;

        if (null == IconPack.get())
            IconPack.setIconPack(this);
    }

    public static IconPack getDefault() {
        return inst;
    }

}