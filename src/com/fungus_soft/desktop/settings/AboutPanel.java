package com.fungus_soft.desktop.settings;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.Utils;
import com.fungus_soft.desktop.api.ProgramInfo;

@ProgramInfo(name = "About System")
public class AboutPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public AboutPanel() {
        JProgressBar pb = new JProgressBar();
        pb.setStringPainted(true);
        this.setLayout(new BorderLayout());

        JEditorPane e = new JEditorPane();
        e.setContentType("text/html");
        e.setText("<div style='padding:8px 16px 8px 16px;'><h2>" + Main.NAME + "</h2><hr>"
                + "<div style='padding:16px 0 8px 12px'>Version " + Main.VERSION + "<br>Java version: " + Utils.getJavaVersion()
                + "<br>Heap size: " + Main.mem + "<p>&copy; 2020 Fungus Software - https://fungus-soft.com/</p></div><br><h2>Operating System</h2><hr>"
                        + "<div style='padding:16px 0 8px 12px'><span>Operating System Name: " + System.getProperty("os.name") + "</span></div></div>");
        e.setEditable(false);
        e.setBorder(null);
        e.setOpaque(false);
        add(e, BorderLayout.CENTER);
    }

}