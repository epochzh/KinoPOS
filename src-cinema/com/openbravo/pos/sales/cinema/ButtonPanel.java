package com.openbravo.pos.sales.cinema;

import com.openbravo.pos.sales.cinema.listener.BookingsDatabaseAl;
import com.openbravo.pos.sales.cinema.listener.CancelTicketAl;
import com.openbravo.pos.sales.cinema.listener.CinemaReservationMapAl;

import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 */
public class ButtonPanel extends JPanel {

    /**
	 */
    private static final long serialVersionUID = 4455753454080990706L;

    /**
     */
    private static final Icon ICON_BOOKINGS = new ImageIcon(ButtonPanel.class
        .getResource("/com/openbravo/images/unsortedList.png"));

    /**
     */
    private static final Icon ICON_DELETE = new ImageIcon(ButtonPanel.class
        .getResource("/com/openbravo/images/editdelete.png"));

    /**
     */
    private static final Icon ICON_MAP = new ImageIcon(ButtonPanel.class
        .getResource("/com/openbravo/images/atlantikdesignersmall.png"));

    /**
     */
    private final CinemaReservationMap panel;

    /**
     * @param panel
     */
    public ButtonPanel(final CinemaReservationMap panel) {
        super();

        this.panel = panel;

        // MAP

        final CinemaReservationMapAl mapListener =
            new CinemaReservationMapAl(this.panel);

        final JButton map = new JButton();
        map.addActionListener(mapListener);
        map.setFocusPainted(false);
        map.setFocusable(false);
        map.setIcon(ICON_MAP);
        map.setMargin(new Insets(15, 18, 15, 18));
        map.setRequestFocusEnabled(false);

        // BOOKINGS

        final JButton bookings = new JButton();
        bookings.addActionListener(new BookingsDatabaseAl(this.panel));
        bookings.setFocusPainted(false);
        bookings.setFocusable(false);
        bookings.setIcon(ICON_BOOKINGS);
        bookings.setMargin(new Insets(15, 18, 15, 18));
        bookings.setRequestFocusEnabled(false);

        // DELETE

        final JButton delete = new JButton();
        delete.addActionListener(new CancelTicketAl(this.panel));
        delete.setFocusPainted(false);
        delete.setFocusable(false);
        delete.setIcon(ICON_DELETE);
        delete.setMargin(new Insets(15, 18, 15, 18));
        delete.setRequestFocusEnabled(false);

        this.setSize(700, 100);

        this.add(map);
        this.add(bookings);
        this.add(delete);
    }
}
