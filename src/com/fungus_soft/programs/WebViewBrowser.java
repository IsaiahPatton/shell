package com.fungus_soft.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.sexydock.SwingUtils;
import org.sexydock.tabs.jhrome.JhromeTabbedPaneUI;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import jthemes.ThemeUtils;
import jthemes.WindowPane.WindowControl;
import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;

@ProgramInfo(name = "Web Browser (WebKit version)", version="1.0", authors="Contributers", width=900, height=650)
public class WebViewBrowser extends JProgram {

    private static final long serialVersionUID = 1L;
    private final JTabbedPane tb;

    public static boolean jfxInit = false;

    public WebViewBrowser() {
        if (!jfxInit) {
            new JFXPanel(); // Init JavaFX
            Platform.runLater(() -> Platform.setImplicitExit(false));
        }
        jfxInit = true;

        tb = new JTabbedPane();
        JhromeTabbedPaneUI ui = new JhromeTabbedPaneUI();
        tb.setUI(ui);

        tb.putClientProperty( JhromeTabbedPaneUI.TAB_CLOSE_BUTTONS_VISIBLE, true );
        tb.putClientProperty( JhromeTabbedPaneUI.NEW_TAB_BUTTON_VISIBLE, true );

        MouseAdapter adapt = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent m) {
                Platform.runLater(() -> newTab("https://start.fungus-soft.com/", tb));
            }};
        ui.getNewTabButton().addMouseListener(adapt);
        adapt.mouseClicked(null);

        JPanel p = new JPanel(new BorderLayout());
        setContentPane(p);

        tb.addMouseListener(this.getTitleBar().getMouseListeners()[0]);
        tb.addMouseMotionListener(this.getTitleBar().getMouseMotionListeners()[0]);

        JPanel controls = new JPanel();
        for (WindowControl w : this.getTitleBar().getWindowControls())
            controls.add(w);
        getTitleBar().setVisible(false);

        controls.setVisible(true);
        controls.setOpaque(false);

        Container gl = (Container) this.getGlassPane();
        gl.add(controls);
        gl.setLayout(new FlowLayout(FlowLayout.RIGHT));
        gl.setVisible(true);

        ThemeUtils.addThemeChangeListener(() -> {
            Object res = ThemeUtils.getCurrentTheme().getTitleBar().obj;
            p.setBackground(res instanceof Color ? (Color)res : Color.LIGHT_GRAY);
        });

        p.add(tb, BorderLayout.CENTER);
        setVisible(true);
    }

    public void newTab(String url, final JTabbedPane tb) {
        final JTextField bar = new JTextField();
        final JFXPanel fx = new JFXPanel();
        final WebView v = new WebView();
        final WebEngine e = v.getEngine();
        final JPanel p = new JPanel(new BorderLayout());
        e.setUserAgent(e.getUserAgent() + " ZunoZap/1.0");

        ActionListener l = a -> { 
            Platform.runLater(() -> {
                String text = bar.getText();

                if (!text.contains("."))
                    text = "https://google.com/search?q=" + text;
                e.load(text.contains("://") ? text : "http://" + text);
            });
        };

        JToolBar ubar = new JToolBar();
        ubar.add(Box.createHorizontalStrut(2));
        ((JButton)ubar.add(new JButton("<"))).addActionListener(a -> {
            bar.setText(e.getHistory().getEntries().get(e.getHistory().getEntries().size() - 1).getUrl());
            Platform.runLater(() -> e.getHistory().go(-1));
        });
        ubar.add(Box.createHorizontalStrut(5));
        ubar.add(bar);
        ubar.add(Box.createHorizontalStrut(5));
        ubar.setFloatable(false);
        ((JButton)ubar.add(new JButton("Go"))).addActionListener(l);
        ubar.add(Box.createHorizontalStrut(2));

        bar.addActionListener(l);

        p.add(ubar, BorderLayout.NORTH);
        p.add(fx);

        bar.setText(url);
        fx.setScene(new Scene(v, 600, 600));

        final int c = tb.getTabCount();

        e.titleProperty().addListener((ov,o,n) -> {
            JhromeTabbedPaneUI ui = (JhromeTabbedPaneUI) tb.getUI();
            ui.getTabAt(c).setTitle(n);
            if (tb.getTitleAt(c == 0 ? 0 : c - 1).equalsIgnoreCase(o)) tb.setTitleAt(c == 0 ? 0 : c - 1, n); // Change tab title
        });
        e.load(url);

        SwingUtils.doSwing(() -> {
            tb.insertTab(e.getTitle() != null ? e.getTitle() : url, null, p, url, c);
            tb.setSelectedIndex(tb.getTabCount() - 1);
        });
    }

}