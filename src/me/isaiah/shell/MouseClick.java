package me.isaiah.shell;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class MouseClick extends MouseAdapter {

    private IClick i;

    @Override public void mouseClicked(MouseEvent e) { i.click(e); }

    public MouseClick set(IClick i) {
        this.i = i;
        return this;
    }

    public static MouseAdapter click(IClick e) {
        return new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e2) { e.click(e2); }
        };
    }

}