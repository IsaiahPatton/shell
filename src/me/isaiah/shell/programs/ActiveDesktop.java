package me.isaiah.shell.programs;

import javax.swing.JRootPane;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import me.isaiah.shell.Main;
import me.isaiah.shell.MouseClick;
import me.isaiah.shell.api.JProgram;

public class ActiveDesktop extends JProgram {

    private static final long serialVersionUID = 1L;

    public ActiveDesktop() {
        super("ZunoZap Browser [ActiveDesktop]");
        setLocation(0, 0);
        putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        final JFXPanel fx = new JFXPanel();

        Platform.runLater(() -> {
            final WebView v = new WebView();
            final WebEngine e = v.getEngine();

            fx.setScene(new Scene(v, 1200, 600));

            v.setOnMousePressed(a -> moveToBack());
            e.load("http://start.duckduckgo.com");
        });

        setContentPane(fx);
        setBorder(null);
        setVisible(true);
        pack();
        setSize(Main.p.getWidth(), Main.p.getHeight());
        validate();
        addMouseListener(MouseClick.click(e -> moveToBack()));
        setResizable(false);

        Main.p.add(this);
        moveToBack();
    }

    public void moveToBack() {
        Main.p.setComponentZOrder(this, 0);
        Main.p.moveToBack(this);
    }

}