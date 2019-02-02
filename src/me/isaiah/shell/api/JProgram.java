package me.isaiah.shell.api;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import me.isaiah.shell.Main;

/**
 * API class for Program developers
 * 
 * In most cases you can replace JFrames with this
 * as this is a JInternalFrame
 */
public class JProgram extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Main contructor for programs
     * The window will be closable, resizable, & maximizable.
     */
    public JProgram(String title) {
        this(title, true, true, true);
    }
    
    /**
     * Main contructor for programs
     * The window will be closable, resizable, & maximizable.
     */
    public JProgram() {
        this(null);
        this.setTitle(getInfo().name());
    }

    public JProgram(String title, boolean resizable, boolean closable, boolean maximizable) {
        super(title, resizable, closable, maximizable);
        this.toFront();
        this.iconable = true;
        this.moveToFront();
        this.validate();
    }

    @Override
    public void setFrameIcon(Icon icon) {
        if (icon == null || !(icon instanceof ImageIcon)) return;

        super.setFrameIcon(new ImageIcon( ((ImageIcon) icon).getImage().getScaledInstance(16, 16, 0) ));
    }

    public BasicInternalFrameUI getUI() {
        return ((BasicInternalFrameUI)super.getUI());
    }
    
    public ProgramInfo getInfo() {
        return getClass().getAnnotation(ProgramInfo.class);
    }

    public boolean isDarkModeEnabled() {
        return Main.dark;
    }

}