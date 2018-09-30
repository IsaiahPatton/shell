package me.isaiah.shell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Icon extends JLabel {

    private static final long serialVersionUID = 1L;
    private static DefaultIconPack pack;
    public boolean hasIcon;
    
    public Icon(File f) {
        this(f, false);
    } 

    public Icon(File f, boolean lis) {
        super(f.getName());
        this.setForeground(Color.WHITE);
        if (null == pack)
            pack = new DefaultIconPack();

        hasIcon = false;
        try {
            setIcon(f.getName(), f.isDirectory());
        } catch (IOException e) { e.printStackTrace(); }

        this.setPreferredSize(new Dimension(100,55));
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        if (lis) this.addActionListener(l -> Main.newFileExplorer(f));
    }

    public void setIcon(String name, boolean folder) throws IOException {

        if (name.endsWith(".txt") || name.endsWith(".text") || name.endsWith(".html")) 
            setIcon("textfile.png");
        if (name.endsWith(".jar")) {
            if (name.startsWith("ZunoZap")) setIcon("zunozapfile.png"); else setIcon("jar.png");
            setText(name.substring(0, name.lastIndexOf(".")));
        }

        if (name.endsWith(".exe")) {
            if (name.startsWith("ZunoZap")) setIcon("zunozapfile.png"); else setIcon("exe.png");
            setText(name.substring(0, name.lastIndexOf(".")));
        }

        if (name.endsWith(".png") || name.endsWith(".jpg")) setIcon("img.png");

        if (getText().length() > 14) this.setFont(new Font("Arial", Font.PLAIN, 7));
        else this.setFont(new Font("Arial", Font.PLAIN, 10));
        if (folder) {
            setIcon(pack.folder);
            return;
        }
        if (!hasIcon)setIcon(pack.blank);
    }

    public void setIcon(Image i) throws IOException {
        this.setIcon(new ImageIcon(i));
        hasIcon = true;
    }

    public void setIcon(String name) throws IOException {
        ImageIcon icon = new ImageIcon(ImageIO.read(Icon.class.getClassLoader().getResourceAsStream(name)));
        icon.setImage(icon.getImage().getScaledInstance(40, 40, 0));
        this.setIcon(icon);
        hasIcon = true;
    }

    public void addActionListener(ActionListener l) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                l.actionPerformed(null);
            }
        });
    }

}