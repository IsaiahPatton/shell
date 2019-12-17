package me.isaiah.shell.api;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import jthemes.StyledJInternalFrame;
import me.isaiah.shell.Main;
import me.isaiah.shell.SystemBar;

/**
 * API class for Program developers
 * 
 * In most cases you can replace JFrames with this
 * as this is a JInternalFrame
 */
public class JProgram extends StyledJInternalFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Main constructor for programs
     * The window will be closable, resizable, & maximizable.
     */
    public JProgram(String title) {
        this(title, true, true, true);
    }

    /**
     * Main constructor for programs
     * The window will be closable, resizable, & maximizable.
     */
    public JProgram() {
        this(null);
        this.setTitle(getInfo().name());
    }

    public JProgram(String title, boolean resizable, boolean closable, boolean maximizable) {
        super(title, resizable, closable, maximizable);
        this.toFront();
        this.min = true;
        this.moveToFront();
        this.validate();
        this.setIconifiable(true);
        if (SystemBar.get != null) {
            this.setMaximumSize(new Dimension((int)this.getMaximumSize().getWidth(), (int)this.getMaximumSize().getHeight() - SystemBar.get.getHeight()));
            this.setMaximizedBounds(new Rectangle((int)this.getMaximumSize().getWidth(), (int)this.getMaximumSize().getHeight() - SystemBar.get.getHeight()));
        }
    }

    private Dimension oldSize;

    /**
     * When Maximized internal frame will fully fit the JDesktopPane minus the SystemBar 
     */
    @Override
    public void setMaximum(boolean b) throws PropertyVetoException {
        if (isMaximum) {
            setSize(oldSize);
            super.setMaximum(b);
            return;
        }
        oldSize = this.getSize();
        super.setMaximum(true);
        this.isMaximum = true;
        this.setSize(new Dimension(Main.p.getWidth(), Main.p.getHeight() - SystemBar.get.getHeight()));
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

    public void setDisplayInSystemBar(boolean b) {
        this.putClientProperty("dontDisplayInWindowBar", b ? null : true);
    }

}