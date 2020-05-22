package com.fungus_soft.desktop.api;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.SystemBar;
import com.fungus_soft.desktop.Utils;

import me.isaiah.downloadmanager.Download;
import me.isaiah.shell.programs.ChromeBrowser;

public class ProgramDownloader extends JComponent {

    private static final long serialVersionUID = 1L;

    // Maven info
    private static int i = 0;

    public DownloadAction a;
    public boolean finished;
    private Download d;

    private String url;
    private File targetDir;

    public ProgramDownloader(String url, File targetDir) {
        this.finished = false;
        this.url = url;
        this.targetDir = targetDir;
        targetDir.mkdirs();
    } 

    public void start(DownloadAction a, JProgram pro) throws IOException {
        //super("Program Update", false, false, false);
        this.a = a;
        //this.finished = false;
        //targetDir.mkdirs();

        //Utils.runAsync(() -> {
            try {
                d = new Download(new URL(url), targetDir);
            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        //});
        pro.setVisible(false);

        JProgressBar pb = new JProgressBar(0, 150);
        String title = pro.getTitle();
        pro.setTitle("Program Update");

        JLabel z = new JLabel("Updating") {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                int k = (int)(getWidth()*(pb.getValue()/150.0f));
                g.setFont(new Font("Dialog", 0, 12));
                g.setColor(Color.ORANGE);
                g.fillRect(0, getHeight() - 10, k, 10);
                g.setColor(Color.WHITE);
                String str = formatSize(d.getDownloaded()) + " / " + formatSize(d.getSize()) + " [Status=" + Download.STATUSES[d.getStatus()] + "]";
                g.drawString("Downloading program, Please wait.", (getWidth() / 2) - (3 * 32), 24);
                g.drawString(str, (getWidth() / 2) - (3 * str.length()), getHeight() - 15);
            }
        };
        z.setFont(new Font("Dialog", Font.BOLD, 48));
        z.setForeground(Color.WHITE);
        z.setBackground(Color.BLACK);
        z.setOpaque(true);
        z.setBorder(new EmptyBorder(30,65,30,65));
        //z.setOpaque(false);
        //this.setUndecorated(true);
        this.setOpaque(true);
        this.setBackground(Color.BLACK);

        Dimension size = pro.getSize();
        new Thread(() -> { Timer t = new Timer();t.schedule(new TimerTask() { @Override public void run() {
            pb.setValue((int) d.getProgress());
            z.repaint();
            pro.setSize(350, 220);
            pro.setClosable(false);
            pro.setUndecorated(true);

            if (d.getStatus() == 2 && i == 0) {
                i = 1;
                t.cancel();
                pro.setTitle(title);
                pro.setSize(size);
                finished = true;
                a.onFinished();
            }
        }}, 20, 500);}).start();

        //this.setContentPane(z);
        //this.pack();
        Dimension p = Main.p.getSize();
        pro.setContentPane(z);
        pro.setSize(350, 220);
        pro.setLocation(p.width/2 - getWidth()/2, (p.height/2 - getHeight()/2) - SystemBar.get.getHeight());
        pro.setVisible(true);
    }

    public interface DownloadAction {
        public void onFinished();
    }

    public static String formatSize(long v) {
        if (v < 1024) return (v > 0 ? v : 0) + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double)v / (1L << (z*10)), " KMGTPE".charAt(z));
    }

}