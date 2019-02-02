package me.isaiah.shell.bartray;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import me.isaiah.shell.Main;
import me.isaiah.shell.MouseClick;
import me.isaiah.shell.Multithreading;
import me.isaiah.shell.api.JProgram;

public class InternetIcon extends TrayIcon {

    private static final long serialVersionUID = 1L;

    public InternetIcon() {
        super(null, "wifi.png");

        this.addMouseListener(MouseClick.click(e -> {
            String[] txt = system("netsh wlan show interfaces", true);

            String ssid = "Not Connected";
            int progress = 0;

            for (String str : txt) {
                str = str.trim();
                if (!str.contains(":")) continue;

                String after = str.substring(str.indexOf(":") + 1).trim();
                if (str.startsWith("SSID")) ssid = after;

                if (str.startsWith("Signal"))
                    progress = Integer.valueOf(after.replace("%", ""));
            }

            JProgressBar pb = new JProgressBar();
            pb.setValue(progress);
            pb.setStringPainted(true);

            JPanel z = new JPanel();
            JLabel l = new JLabel(ssid);
            z.add(l);
            z.add(pb);

            Timer t = new Timer(2000, a -> refresh(pb, l));
            t.start();

            JProgram p = new JProgram("Network");
            p.setVisible(true);
            p.setContentPane(z);
            p.addInternalFrameListener(new InternalFrameAdapter(){
                public void internalFrameClosing(InternalFrameEvent e) { t.stop(); }
            });
            Main.p.add(p, 200, 300);
            effect(p);
        }));

    }

    public void effect(JProgram p) {
        new Thread(() -> {
            for (int i = Main.taskbar.getY(); i > (Main.taskbar.getY() - p.getHeight()); i--) {
                p.setLocation(((TaskBarTray.get().getX() - 15) - (getX() + getWidth())), i--);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e1) { e1.printStackTrace(); }
                p.validate();
            }
        }).start();

        p.setLocation(Main.taskbar.getWidth(), Main.taskbar.getY() - p.getHeight());
        p.validate();
    }

    public void refresh(JProgressBar pb, JLabel ssidl) {
        Multithreading.run(() -> {
            String[] txt = system("netsh wlan show interfaces", true);

            String ssid = "Not Connected";
            int progress = 0;

            for (String str : txt) {
                str = str.trim();
                if (!str.contains(":")) continue;
    
                String after = str.substring(str.indexOf(":") + 1).trim();
                if (str.startsWith("SSID")) ssid = after;
    
                if (str.startsWith("Signal"))
                    progress = Integer.valueOf(after.replace("%", ""));
            }
    
            pb.setValue(progress);
            pb.setStringPainted(true);
    
            ssidl.setText(ssid);
        });
    }

}