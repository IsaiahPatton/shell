package me.isaiah.shell.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import me.isaiah.shell.MouseClick;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;
import me.isaiah.zunozap.Info;
import me.isaiah.zunozap.UniversalEngine.Engine;

@Info(name="ZunoZap", version="0.6.1", engine = Engine.CHROME, enableGC = false)
@ProgramInfo(name = "Web Browser", version="0.6.1-swing", authors="Contributers", width=1000, height=650)

public class ZunoZapSwing extends JProgram {

    private static final long serialVersionUID = 1L;
    private final JTabbedPane tb;
    private final ActionListener removelis;
    public boolean hasInitFx = false;

    public ZunoZapSwing() {

        if (!hasInitFx) new JFXPanel(); // Init JavaFX

        tb = new JTabbedPane();
        removelis = new ActionListener() { public void actionPerformed(ActionEvent e) { 
            tb.remove(tb.getSelectedIndex());
            tb.setSelectedIndex(tb.getTabCount() - 2);
        }};
        tb.addTab(" + ", new JTextField("Loading..."));
        tb.setBackgroundAt(0, Color.LIGHT_GRAY);

        Platform.runLater(() -> {
            try {
                newTab("http://zunozap.com/", tb);
            } catch (IOException e1) { e1.printStackTrace(); }
        });

        getContentPane().add(tb, BorderLayout.CENTER);
        setSize(800, 600);
        setVisible(true);
        getContentPane().setBackground(Color.ORANGE);
        tb.addMouseListener(MouseClick.click(m -> {
            Platform.runLater(() -> {
                if ((tb.getSelectedIndex() + 1) == tb.getTabCount()) {
                    try {
                        newTab("http://google.com", tb);
                    } catch (IOException e) { e.printStackTrace(); }
                } else if (m.getButton() == MouseEvent.BUTTON3) {
                    Timer timer = new Timer(400, removelis);
                    timer.setRepeats(false);
                    timer.start();
                }
            });
        }));
        
    }

    public void newTab(String url, final JTabbedPane tb) throws IOException {
        final JTextField bar = new JTextField();
        final JFXPanel fx = new JFXPanel();
        final WebView v = new WebView();
        final WebEngine e = v.getEngine();
        final JPanel p = new JPanel();

        p.setLayout(new BorderLayout());
        p.add(bar, BorderLayout.NORTH);
        p.add(fx);

        bar.addActionListener(l -> {
            String text = bar.getText();
            Platform.runLater(() -> e.load(text.contains("://") ? text : "http://" + text));
        });
        bar.setText(url);
        fx.setScene(new Scene(v, 1200, 600));

        final int c = tb.getTabCount();

        e.titleProperty().addListener((ov,o,n) -> {
            if (tb.getTitleAt(c - 1).equalsIgnoreCase(o)) tb.setTitleAt(c - 1, n); // Change tab title
        });
        e.load(url);

        tb.insertTab(e.getTitle() != null ? e.getTitle() : url, null, p, url, c > 0 ? c - 1 : c);
        tb.setSelectedIndex(tb.getTabCount() - 2);
    }

}
