package com.fungus_soft.desktop.theme;

import java.io.IOException;

public class DefaultIconPack extends IconPack {

    private static IconPack inst;

    public DefaultIconPack() {
        try {
            this.folder = getIcon("res/icons/folder.png", true);
            this.blank = getIcon("res/icons/blankfile.png", true);
            this.img = getIcon("res/icons/img.png", true);
            this.text = getIcon("res/icons/text.png", true);
            this.fxprogram = getIcon("fxprogram.png", true);
            this.user = get("res/userpictures/avatar.png", false);
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