package com.openbravo.pos.sales.cinema;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 */
public class ColorPanel extends JPanel {

    /**
     */
    private static final long serialVersionUID = -6153290260638887247L;

    /**
     */
    private static final Logger LOGGER = Logger.getLogger(ColorPanel.class
        .getName());

    /**
     */
    static final Color COLOR_AVAILABLE = Color.GREEN;

    /**
     */
    static final Color COLOR_LOCKED = Color.BLACK;

    /**
     */
    static final Color COLOR_REMOVABLE = Color.BLUE;

    /**
     */
    static final Color COLOR_RESERVED = Color.MAGENTA;

    /**
     */
    static final Color COLOR_SELECTED = Color.ORANGE;

    /**
     */
    static final Color COLOR_TAKEN = Color.RED;

    /**
     */
    private static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);

    /**
     */
    private static final Border BORDER = BorderFactory.createMatteBorder(7, 13,
        7, 13, CinemaReservationMap.COLOR_BACKGROUND);

    /**
     */
    private static final Color[] COLORS = {
        COLOR_AVAILABLE, COLOR_REMOVABLE, COLOR_SELECTED, COLOR_LOCKED,
        COLOR_RESERVED, COLOR_TAKEN,
    };

    /**
     */
    private static final String[] TEXTS = {
        "AVAILABLE", "REMOVABLE", "SELECTED", "LOCKED", "RESERVED", "TAKEN",
    };

    /**
     */
    public ColorPanel() {
        super();

        LOGGER.info("ColorPanel");

        final JLabel title = new JLabel("SEATING PLAN");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(FONT_TITLE);
        title.setForeground(Color.white);

        final JPanel colorPanel = new JPanel();
        colorPanel.setBackground(CinemaReservationMap.COLOR_BACKGROUND);
        colorPanel.setLayout(new GridLayout(3, 2));

        for (int i = 0; i < 6; ++i) {
            final JLabel label = new JLabel(TEXTS[i]);
            label.setBackground(COLORS[i]);
            label.setBorder(BORDER);
            label.setForeground(Color.white);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);

            colorPanel.add(label);
        }

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(colorPanel);
    }
}
