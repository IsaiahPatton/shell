package me.isaiah.shell.bartray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class TrayIcon extends JLabel {

    private static final long serialVersionUID = 1L;
    
    public TrayIcon(String txt, String icon) {
        if (null != icon) {
            try {
                setIcon(icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != txt)
            this.setText(txt);
    }

    public void setIcon(String name) throws IOException {
        ImageIcon icon = new ImageIcon(ImageIO.read(TrayIcon.class.getClassLoader().getResourceAsStream(name)));
        icon.setImage(icon.getImage().getScaledInstance(16, 16, 0));
        this.setIcon(icon);
    }
    
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
