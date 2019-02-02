package me.isaiah.shell.programs;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import me.isaiah.shell.Main;
import me.isaiah.shell.api.JFXProgram;
import me.isaiah.shell.api.ProgramInfo;
import me.isaiah.zunozap.Info;
import me.isaiah.zunozap.Reader;
import me.isaiah.zunozap.Settings;
import me.isaiah.zunozap.Settings.Options;
import me.isaiah.zunozap.UniversalEngine;
import me.isaiah.zunozap.UniversalEngine.Engine;
import me.isaiah.zunozap.ZunoAPI;
import me.isaiah.zunozap.plugin.PluginBase;

@Info(name="ZunoZap", version="0.6.1", engine = Engine.WEBKIT, enableGC = false)
@ProgramInfo(name = "ZunoZap Browser", version="0.6.1", authors="Contributers", width=1000, height=650)

public class Browser extends ZunoAPI {

    public static final File home = new File(System.getProperty("user.home"), "zunozap");
    private static Reader bmread;

    public static void run() {
        new JFXPanel(); // init JavaFX
        Platform.runLater(() -> {
            try { 
                Browser.main(null);
            }  catch (Exception e) { 
                Main.showNotification(e.getMessage(), 5000);
                e.printStackTrace(); 
            }
        });
    }

    public static void main(String[] args) throws Exception {
        setInstance(new Browser());
        File s = new File(home, "settings.txt");
        if (!s.exists()) {
            home.mkdir();
            s.createNewFile();
            Settings.save(false);
            firstRun = true;
        }
        try { bmread = new Reader(menuBook); } catch (IOException e) { e.printStackTrace(); }
        Stage st = new Stage();
        getInstance().start(st);
    }

    @Override
    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();
        BorderPane border = new BorderPane();

        root.getChildren().add(border);
        Scene scene = new Scene(root, 1200, 600);

        en = Engine.WEBKIT;

        Settings.initCss(cssDir);
        Settings.initMenu();

        try {
            setup(new URL("https://raw.githubusercontent.com/ZunoZap/Blacklist/master/list.dat"), true);
        } catch (Exception e) {}

        mkDirs(home, saves, temp, cssDir);

        tb = new TabPane();
        menuBar = new MenuBar();

        tb.setPrefSize(1365, 768);
        tb.setSide(Side.TOP);

        // Setup tabs
        Tab newtab = new Tab(" + ");
        newtab.setClosable(false);
        tb.getTabs().add(newtab);
        createTab(true);

        tb.getSelectionModel().selectedItemProperty().addListener((a,b,c) -> { if (c == newtab) createTab(false); });

        border.setCenter(tb);
        border.setTop(menuBar);
        border.autosize();

        WebView dummy = new WebView();
        setUserAgent(dummy.getEngine());
        regMenuItems(bmread, menuFile, menuBook, aboutPageHTML(dummy.getEngine().getUserAgent(), "N/A", "ZunoZap/zunozap/master/LICENCE", "LGPLv3"), tb, Engine.WEBKIT);
        menuBar.getMenus().addAll(menuFile, menuBook);
        Settings.set(cssDir, scene);
        scene.getStylesheets().add(ZunoAPI.stylesheet.toURI().toURL().toExternalForm());

        // p.loadPlugins();
        if (allowPluginEvents()) for (PluginBase pl : p.plugins) pl.onLoad(stage, scene, tb);

        JFXProgram inf = new JFXProgram("ZunoZap 0.6 Port");
        inf.setScene(scene);
        inf.setClosable(true);
        inf.setSize(1000, 650);
        inf.setVisible(true);
        inf.addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) { stop(); }
        });
        Main.p.add(inf);
    }

    @Override
    @SuppressWarnings("static-access") 
    public final void createTab(boolean isStartTab, String url) {
        tabnum++;

        // Create Tab
        final Tab tab = new Tab("Loading...");
        tab.setTooltip(new Tooltip("Tab " + tabnum));
        tab.setId("tab-"+tabnum);

        final Button back = new Button("<"), forward = new Button(">"), goBtn = new Button("Go"), bkmark = new Button("Bookmark");

        WebView web = new WebView();
        WebEngine engine = web.getEngine();
        TextField urlField = new TextField("http://");
        HBox hBox = new HBox(back, forward, urlField, goBtn, bkmark);
        VBox vBox = new VBox(hBox, web);
        UniversalEngine e = new UniversalEngine(web);

        urlChangeLis(e, web, engine, urlField, tab, bkmark);

        goBtn.setOnAction(v -> loadSite(urlField.getText(), e));
        urlField.setOnAction(v -> loadSite(urlField.getText(), e));

        back.setOnAction(v -> history(engine, "BACK"));
        forward.setOnAction(v -> history(engine, "FORWARD"));

        bkmark.setOnAction(v -> bookmarkAction(e, bmread, (t -> createTab(false, engine.getLocation())), bkmark, menuBook));

        // Setting Styles
        urlField.setId("urlfield");
        urlField.setMaxWidth(400);
        hBox.setId("urlbar");
        hBox.setHgrow(urlField, Priority.ALWAYS);
        vBox.setVgrow(web, Priority.ALWAYS);
        vBox.autosize();

        engine.setUserDataDirectory(saves);
        setUserAgent(engine);
        engine.javaScriptEnabledProperty().set(Options.javascript.b);

        if (isStartTab) engine.loadContent("Full JFX Powered ZunoZap Browser support is exparemental");
        else loadSite(url, e);

        tab.setContent(vBox);

        tab.setOnCloseRequest(a -> {
            ((WebView) ((VBox) ((Tab) a.getSource()).getContent()).getChildren().get(1)).getEngine().loadContent("Closing");
        });

        if (allowPluginEvents()) for (PluginBase pl : p.plugins) pl.onTabCreate(tab);

        final ObservableList<Tab> tabs = tb.getTabs();
        tabs.add(tabs.size() - 1, tab);
        tb.getSelectionModel().select(tab);
    }

    public final void urlChangeLis(UniversalEngine u, WebView web, final WebEngine en, final TextField urlField, final Tab tab, Button bkmark) {
        en.locationProperty().addListener((o,oU,nU) -> Browser.this.changed(u, urlField, tab, oU, nU, bkmark, bmread));
        en.titleProperty().addListener((ov, o, n) -> tab.setText(n));
    }

    @Override
    protected void onTabClosed(Object s) {
    }

    @Override
    public void start(Stage arg0, Scene arg1, StackPane arg2, BorderPane arg3) throws Exception {
    }

}