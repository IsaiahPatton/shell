package com.fungus_soft.desktop.settings;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import com.fungus_soft.desktop.Utils;
import com.fungus_soft.desktop.api.ProgramInfo;

@ProgramInfo(name = "Network Settings", version="1.0", authors="contributers", width=600, height=400)
public class NetworkPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Timer t;
    private final JPanel z;
    private final JLabel ref;

    public NetworkPanel() {
        JProgressBar pb = new JProgressBar();
        pb.setStringPainted(true);

        ref = new JLabel();

        z = new JPanel();
        z.setLayout(new BoxLayout(z, BoxLayout.Y_AXIS));

        t = new Timer(10000, a -> refresh());
        t.setInitialDelay(20);
        t.start();

        add(z);
    }

    @Override
    public void setName(String s) {
        if (s.equals("close"))
            t.stop();
        super.setName(s);
    }

    public void refresh() {
        Utils.runAsync(() -> {
            ref.setText("Refreshing...");
            String[] txt = system("netsh wlan show networks mode=Bssid", true);

            JLabel ssid = new JLabel("Hidden");
            JProgressBar pb = new JProgressBar();
            int count = 0;

            z.removeAll();
            z.add(ref);
            for (String str : txt) {
                if (str.length() == 0 && count > 0) {
                    if (ssid.getText().length() <= 0)
                        continue;
                    JPanel a = new JPanel();
                    a.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(16,28,16,28)));
                    a.setLayout(new GridLayout());
                    a.add(ssid);
                    a.add(pb);
                    z.add(a);
                    ssid = new JLabel("Hidden");
                    pb = new JProgressBar();
                    continue;
                }
                str = str.trim();
                if (str.startsWith("SSID")) count++;

                String after = str.substring(str.indexOf(":") + 1).trim();
                if (str.startsWith("SSID")) ssid.setText(after);

                if (str.startsWith("Signal"))
                    pb.setValue(Integer.valueOf(after.replace("%", "")));
            }
            validate();
            repaint();
            ref.setText("");
        });
    }

    // TODO better solution

    public String[] system(String args, boolean block) {
        return system(args.trim().split(" "), block);
    }

    public String[] system(String[] args, boolean block) {
        ArrayList<String> txt = new ArrayList<>();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args).redirectErrorStream(true);
            Process process = processBuilder.start();
            try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
                String readLine;
                while ((readLine = processOutputReader.readLine()) != null) txt.add(readLine);

                try {
                    if (block) process.waitFor();
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
            process.destroy();
        } catch (IOException e) { e.printStackTrace(); }
        return txt.toArray(new String[0]);
    }

}