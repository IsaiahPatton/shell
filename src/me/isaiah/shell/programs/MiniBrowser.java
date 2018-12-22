package me.isaiah.shell.programs;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import me.isaiah.shell.api.JProgram;

@Deprecated
public class MiniBrowser extends JProgram {

    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane = new JTabbedPane();

    public MiniBrowser() {
        super("Swing Web Browser");

        createNewTab();
        getContentPane().add(tabbedPane);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = menuBar.add(new JMenu("File"));
        fileMenu.add(new JMenu("New Tab")).addMouseListener(new MouseAdapter(){ @Override public void mouseClicked(MouseEvent e) { createNewTab(); }});
        fileMenu.addSeparator();
        fileMenu.setMnemonic('F');
        setJMenuBar(menuBar);
    }

    private void createNewTab() {
        JPanel panel = new JPanel(new BorderLayout());
        WebBrowserPane browserPane = new WebBrowserPane();
        WebToolBar toolBar = new WebToolBar(browserPane);
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(browserPane), BorderLayout.CENTER);
        tabbedPane.addTab("Tab " + tabbedPane.getTabCount(), panel);
    }
}

class WebBrowserPane extends JEditorPane {
    private static final long serialVersionUID = 1L;
    private List<URL> history = new ArrayList<>();
    private int historyIndex;
    public WebBrowserPane() { setEditable(false); }

    public void goToURL(URL url) {
        displayPage(url);
        history.add(url);
        historyIndex = history.size() - 1;
    }

    public URL forward() {
        historyIndex++;
        if (historyIndex >= history.size()) historyIndex = history.size() - 1;

        URL url = (URL) history.get(historyIndex);
        displayPage(url);
        return url;
    }

    public URL back() {
        historyIndex--;
        if (historyIndex < 0) historyIndex = 0;

        URL url = (URL) history.get(historyIndex);
        displayPage(url);

        return url;
    }

    private void displayPage(URL pageURL) {
        try { setPage(pageURL); } catch (IOException e) { e.printStackTrace(); }
    }
}

class WebToolBar extends JToolBar implements HyperlinkListener {
    private static final long serialVersionUID = 1L;
    private WebBrowserPane webBrowserPane;
    private JButton backButton;
    private JButton forwardButton;
    private JTextField urlTextField;

    public WebToolBar(WebBrowserPane browser) {
        super("Navigation");

        webBrowserPane = browser;
        webBrowserPane.addHyperlinkListener(this);

        urlTextField = new JTextField(25);
        urlTextField.addActionListener((l) -> {
            try {
                webBrowserPane.goToURL(new URL(urlTextField.getText()));
            } catch (MalformedURLException urlException) {
                try { webBrowserPane.goToURL(new URL("http://" + urlTextField.getText()));
            } catch (MalformedURLException e) {e.printStackTrace();}}
        });

        backButton = new JButton("<");
        backButton.addActionListener(l -> urlTextField.setText(webBrowserPane.back().toString()));
        forwardButton = new JButton(">");
        forwardButton.addActionListener(l -> urlTextField.setText(webBrowserPane.forward().toString()));
        add(backButton);
        add(forwardButton);
        add(urlTextField);
    }

    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            URL url = event.getURL();
            webBrowserPane.goToURL(url);
            urlTextField.setText(url.toString());
        }
    }

}