package com.fungus_soft.programs;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;
import com.fungus_soft.ui.ModernScrollPane;

@ProgramInfo(name = "Photo Viewer", width=400, height=400)
public class ImageViewer extends JProgram {

    private static final long serialVersionUID = 1L;

    public ImageViewer(File file) {
        JLabel l = new JLabel();
        try {
            l.setIcon(new ImageIcon(ImageIO.read(file)));
        } catch (IOException e) {
            l.setText("Error: " + e.getMessage());
            e.printStackTrace();
        };
        this.setContentPane(new ModernScrollPane(l));
        this.setSize(400, 400);
        this.setVisible(true);
    }

}