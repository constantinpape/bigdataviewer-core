package bdv.viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Passes mouse wheel events to the parent component if this component
 * cannot scroll further in the given direction.
 * <p>
 * This behavior is a little better than Swing's default behavior but
 * still worse than the behavior of Google Chrome, which remembers the
 * currently scrolling component and sticks to it until a timeout happens.
 *
 * @see <a href="https://stackoverflow.com/a/53687022">Stack Overflow</a>
 */
public final class MouseWheelScrollListener implements MouseWheelListener {

    private final JScrollPane pane;
    private int previousValue;

    public MouseWheelScrollListener(JScrollPane pane) {
        this.pane = pane;
        previousValue = pane.getVerticalScrollBar().getValue();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        Component parent = pane.getParent();
        while (!(parent instanceof JScrollPane)) {
            if (parent == null) {
                return;
            }
            parent = parent.getParent();
        }

        JScrollBar bar = pane.getVerticalScrollBar();
        int limit = e.getWheelRotation() < 0 ? 0 : bar.getMaximum() - bar.getVisibleAmount();
        if (previousValue == limit && bar.getValue() == limit) {
            parent.dispatchEvent(SwingUtilities.convertMouseEvent(pane, e, parent));
        }
        previousValue = bar.getValue();
    }
}