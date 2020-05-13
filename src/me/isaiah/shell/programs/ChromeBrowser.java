package me.isaiah.shell.programs;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;

import com.codebrig.journey.JourneyBrowserView;

import me.isaiah.shell.Main;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramDownloader;
import me.isaiah.shell.api.ProgramDownloader.DownloadAction;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name = "Web Chrome", version="1.0", authors="Google, Fungus Software", width=900, height=700)
public class ChromeBrowser extends JProgram {

    private static final long serialVersionUID = 1L;
    private ProgramDownloader dl;
    private boolean finished;

    public ChromeBrowser() {
        File f = new File(Main.pStorage.getParentFile(), "downloadedPrograms");
        f.getParentFile().mkdir();
        DownloadAction a = () -> {
            JPanel pan = new JPanel(new BorderLayout());
            JourneyBrowserView chrome = new JourneyBrowserView("https://google.com/");
            pan.add(chrome, BorderLayout.CENTER);
            setContentPane(pan);
            pack();
            setTitle("Chrome");
            finished = true;
            super.setVisible(true);
            Main.p.add(this);
        };

        if (f.exists()) {
            a.onFinished();
        } else {
            try {
                dl = new ProgramDownloader("https://github.com/CodeBrig/Journey/releases/download/0.4.0-online/journey-browser-0.4.0.jar", f, a);
                Main.p.add(dl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setVisible(boolean b) {
        if (!finished)
            if (dl != null) dl.setVisible(b);
        else super.setVisible(b);
    }

}