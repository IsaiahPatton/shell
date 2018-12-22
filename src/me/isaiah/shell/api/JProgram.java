package me.isaiah.shell.api;

import javax.swing.JInternalFrame;

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
    
    public ProgramInfo getInfo() {
        return getClass().getAnnotation(ProgramInfo.class);
    }

}