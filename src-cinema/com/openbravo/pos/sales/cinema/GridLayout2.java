package com.openbravo.pos.sales.cinema;

// http://www.javaworld.com/javaworld/javatips/javatip121/GridLayout2.java

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

//Grid Layout which allows components of differrent sizes
@SuppressWarnings("all")
public class GridLayout2 extends GridLayout {

    public GridLayout2() {
        this(1, 0, 0, 0);
    }

    public GridLayout2(final int rows, final int cols) {
        this(rows, cols, 0, 0);
    }

    public GridLayout2(final int rows, final int cols, final int hgap,
    final int vgap) {
        super(rows, cols, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(final Container parent) {
        // System.err.println("preferredLayoutSize");
        synchronized (parent.getTreeLock()) {
            final Insets insets = parent.getInsets();
            final int ncomponents = parent.getComponentCount();
            int nrows = this.getRows();
            int ncols = this.getColumns();
            if (nrows > 0) {
                ncols = ((ncomponents + nrows) - 1) / nrows;
            } else {
                nrows = ((ncomponents + ncols) - 1) / ncols;
            }
            final int[] w = new int[ncols];
            final int[] h = new int[nrows];
            for (int i = 0; i < ncomponents; i++) {
                final int r = i / ncols;
                final int c = i % ncols;
                final Component comp = parent.getComponent(i);
                final Dimension d = comp.getPreferredSize();
                if (w[c] < d.width) {
                    w[c] = d.width;
                }
                if (h[r] < d.height) {
                    h[r] = d.height;
                }
            }
            int nw = 0;
            for (int j = 0; j < ncols; j++) {
                nw += w[j];
            }
            int nh = 0;
            for (int i = 0; i < nrows; i++) {
                nh += h[i];
            }
            return new Dimension(insets.left + insets.right + nw
                + ((ncols - 1) * this.getHgap()), insets.top + insets.bottom
                + nh + ((nrows - 1) * this.getVgap()));
        }
    }

    @Override
    public Dimension minimumLayoutSize(final Container parent) {
        // System.err.println("minimumLayoutSize");
        synchronized (parent.getTreeLock()) {
            final Insets insets = parent.getInsets();
            final int ncomponents = parent.getComponentCount();
            int nrows = this.getRows();
            int ncols = this.getColumns();
            if (nrows > 0) {
                ncols = ((ncomponents + nrows) - 1) / nrows;
            } else {
                nrows = ((ncomponents + ncols) - 1) / ncols;
            }
            final int[] w = new int[ncols];
            final int[] h = new int[nrows];
            for (int i = 0; i < ncomponents; i++) {
                final int r = i / ncols;
                final int c = i % ncols;
                final Component comp = parent.getComponent(i);
                final Dimension d = comp.getMinimumSize();
                if (w[c] < d.width) {
                    w[c] = d.width;
                }
                if (h[r] < d.height) {
                    h[r] = d.height;
                }
            }
            int nw = 0;
            for (int j = 0; j < ncols; j++) {
                nw += w[j];
            }
            int nh = 0;
            for (int i = 0; i < nrows; i++) {
                nh += h[i];
            }
            return new Dimension(insets.left + insets.right + nw
                + ((ncols - 1) * this.getHgap()), insets.top + insets.bottom
                + nh + ((nrows - 1) * this.getVgap()));
        }
    }

    @Override
    public void layoutContainer(final Container parent) {
        // System.err.println("layoutContainer");
        synchronized (parent.getTreeLock()) {
            final Insets insets = parent.getInsets();
            final int ncomponents = parent.getComponentCount();
            int nrows = this.getRows();
            int ncols = this.getColumns();
            if (ncomponents == 0) {
                return;
            }
            if (nrows > 0) {
                ncols = ((ncomponents + nrows) - 1) / nrows;
            } else {
                nrows = ((ncomponents + ncols) - 1) / ncols;
            }
            final int hgap = this.getHgap();
            final int vgap = this.getVgap();
            // scaling factors
            final Dimension pd = this.preferredLayoutSize(parent);
            final double sw = (1.0 * parent.getWidth()) / pd.width;
            final double sh = (1.0 * parent.getHeight()) / pd.height;
            // scale
            final int[] w = new int[ncols];
            final int[] h = new int[nrows];
            for (int i = 0; i < ncomponents; i++) {
                final int r = i / ncols;
                final int c = i % ncols;
                final Component comp = parent.getComponent(i);
                final Dimension d = comp.getPreferredSize();
                d.width = (int) (sw * d.width);
                d.height = (int) (sh * d.height);
                if (w[c] < d.width) {
                    w[c] = d.width;
                }
                if (h[r] < d.height) {
                    h[r] = d.height;
                }
            }
            for (int c = 0, x = insets.left; c < ncols; c++) {
                for (int r = 0, y = insets.top; r < nrows; r++) {
                    final int i = (r * ncols) + c;
                    if (i < ncomponents) {
                        parent.getComponent(i).setBounds(x, y, w[c], h[r]);
                    }
                    y += h[r] + vgap;
                }
                x += w[c] + hgap;
            }
        }
    }
}
