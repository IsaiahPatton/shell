package com.fungus_soft.programs.zunozap;

import java.net.URL;

import com.fungus_soft.desktop.api.JFXProgram;
import com.zunozap.Engine.Type;
import com.zunozap.EngineHelper;
import com.zunozap.Reader;
import com.zunozap.Settings;
import com.zunozap.Settings.Options;
import com.zunozap.impl.BrowserImpl;
import com.zunozap.impl.ChromeEngine;
import com.zunozap.impl.WebKitEngine;

import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ZunoZapImpl extends BrowserImpl {

    private JFXProgram swing;
    public ZunoZapImpl(JFXProgram swing) {
        super();
        this.swing = swing;
    }

    @Override
    public void start(Stage stage) throws Exception {
        EngineHelper.setEngine(Type.WEBKIT, WebKitEngine.class);
        this.stage = stage;
        StackPane root = new StackPane();
        BorderPane border = new BorderPane();

        root.getChildren().add(border);
        Scene scene = new Scene(root, 1200, 700);

        Settings.en = EngineHelper.type;
        menuBar = new MenuBar();
        tb = new TabPane();

        Settings.init(cssDir);

        mkDirs(home, cssDir);

        if (Options.COMPACT.b) {
            Tab m = new Tab();
            m.setClosable(false);
            menuBar.setBackground(null);
            m.setGraphic(menuBar);
            m.setId("createtab");
            tb.getTabs().add(m);
            tb.setRotateGraphic(true);
        } else border.setTop(menuBar);

        bmread = new Reader(menuBook);

        tb.setPrefSize(1365, 768);
        Tab newtab = new Tab(" + ");
        newtab.setClosable(false);
        newtab.setId("createtab");
        tb.getTabs().add(newtab);

        tb.getSelectionModel().selectedItemProperty().addListener((a,b,c) -> { if (c == newtab) createTab(Settings.tabPage); });

        border.setCenter(tb);
        border.autosize();

        start(stage, scene, root, border);

        swing.setTitle(swing.getTitle() + " " + VERSION);
        swing.setScene(scene);
        swing.show();
    }

}
