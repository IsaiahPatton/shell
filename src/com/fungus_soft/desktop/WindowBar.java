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
import javax.swing.Timer;

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

        list.setBackground(Color.BLACK);
        list.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 12));

        add(list);
        this.setOpaque(false);
        new Timer(3000, a -> {
            for (FrameWrapper f : wrappers.values()) {
                if (f.frame.isClosed())
                    removeFrame(f.frame);
                f.repaint();
            }
        }).start();
    }

    public int getOpenCount() {
        return wrappers.size();
    }

    public void addFrame(final JInternalFrame frame) {
        final FrameWrapper wrapper = new FrameWrapper(frame);
        SwingUtilities.invokeLater(() -> {
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
            list.remove(wrapper);
            wrappers.remove(frame);
            revalidate();
            repaint();
            list.repaint();
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
        private Color barColor;

        public FrameWrapper(JInternalFrame frame) {
            int size = SystemBar.get.getHeight() / 2;
            setIcon(new ImageIcon(((ImageIcon)frame.getFrameIcon()).getImage().getScaledInstance(size, size, 0)));
            this.frame = frame;
            Color c = SystemBar.barColor;
            this.barColor = new Color(c.getRed(), c.getGreen(), c.getBlue());
            this.setBorder(BorderFactory.createMatteBorder(12, 12, 12, 12, barColor));
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBorder(BorderFactory.createMatteBorder(12, 12, 12, 12, Color.GRAY));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBorder(BorderFactory.createMatteBorder(12, 12, 12, 12, barColor));
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
            if (!colorEqual(barColor, SystemBar.barColor)) {
                Color c = SystemBar.barColor;
                this.barColor = new Color(c.getRed(), c.getGreen(), c.getBlue());
                setBorder(BorderFactory.createMatteBorder(12, 12, 12, 12, barColor));
            }
            repaint();
        }

        private boolean colorEqual(Color a, Color b) {
            return a.getRed() == b.getRed() && a.getGreen() == b.getGreen() && a.getBlue() == b.getBlue();
        }

        public final JInternalFrame getFrame() {
            return frame;
        }

        public String toString() {
            return frame.getTitle();
        }
    }

}