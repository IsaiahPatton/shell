package com.fungus_soft.desktop.api;

import javax.swing.JEditorPane;

/**
 * API for creating programs with HTML
 */
public class JWebApp extends JProgram {

    private static final long serialVersionUID = 1L;
    private JEditorPane e;

    public JWebApp(String html) {
        super("JWebApp");
        e = new JEditorPane();
        e.setContentType("text/html");
        setHtml(html);
        e.setEditable(false);
        setContentPane(e);
        pack();
    }

    public void setHtml(String t) {
        if (t.contains("<title>"))
            setTitle( t.substring(t.indexOf("<title>") + 7, t.indexOf("</title>")) );

        e.setText(t);
    }

}
