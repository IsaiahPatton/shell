package me.isaiah.shell;

import java.awt.Font;

public class UpdateCheck {

    public UpdateCheck() {
        String l = Main.getUrlSource("https://api.github.com/repos/isaiahpatton/shell/releases/latest");
        if (!l.equalsIgnoreCase("internet")) {
            l = l.substring(l.indexOf("\"tag_name\":\"") + 12);
            l = l.substring(0, l.indexOf("\","));
        } else Main.showNotification("Could not connect to api.github.com\nto get update infomation.", new Font("Arial", Font.PLAIN, 13),
                5000, 320, 60);

        if (!l.equalsIgnoreCase("internet") && !l.equalsIgnoreCase(Main.VERSION))
            Main.showNotification("A new update is out!\n  Running: " + Main.VERSION + ", Latest: " + l,
                    new Font("Arial", Font.BOLD, 14), 9000, 320, 50);
    }

}