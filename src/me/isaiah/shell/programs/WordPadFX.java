package me.isaiah.shell.programs;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import me.isaiah.shell.Main;
import me.isaiah.shell.api.JFXProgram;
import me.isaiah.shell.api.JWebApp;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name = "WordPad", version="1.0")
public class WordPadFX extends JFXProgram {

    private static final long serialVersionUID = 1L;

    public WordPadFX() {
        final HTMLEditor h = new HTMLEditor();
        h.setPrefHeight(245);
        VBox b = new VBox();

        Button j = new Button("Open as JWebApp");
        j.setOnAction(v -> {
            JWebApp w = new JWebApp(h.getHtmlText());
            w.setVisible(true);
            Main.p.add(w);
        });
        VBox.setVgrow(h, Priority.ALWAYS);
        b.getChildren().add(j);
        b.getChildren().add(h);

        Scene scene = new Scene(b);

        JMenuBar m = new JMenuBar();
        JMenu mf = new JMenu("File");
        mf.add("Save").addActionListener(l -> {
            Main.showNotification("WordPad 'Save' not implmented yet", 1000);
        });

        this.setJMenuBar(m);
        this.setScene(scene);
    }
    
}
