package me.isaiah.shell.programs;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name = "Photo Viewer")
public class ImageViewer extends JProgram {

    private static final long serialVersionUID = 1L;

    public ImageViewer(File file) {
        JLabel l = new JLabel();
        try {
            l.setIcon(new ImageIcon(ImageIO.read(file)));
        } catch (IOException e) {
            l.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
        JProgram i = new JProgram("Image Viewer");
        i.setContentPane(new JScrollPane(l));
        i.pack();
    }

}