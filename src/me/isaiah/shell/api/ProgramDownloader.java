package me.isaiah.shell.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import me.isaiah.downloadmanager.Download;

@ProgramInfo(name = "Software Update", version="1.0", width=900, height=700)
public class ProgramDownloader extends JProgram {

    private static final long serialVersionUID = 1L;

    // Maven info
    private static int i = 0;

    public DownloadAction a;

    public ProgramDownloader(String url, File targetDir, DownloadAction a) throws IOException {
        this.a = a;
        targetDir.mkdirs();

        Download d = new Download(new URL(url), targetDir);

        JProgressBar pb = new JProgressBar(0, 150);
        this.setTitle("Program Update");
        JLabel z = new JLabel("Loading") {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                int k = (int)(getWidth()*(pb.getValue()/150.0f));
                g.setFont(new Font("Dialog", 0, 12));
                g.setColor(Color.ORANGE);
                g.fillRect(0, getHeight() - 5, k, 5);
                g.setColor(Color.WHITE);
                String str = formatSize(d.getDownloaded()) + " / " + formatSize(d.getSize()) + " [Status=" + Download.STATUSES[d.getStatus()] + "]";
                g.drawString("Downloading program, Please wait.", (getWidth() / 2) - (3 * 32), 24);
                g.drawString(str, (getWidth() / 2) - (3 * str.length()), getHeight() - 15);
            }
        };
        z.setFont(new Font("Dialog", Font.BOLD, 56));
        z.setForeground(Color.WHITE);
        z.setBorder(new EmptyBorder(30,65,30,65));
        //this.setUndecorated(true);
        this.setBackground(new Color(0,0,0,235));

        new Thread(() -> { Timer t = new Timer();t.schedule(new TimerTask() { @Override public void run() {
            pb.setValue((int) d.getProgress());
            z.repaint();

            if (d.getStatus() == 2 && i == 0) {
                i = 1;
                t.cancel();
                try {
                    setClosed(true);
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                }
                a.onFinished();
            }
        }}, 20, 500);}).start();

        this.setContentPane(z);
        this.pack();
        this.setVisible(true);
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