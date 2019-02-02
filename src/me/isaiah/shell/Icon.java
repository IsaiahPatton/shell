package me.isaiah.shell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;

import me.isaiah.shell.theme.DefaultIconPack;

public class Icon extends JLabel {

    private static final long serialVersionUID = 1L;
    private static DefaultIconPack pack;
    public boolean hasIcon;
    private File f;

    public Icon(File f) {
        this(f, false, Color.BLACK);
    }

    public Icon(File f, boolean lis, Color fg) {
        super(f.getName());
        this.setForeground(fg);

        if (null == pack)
            pack = new DefaultIconPack();

        hasIcon = false;
        this.f = f;
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
        
        this.setFont(new Font("Arial", Font.PLAIN, this.getFont().getSize() - 1));

        if (folder) {
            setIcon(pack.folder);
            return;
        }

        if (name.endsWith(".txt") || name.endsWith(".text") || name.endsWith(".html")) 
            setIcon("textfile.png");

        if (name.endsWith(".jar")) {
            setIcon(name.startsWith("ZunoZap") ? "zunozapfile.png" : "jar.png");
            setText(name.substring(0, name.lastIndexOf(".")));
        }

        if (name.endsWith(".exe"))
            setIcon("exe.png");

        if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".jpeg")) setIcon("img.png");

        if (!hasIcon) setIconFromSystem();
        if (!hasIcon) setIcon(pack.blank);
    }

    public void setIconFromSystem() {
        ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(f);
        icon.setImage(icon.getImage().getScaledInstance(28, 28, 0));
        this.setIcon(icon);
        hasIcon = getIcon() != null;
    }

    @Override
    public void setIcon(javax.swing.Icon i) {
        super.setIcon(i);
        hasIcon = true;
    }

    @Deprecated
    public void setIcon(String name) throws IOException {
        ImageIcon icon = new ImageIcon(ImageIO.read(Icon.class.getClassLoader().getResourceAsStream(name)));
        icon.setImage(icon.getImage().getScaledInstance(38, 38, 0));
        this.setIcon(icon);
    }

    public void addActionListener(ActionListener l) {
        this.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { l.actionPerformed(null); }
        });
    }

}