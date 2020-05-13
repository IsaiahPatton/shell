package me.isaiah.shell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;

import me.isaiah.shell.programs.FileExplorer;
import me.isaiah.shell.theme.DefaultIconPack;
import me.isaiah.shell.theme.IconPack;
import me.isaiah.shell.ui.OutlineLabel;

public class Icon extends OutlineLabel {

    private static final long serialVersionUID = 1L;
    private static IconPack pack;
    public boolean hasIcon;
    private File f;

    public Icon(File f) {
        this(f, false, Color.WHITE, false);
    }

    public Icon(File f, boolean lis, Color fg, boolean outline) {
        super(f.getName());
        if (outline) this.outlineColor = Color.BLACK;
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

        if (lis) this.addActionListener(l -> FileExplorer.newExplorer(f));
    }

    public void setIcon(String name, boolean folder) throws IOException {
        
        this.setFont(new Font("Arial", Font.PLAIN, this.getFont().getSize()));

        if (folder) {
            setIcon(pack.folder);
            return;
        }

        if (name.endsWith(".txt") || name.endsWith(".text") || name.endsWith(".html")) 
            setIcon(pack.text);

        if (name.endsWith(".jar")) {
            setIcon("jar.png");
            setText(name.substring(0, name.lastIndexOf(".")));
        }

        if (name.endsWith(".exe"))
            setIcon("exe.png");

        if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".jpeg"))
            setIcon(pack.img);

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
        ImageIcon icon = new ImageIcon(ImageIO.read(Icon.class.getClassLoader().getResourceAsStream("res/icons/" + name)));
        icon.setImage(icon.getImage().getScaledInstance(38, 38, 0));
        this.setIcon(icon);
    }

    public void addActionListener(ActionListener l) {
        this.addMouseListener(Utils.click(e -> l.actionPerformed(null)));
    }

}