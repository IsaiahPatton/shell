package me.isaiah.shell.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.plaf.metal.MetalScrollBarUI;

public class ModernScrollPane extends JScrollPane {

    private static final long serialVersionUID = 8607734981506765935L;
    private static final int SCROLL_BAR_ALPHA_ROLLOVER = 100;
    private static final int SCROLL_BAR_ALPHA = 50;
    private static final int THUMB_SIZE = 8;
    private static final int SB_SIZE = 10;
    private static final Color THUMB_COLOR = Color.GRAY;

    public ModernScrollPane(Component view) {
        this(view, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public ModernScrollPane(int vsbPolicy, int hsbPolicy) {
        this(null, vsbPolicy, hsbPolicy);
    }

    public ModernScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        setBorder(null);

        // Set ScrollBar UI
        JScrollBar verticalScrollBar = getVerticalScrollBar();
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setUI(new ModernScrollBarUI(this));
        verticalScrollBar.setUnitIncrement(10);

        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        horizontalScrollBar.setOpaque(false);
        horizontalScrollBar.setUI(new ModernScrollBarUI(this));

        setLayout(new ScrollPaneLayout() {
            private static final long serialVersionUID = 5740408979909014146L;

            @Override
            public void layoutContainer(Container parent) {
                Rectangle availR = ((JScrollPane) parent).getBounds();
                availR.x = availR.y = 0;

                // viewport
                Insets insets = parent.getInsets();
                availR.x = insets.left;
                availR.y = insets.top;
                availR.width -= insets.left + insets.right;
                availR.height -= insets.top + insets.bottom;
                if (viewport != null) {
                    viewport.setBounds(availR);
                }

                // vertical scroll bar
                Rectangle vsbR = new Rectangle();
                vsbR.width = SB_SIZE;
                vsbR.height = availR.height;
                vsbR.x = availR.x + availR.width - vsbR.width;
                vsbR.y = availR.y;
                if (vsb != null) {
                    vsb.setBounds(vsbR);
                }
            }
        });

        // Layering
        setComponentZOrder(getVerticalScrollBar(), 0);
        setComponentZOrder(getHorizontalScrollBar(), 1);
        setComponentZOrder(getViewport(), 2);

        //viewport.setView(view);
        validate();
    }

    /**
     * Class extending the BasicScrollBarUI and overrides all necessary methods
     */
    private static class ModernScrollBarUI extends MetalScrollBarUI {

        private JScrollPane sp;

        public ModernScrollBarUI(ModernScrollPane sp) {
            this.sp = sp;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new InvisibleScrollBarButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new InvisibleScrollBarButton();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
            int orientation = scrollbar.getOrientation();
            int x = r.x;
            int y = r.y;

            int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : r.width;
            width = Math.max(width, THUMB_SIZE);

            int height = orientation == JScrollBar.VERTICAL ? r.height : THUMB_SIZE;
            height = Math.max(height, THUMB_SIZE);

            Graphics2D graphics2D = (Graphics2D) g.create();
            graphics2D.setColor(new Color(THUMB_COLOR.getRed(), THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
            graphics2D.fillRect(x, y, width, height);
            graphics2D.dispose();
          }

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            sp.repaint();
        }

        /**
         * Invisible Buttons, to hide scroll bar buttons
         */
        private static class InvisibleScrollBarButton extends JButton {

            private static final long serialVersionUID = 1552427919226628689L;

            private InvisibleScrollBarButton() {
                setOpaque(false);
                setFocusable(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }
}