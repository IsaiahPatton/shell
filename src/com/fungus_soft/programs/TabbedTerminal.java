package com.fungus_soft.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.sexydock.tabs.jhrome.JhromeTabbedPaneUI;

import com.fungus_soft.desktop.Utils;
import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.ProgramInfo;

import jthemes.ThemeUtils;
import jthemes.WindowPane.WindowControl;

@ProgramInfo(name = "Command Prompt", width=800, height=450)
public class TabbedTerminal extends JProgram {

    private static final long serialVersionUID = 1L;
    public JTabbedPane tb;
    public JPanel controls;

    public TabbedTerminal() {
        JhromeTabbedPaneUI ui = new JhromeTabbedPaneUI();
        tb = new JTabbedPane();
        controls = new JPanel();
        tb.setUI(ui);

        tb.putClientProperty( JhromeTabbedPaneUI.TAB_CLOSE_BUTTONS_VISIBLE, true );
        tb.putClientProperty( JhromeTabbedPaneUI.NEW_TAB_BUTTON_VISIBLE, true );
        tb.putClientProperty( JhromeTabbedPaneUI.CONTENT_PANEL_BORDER, BorderFactory.createMatteBorder(0,0,0,0, new Color(20,20,20)) );

        MouseAdapter e = Utils.click(a -> {
            int i = tb.getTabCount();
            Terminal c = new Terminal() {
                private static final long serialVersionUID = 1L;
                int a = 0;
                @Override
                public void setTitle(String s) {
                    if (tb.getTabCount() > 0 && a > 0)
                        tb.setTitleAt(i, s);
                    super.setTitle(s);
                    a++;
                }

                @Override
                public void onCommand(String c) {
                    a++;
                    super.onCommand(c);
                }

                @Override
                public ProgramInfo getInfo() {
                    return Terminal.class.getAnnotation(ProgramInfo.class);
                }
            };
            tb.addTab("Command Prompt", c.getContentPane());
            tb.setSelectedIndex(i);
            c.setVisible(true);
        });

        ui.getNewTabButton().addMouseListener(e);
        e.mouseClicked(null);
        tb.setOpaque(false);

        JPanel p = new JPanel(new BorderLayout());
        p.add(tb);
        Object res = ThemeUtils.getCurrentTheme().getTitleBar().obj;

        tb.addMouseListener(this.getTitleBar().getMouseListeners()[0]);
        tb.addMouseMotionListener(this.getTitleBar().getMouseMotionListeners()[0]);

        for (WindowControl w : this.getTitleBar().getWindowControls()) controls.add(w);
        getTitleBar().setVisible(false);

        controls.setVisible(true);
        controls.setOpaque(false);

        Container gl = (Container) this.getGlassPane();
        gl.add(controls);
        gl.setLayout(new FlowLayout(FlowLayout.RIGHT));
        gl.setVisible(true);

        p.setBackground(res instanceof Color ? (Color)res : Color.LIGHT_GRAY);
        setContentPane(p);
    }

}