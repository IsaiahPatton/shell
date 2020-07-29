package com.fungus_soft.desktop.api;

import java.beans.PropertyVetoException;

import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import com.fungus_soft.desktop.theme.IconPack;

@Deprecated
public class JFXProgram extends JProgram {

    private static final long serialVersionUID = 1L;
    private JFXPanel e;

    public JFXProgram(String title) {
        super(title);
        e = new JFXPanel();
        runOnFXThread(() -> Platform.setImplicitExit(false)); // Fixes JFXPanel Bug

        this.setFrameIcon(IconPack.get().fxprogram);

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
        runOnFXThread(() -> {
            e.setScene(s);
            SwingUtilities.invokeLater(() -> pack());
        });
    }

    public void runOnFXThread(Runnable r) {
        if (Platform.isFxApplicationThread())
            r.run();
        else Platform.runLater(r);
    }

    /**
     * Fixes JFXPanel NPE bug
     */
    @Override
    public void dispose() {
        try {
            super.dispose();
        } catch (NullPointerException e) {
            try {
                this.setClosed(true);
            } catch (PropertyVetoException e1) {
                e1.printStackTrace();
            }
        }
    }

}