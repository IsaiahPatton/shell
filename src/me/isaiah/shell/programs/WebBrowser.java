package me.isaiah.shell.programs;

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

import com.codebrig.journey.JourneyBrowserView;
import com.codebrig.journey.proxy.CefBrowserProxy;

import jthemes.ThemeUtils;
import jthemes.WindowPane.WindowControl;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;
import me.isaiah.shell.api.Toast;

@ProgramInfo(name = "Web Browser (Chromium)", version="1.0", authors="Contributers", width=900, height=650)
public class WebBrowser extends JProgram {

    private static final long serialVersionUID = 1L;
    private final JTabbedPane tb;

    public WebBrowser() {
        tb = new JTabbedPane();
        JhromeTabbedPaneUI ui = new JhromeTabbedPaneUI();
        tb.setUI(ui);

        tb.putClientProperty( JhromeTabbedPaneUI.TAB_CLOSE_BUTTONS_VISIBLE, true );
        tb.putClientProperty( JhromeTabbedPaneUI.NEW_TAB_BUTTON_VISIBLE, true );

        MouseAdapter adapt = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent m) {
                newTab("https://start.fungus-soft.com/", tb);
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
        final JPanel p = new JPanel(new BorderLayout());
        final JourneyBrowserView chrome = new JourneyBrowserView(url);
        final CefBrowserProxy e = chrome.getCefBrowser();

        ActionListener l = a -> { 
            String text = bar.getText();

            if (!text.contains("."))
                text = "https://google.com/search?q=" + text;
            int s = e.getFrameCount();

            Toast.show("" + s, 500);
            e.loadURL(text.contains("://") ? text : "http://" + text);
        };

        JToolBar ubar = new JToolBar();
        ubar.add(Box.createHorizontalStrut(2));
        ((JButton)ubar.add(new JButton("<"))).addActionListener(a -> {
            if (e.canGoBack())
                e.goBack();
        });
        ubar.add(Box.createHorizontalStrut(5));
        ubar.add(bar);
        ubar.add(Box.createHorizontalStrut(5));
        ubar.setFloatable(false);
        ((JButton)ubar.add(new JButton("Go"))).addActionListener(l);
        ubar.add(Box.createHorizontalStrut(2));

        bar.addActionListener(l);

        p.add(ubar, BorderLayout.NORTH);
        p.add(chrome, BorderLayout.CENTER);

        bar.setText(url);

        final int c = tb.getTabCount();

        /*
        e.titleProperty().addListener((ov,o,n) -> {
            JhromeTabbedPaneUI ui = (JhromeTabbedPaneUI) tb.getUI();
            ui.getTabAt(c).setTitle(n);
            if (tb.getTitleAt(c == 0 ? 0 : c - 1).equalsIgnoreCase(o)) tb.setTitleAt(c == 0 ? 0 : c - 1, n); // Change tab title
        });
        e.load(url);*/

        SwingUtils.doSwing(() -> {
            tb.insertTab(e.getURL(), null, p, url, c);
            tb.setSelectedIndex(tb.getTabCount() - 1);
        });
    }

}