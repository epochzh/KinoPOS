package com.openbravo.pos.sales.cinema;

import com.openbravo.pos.sales.cinema.listener.CartAddToReceiptAl;
import com.openbravo.pos.sales.cinema.listener.CartReserveAl;
import com.openbravo.pos.sales.cinema.model.Booking;
import com.openbravo.pos.sales.cinema.model.Event;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 */
public class CartPanel extends JPanel {

    /**
     */
    private static final long serialVersionUID = -9113707108321005579L;

    /**
     */
    private static final Logger LOGGER = Logger.getLogger(CartPanel.class
        .getName());

    /**
     */
    private static final Format DATE_FORMAT = new SimpleDateFormat(
        "dd MMMM yyyy");

    /**
     */
    private static final Format PRICE_FORMAT = new DecimalFormat(
        "\u00A4 ##0.00");

    /**
     */
    private static final Format TIME_FORMAT = new SimpleDateFormat("HH:mm");

    /**
     */
    private static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 20);

    /**
     */
    private static final Font FONT_TOTAL = new Font("Arial", Font.BOLD, 20);

    /**
     */
    private final CinemaReservationMap panel;

    /**
     * @param panel
     */
    public CartPanel(final CinemaReservationMap panel) {
        super();

        LOGGER.info("CartPanel");

        this.panel = panel;
    }

    /**
     * @param bookings
     */
    public void updateCart(final List<Booking> bookings) {
        this.removeAll();

        if (bookings.isEmpty()) {
            return;
        }

        final Event event = bookings.get(0).getEvent();

        // cart

        final JLabel title = new JLabel("CART");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(FONT_TITLE);
        title.setForeground(Color.white);

        // date

        final JLabel dateLabel = new JLabel("Date");
        dateLabel.setForeground(Color.white);
        dateLabel.setPreferredSize(new Dimension(80, 20));

        final JLabel dateText =
            new JLabel(DATE_FORMAT.format(event.getDateBegin()));
        dateText.setForeground(Color.white);

        // title

        final JLabel titleLabel = new JLabel("Title");
        titleLabel.setForeground(Color.white);

        final JLabel titleText =
            new JLabel("<html><p>" + event.getName() + "</p></html>");
        titleText.setForeground(Color.white);

        // time

        final JLabel timeLabel = new JLabel("Time");
        timeLabel.setForeground(Color.white);

        final JLabel timeText =
            new JLabel(TIME_FORMAT.format(event.getDateBegin()));
        timeText.setForeground(Color.white);

        // seats

        final JLabel seatsLabel = new JLabel("Seats");
        seatsLabel.setForeground(Color.white);

        final JPanel mainPanel = new JPanel();
        mainPanel.setBackground(CinemaReservationMap.COLOR_BACKGROUND);
        mainPanel.setLayout(new GridLayout2(10 + (bookings.size() * 2), 2));
        mainPanel.setPreferredSize(new Dimension(275, 380));

        mainPanel.add(new JLabel());
        mainPanel.add(new JLabel());

        mainPanel.add(dateLabel);
        mainPanel.add(dateText);

        mainPanel.add(titleLabel);
        mainPanel.add(titleText);

        mainPanel.add(timeLabel);
        mainPanel.add(timeText);

        mainPanel.add(new JLabel());
        mainPanel.add(new JLabel());

        mainPanel.add(seatsLabel);
        mainPanel.add(new JLabel());

        mainPanel.add(new JLabel());
        mainPanel.add(new JLabel());

        // seats

        double total = 0;
        for (final Booking booking : bookings) {
            final JLabel label =
                new JLabel(booking.getSeatCoordinates().toUpperCase());
            label.setForeground(Color.white);

            final JLabel text =
                new JLabel(PRICE_FORMAT.format(booking.getPrice()) + "     "
                    + booking.getPriceType().getAbbreviation());
            text.setForeground(Color.white);

            mainPanel.add(label);
            mainPanel.add(text);

            total += booking.getPrice();
        }

        mainPanel.add(new JLabel());
        mainPanel.add(new JLabel());

        // total

        final JLabel totalLabel = new JLabel("Total");
        totalLabel.setFont(FONT_TOTAL);
        totalLabel.setForeground(Color.white);

        final JLabel totalText = new JLabel(PRICE_FORMAT.format(total));
        totalText.setFont(FONT_TOTAL);
        totalText.setForeground(Color.white);

        mainPanel.add(totalLabel);
        mainPanel.add(totalText);

        // button

        final ActionListener reserveAl = new CartReserveAl(this.panel);

        final JButton reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(reserveAl);

        final ActionListener addToReceiptAl =
            new CartAddToReceiptAl(this.panel);

        final JButton addToReceiptButton = new JButton("Add to Receipt");
        addToReceiptButton.addActionListener(addToReceiptAl);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        buttonPanel.add(reserveButton);
        buttonPanel.add(addToReceiptButton);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(mainPanel);
        this.add(buttonPanel);

        this.revalidate();
        this.repaint();
    }
}
