package com.openbravo.pos.sales.cinema;

import com.openbravo.pos.sales.cinema.listener.BookingsDatabaseAl;
import com.openbravo.pos.sales.cinema.listener.CancelTicketAl;
import com.openbravo.pos.sales.cinema.listener.CinemaReservationMapAl;
import com.openbravo.pos.sales.cinema.listener.CustomerFinderAl;
import com.openbravo.pos.sales.cinema.listener.MembershipAl;
import com.openbravo.pos.sales.cinema.listener.OldMembershipAl;

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

        // CUSTOMER

        final JButton customer = new JButton();
        customer.setIcon(new ImageIcon(this.getClass().getResource(
            "/com/openbravo/images/kuser.png")));
        customer.setFocusPainted(false);
        customer.setFocusable(false);
        customer.setMargin(new Insets(15, 18, 15, 18));
        customer.setRequestFocusEnabled(false);
        customer.addActionListener(new CustomerFinderAl(panel));

        // MEMBERSHIP

        final JButton membership = new JButton();
        membership.setIcon(new ImageIcon(this.getClass().getResource(
            "/com/openbravo/images/colorize.png")));
        membership.setFocusPainted(false);
        membership.setFocusable(false);
        membership.setMargin(new Insets(15, 17, 15, 17));
        membership.setRequestFocusEnabled(false);
        membership.addActionListener(new MembershipAl(panel));

        // OLDMEMBERSHIP

        final JButton oldMembership = new JButton();
        oldMembership.setIcon(new ImageIcon(this.getClass().getResource(
            "/com/openbravo/images/contents.png")));
        oldMembership.setFocusPainted(false);
        oldMembership.setFocusable(false);
        oldMembership.setMargin(new Insets(12, 14, 12, 14));
        oldMembership.setRequestFocusEnabled(false);
        oldMembership.addActionListener(new OldMembershipAl(panel));

        this.setSize(700, 100);

        this.add(map);
        this.add(bookings);
        this.add(delete);
        this.add(customer);
        this.add(membership);
        this.add(oldMembership);
    }
}
