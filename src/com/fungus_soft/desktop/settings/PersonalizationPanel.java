package com.fungus_soft.desktop.settings;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.StartMenu;
import com.fungus_soft.desktop.SystemBar;
import com.fungus_soft.desktop.theme.Theme;

import jthemes.ThemeUtils;
import com.fungus_soft.desktop.api.ProgramInfo;

@ProgramInfo(name = "Personalization", version="1.0", authors="contributers", width=600, height=400)
public class PersonalizationPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private String[] themes = {
            "Default Theme : com.fungus_soft.desktop.theme.DefaultTheme",
            "Default Light : com.fungus_soft.desktop.theme.DefaultLightTheme",
            "Default Black : com.fungus_soft.desktop.theme.BlackTheme",
            "Windows Theme : com.fungus_soft.desktop.theme.WindowsTheme",
            "Blue Theme : com.fungus_soft.desktop.theme.BlueTheme",
    };

    private String[] wallpapers = {
            "Material Background (1080p) : a.jpg",
            "Yellow Shapes (1080p) : material-design-1920x1200-stock-yellow-shapes-material-hd-14202.jpg",
            "Green Hill (1080p): hill-meadow-tree-green-1920x1080.jpg",
            "Blank",
            "Provided by Windows"
    };

    private static int selectedTheme = 0;

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
            return ("rgb=(" + this.getRed() + "," + this.getGreen() + "," + this.getBlue()) + "), " + Math.round((((double)getAlpha()/255) * 100)) + "% solid";
        }
    }

    public PersonalizationPanel() {
        JPanel p = new JPanel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JComboBox<String> petList = new JComboBox<>();
        for (String s : themes)
            petList.addItem(s.split(":")[0].trim());

        petList.setSelectedIndex(selectedTheme);
        petList.addActionListener(a -> {
            try {
                int index = ((JComboBox<?>) a.getSource()).getSelectedIndex();
                String str = themes[index].split(":")[1].trim();
                Theme.setCurrentTheme((Theme) Class.forName( str ).newInstance());
                selectedTheme = index;
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        p.add(petList);
        p.setBorder(BorderFactory.createTitledBorder("UI Theme Selection"));

        JPanel p2 = new JPanel();
        p2.setBorder(BorderFactory.createTitledBorder("Wallpaper"));

        JComboBox<String> paperList = new JComboBox<>();
        for (String str : wallpapers) {
            if (str.equals("Provided by Windows") && !System.getProperty("os.name").contains("indows"))
                continue;
            paperList.addItem(str.split(":")[0]);
        }

        paperList.setSelectedItem(Theme.getCurrentTheme().getClass().getName());
        paperList.addActionListener(a -> {
            try {
                String bg = wallpapers[((JComboBox<?>) a.getSource()).getSelectedIndex()];

                if (bg.equals("Blank")) {
                    Main.p.img = null;
                    Main.p.repaint();
                } else if (bg.equals("Provided by Windows")) {
                    try {
                        Main.p.setBackground(ImageIO.read(new File( system("WMIC PATH Win32_DESKTOP GET Wallpaper", true)[4].trim() )));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Main.p.setBackground(ImageIO.read(Main.class.getClassLoader().getResource("res/background/" + bg.split(":")[1].trim())));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        p2.add(paperList);

        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
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
        JComboBox<?>[] boxes = {l4,l3,l2};
        for (JComboBox<?> box : boxes) {
            JPanel p3w = new JPanel();
            p3w.add(box);
            p3.add(p3w);
        }

        add(p);
        add(p2);
        add(p3);
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

    public void addNewTheme(String description, String themeClassName) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : themes)
            list.add(description + " : " + s);

        list.add(themeClassName);
        themes = list.toArray(new String[0]);
    }

}