package com.fungus_soft.desktop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class WindowBar extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JPanel list;
    private final Map<JInternalFrame, FrameWrapper> wrappers;

    public WindowBar() {
        this.wrappers = new HashMap<JInternalFrame, FrameWrapper>();

        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.X_AXIS));
        list.setOpaque(false);

        setLayout(new GridLayout(1, 1));

        list.setBackground(new Color(0,0,0,0));
        list.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 12));

        add(list);
        this.setBackground(new Color(0,0,0,0));
        this.setOpaque(false);
    }

    public int getOpenCount() {
        return wrappers.size();
    }

    public void addFrame(final JInternalFrame frame) {
        final FrameWrapper wrapper = new FrameWrapper(frame);
        SwingUtilities.invokeLater(() -> {
            frame.addInternalFrameListener(new InternalFrameAdapter(){
                public void internalFrameClosed(InternalFrameEvent e) {
                    removeFrame(e.getInternalFrame());
                }
            });
            wrappers.put(frame, wrapper);
            list.add(wrapper);
            revalidate();
            repaint();
            list.repaint();
            setActiveFrame(frame);
        });
    }

    public void removeFrame(final JInternalFrame frame) {
        final FrameWrapper wrapper = wrappers.get(frame);
        if (wrapper == null)
            return;

        SwingUtilities.invokeLater(() -> {
            wrapper.setSize(1, wrapper.getHeight());
            list.remove(wrapper);
            wrappers.remove(frame);
            validate();
        });
    }

    public void setActiveFrame(JInternalFrame frame) {
        try {
            frame.setSelected(true);
            FrameWrapper w = wrappers.get(frame);
            if (w != null)
                w.repaint();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static class FrameWrapper extends JLabel {

        private static final long serialVersionUID = 1L;
        public final JInternalFrame frame;

        public FrameWrapper(JInternalFrame frame) {
            int size = SystemBar.get.getHeight() / 2;
            setIcon(new ImageIcon(((ImageIcon)frame.getFrameIcon()).getImage().getScaledInstance(size, size, 0)));
            this.frame = frame;
            this.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
            this.setOpaque(false);
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBorder(BorderFactory.createMatteBorder(12, 12, 12, 12, Color.GRAY));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    frame.setVisible(true);
                }
            });
            repaint();
            validate();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (frame.isSelected()) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, getHeight() - 3, getWidth(), 3);
            }
        }

        public final JInternalFrame getFrame() {
            return frame;
        }

        public String toString() {
            return frame.getTitle();
        }
    }

}