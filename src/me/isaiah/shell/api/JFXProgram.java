package me.isaiah.shell.api;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

public class JFXProgram extends JProgram {

    private static final long serialVersionUID = 1L;
    private JFXPanel e;

    public JFXProgram(String title) {
        super(title);
        e = new JFXPanel();

        setContentPane(e);
        pack();
    }

    public JFXProgram() {
        this("Untitled JFXProgram");
    }

    public JFXPanel getFX() {
        return e;
    }

    public void setScene(Scene s) {
        e.setScene(s);
    }

}