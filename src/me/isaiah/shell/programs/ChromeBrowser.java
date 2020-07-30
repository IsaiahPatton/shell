package me.isaiah.shell.programs;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JPanel;

import com.codebrig.journey.JourneyBrowserView;

import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramDownloader;
import com.fungus_soft.desktop.api.ProgramDownloader.DownloadAction;
import com.fungus_soft.desktop.api.ProgramInfo;

@ProgramInfo(name = "Web Chrome", version="1.0", authors="Fungus Software", width=900, height=700)
public class ChromeBrowser extends JProgram {

    private static final long serialVersionUID = 1L;
    private ProgramDownloader dl;
    private boolean finished;
    private URLClassLoader l;

    public ChromeBrowser() {
        File f = new File(new File(Main.pStorage.getParentFile(), "downloadedPrograms"), "journey-browser-0.4.0.jar");
        f.getParentFile().mkdir();

        dl = new ProgramDownloader("https://github.com/CodeBrig/Journey/releases/download/0.4.0-offline/journey-browser-0.4.0.jar", f.getParentFile());
        DownloadAction a = () -> {
            if (dl.finished) {
                try {
                    finished = true;
                    ClassLoader l = new URLClassLoader(new URL[] {f.toURI().toURL()});
                    JPanel pan = new JPanel(new BorderLayout());
                    JourneyBrowserView chrome = (JourneyBrowserView) Class.forName("com.codebrig.journey.JourneyBrowserView", false, l).getConstructor(String.class).newInstance("https://duck.com/");
                    pan.add(chrome, BorderLayout.CENTER);
                    setContentPane(pan);
                    pack();
                    setTitle("Fungus Chromium");
                    super.setVisible(true);
                    Main.p.add(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        if (f.exists()) {
            dl.finished = true;
            a.onFinished();
        } else {
            try {
                System.out.println("test");
                this.setContentPane(dl);
                dl.start(a);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setVisible(boolean b) {
        if (!finished)
            if (dl != null) dl.setVisible(b);
        super.setVisible(b);
    }

}