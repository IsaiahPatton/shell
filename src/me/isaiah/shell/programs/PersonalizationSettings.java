package me.isaiah.shell.programs;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jthemes.ThemeUtils;
import jthemes.themes.JTheme;
import me.isaiah.shell.Main;
import me.isaiah.shell.StartMenu;
import me.isaiah.shell.SystemBar;
import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name = "Themes", version="1.0", authors="contributers", width=800, height=600)
public class PersonalizationSettings extends JProgram {

    private static final long serialVersionUID = 1L;

    private String[] themes = {
            "jthemes.themes.ModernTheme",
            "jthemes.themes.AeroTheme",
            "jthemes.themes.LunaTheme",
            "jthemes.themes.ModernLunaTheme",
            "jthemes.themes.MetalTheme",
            "jthemes.themes.GrayTheme",
            "jthemes.themes.DarkTheme",
    };

    private Color[] bar_colors = {
            new FixedColor(0, 0, 0, 210),
            new FixedColor(0, 0, 0, 255),
            new FixedColor(0, 0, 0, 0),
            new FixedColor(0, 78, 152, 255),
            new FixedColor(0, 60, 165, 255),
            new FixedColor(250, 250, 250, 215),
            new FixedColor(5, 100, 5, 255)
    };

    public class FixedColor extends Color{
        private static final long serialVersionUID = 1L;

        public FixedColor(int r, int g, int b, int a) {
            super(r, g, b, a);
        }

        @Override
        public String toString() {
            return super.toString().substring(54) + ", " + ((getAlpha()/255) * 100) + "% solid";
        }
    }

    public PersonalizationSettings() {
        super("Personalization");
        JPanel p = new JPanel();
        JPanel all = new JPanel();
        all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));
        JScrollPane s = new JScrollPane();
        s.setViewportView(all);

        JComboBox<String> petList = new JComboBox<>(themes);
        petList.setSelectedItem(ThemeUtils.getCurrentTheme().getClass().getName());
        petList.addActionListener(a -> {
            try {
                ThemeUtils.setCurrentTheme((JTheme) Class.forName( String.valueOf(((JComboBox<?>) a.getSource()).getSelectedItem()) ).newInstance());
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        p.add(petList);
        p.setBorder(BorderFactory.createTitledBorder("Window Theme Selection"));

        JPanel p2 = new JPanel();
        p2.setBorder(BorderFactory.createTitledBorder("Wallpaper"));
        JButton b = new JButton("Use system wallpaper");
        b.addActionListener(l -> {
            try {
                Main.p.setBackground(ImageIO.read(new File( system("WMIC PATH Win32_DESKTOP GET Wallpaper", true)[4].trim() )));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        p2.add(b);

        JPanel p3 = new JPanel();
        p3.setBorder(BorderFactory.createTitledBorder("System Bar & Menu"));
        JComboBox<Color> l2 = new JComboBox<>(bar_colors);
        l2.setSelectedItem(ThemeUtils.getCurrentTheme().getClass().getName());
        l2.addActionListener(a -> SystemBar.setPanelBackground( ((Color)((JComboBox<?>) a.getSource()).getSelectedItem()) ));
        l2.setBorder(BorderFactory.createTitledBorder("Bar Color"));

        JComboBox<Color> l3 = new JComboBox<>(bar_colors);
        l3.setSelectedItem(ThemeUtils.getCurrentTheme().getClass().getName());
        l3.addActionListener(a -> SystemBar.setButtonBackground( ((Color)((JComboBox<?>) a.getSource()).getSelectedItem()) ));
        l3.setBorder(BorderFactory.createTitledBorder("Button Background"));

        JComboBox<Color> l4 = new JComboBox<>(bar_colors);
        l4.setSelectedItem(ThemeUtils.getCurrentTheme().getClass().getName());
        l4.addActionListener(a -> StartMenu.setBackgroundColor( ((Color)((JComboBox<?>) a.getSource()).getSelectedItem()) ));
        l4.setBorder(BorderFactory.createTitledBorder("Menu Background"));
        p3.add(l4);
        p3.add(l3);
        p3.add(l2);

        all.add(p);
        all.add(p2);
        all.add(p3);
        this.setContentPane(all);
    }

    public String[] system(String args, boolean block) {
        ArrayList<String> txt = new ArrayList<>();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args.trim().split(" ")).redirectErrorStream(true);
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

    public void addNewTheme(String themeClassName) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : themes)
            list.add(s);

        list.add(themeClassName);
        themes = list.toArray(new String[0]);
    }

}