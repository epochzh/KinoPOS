package com.openbravo.pos.sales.cinema;

import com.openbravo.pos.sales.cinema.listener.BookedTicketDetailsAl;
import com.openbravo.pos.sales.cinema.listener.BookingAddToCartAl;
import com.openbravo.pos.sales.cinema.listener.BookingAddToTicketAl;
import com.openbravo.pos.sales.cinema.listener.BookingCancelAl;
import com.openbravo.pos.sales.cinema.listener.BookingCancelLockedAl;
import com.openbravo.pos.sales.cinema.model.Booking;
import com.openbravo.pos.sales.cinema.model.BookingState;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

/**
 */
public class BookingPopup extends JDialog {

    /**
	 */
    private static final long serialVersionUID = 8769824653070686584L;

    /**
     */
    private static final Border BORDER = BorderFactory.createEmptyBorder(7, 13,
        7, 13);

    /**
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
        "MMMM yyyy");

    /**
     */
    private static final Font FONT = new Font("Arial", 1, 16);

    /**
     */
    private static final Font FONT_STATUS = new Font("Arial", 1, 35);

    /**
     */
    private static final Icon ICON_PRINT = new ImageIcon(BookingPopup.class
        .getResource("/com/openbravo/images/fileprint.png"));

    /**
     */
    private static final int POPUP_HEIGHT = 400;

    /**
     */
    private static final int POPUP_WIDTH = 380;

    /**
     * @param parent
     * @return a new BookedTicketDetailsPopup
     */
    public static BookingPopup getPopup(final Component parent) {
        final Window window = CinemaReservationMap.getWindow(parent);

        BookingPopup popup;
        if (window instanceof Dialog) {
            popup = new BookingPopup((Dialog) window, true);
        } else {
            popup = new BookingPopup((Frame) window, true);
        }
        popup.setLocationRelativeTo(parent);

        return popup;
    }

    /**
     */
    private boolean addToTicket;

    /**
     */
    private Booking booking;

    /**
     */
    private CinemaReservationMap panel;

    /**
     * @param parent
     * @param modal
     */
    private BookingPopup(final Dialog parent, final boolean modal) {
        super(parent, modal);
    }

    /**
     * @param parent
     * @param modal
     */
    private BookingPopup(final Frame parent, final boolean modal) {
        super(parent, modal);
    }

    /**
     */
    public void addToCart() {
        this.panel.addToCart(this.booking);
        this.dispose();
    }

    /**
     */
    public void addToReceipt() {
        this.panel.addToReceipt(Arrays.asList(this.booking));
        this.dispose();
    }

    /**
     */
    public void cancel() {
        this.panel.cancelBooking(this.booking);
        this.dispose();
    }

    /**
     */
    public void removeLocked() {
    	this.panel.removeFromCart(this.booking);
        this.panel.cancelBooking(this.booking);
        this.dispose();
    }

    /**
	 */
    public void init() {
        // seatNumber

        final JLabel seatNumberLabel = new JLabel("Seat number");
        seatNumberLabel.setFont(FONT);

        final JLabel seatNumber =
            new JLabel(this.booking.getSeatCoordinates().toUpperCase());
        seatNumber.setFont(FONT);

        // seatState

        final JLabel seatStateLabel = new JLabel("Seat state");
        seatStateLabel.setFont(FONT);

        final JLabel seatState = new JLabel(this.booking.getState().name());
        seatState.setFont(FONT);

        // customerId

        final JLabel customerIdLabel = new JLabel("Customer ID");
        customerIdLabel.setFont(FONT);

        final JLabel customerId = new JLabel();
        if (this.booking.getCustomer() == null) {
            // Nothing to do.
        } else {
            customerId.setFont(FONT);
            customerId.setText("#" + this.booking.getCustomer().getId());
        }

        // customerName

        final JLabel customerNameLabel = new JLabel("Customer name");
        customerNameLabel.setFont(FONT);

        final JLabel customerName = new JLabel(this.booking.getCustomerName());
        customerName.setFont(FONT);

        // purchaseMethod

        final JLabel purchaseMethodLabel = new JLabel("Purchase method");
        purchaseMethodLabel.setFont(FONT);

        final JLabel purchaseMethod =
            new JLabel(this.booking.getPriceType().name());
        purchaseMethod.setFont(FONT);

        // purchaseDate

        @SuppressWarnings("deprecation")
        final String date =
            CinemaReservationMap.getDayOfMonthSuffix(this.booking.getDate()
                .getDate())
                + " " + DATE_FORMAT.format(this.booking.getDate());

        final JLabel purchaseDateLabel = new JLabel("Purchase date");
        purchaseDateLabel.setFont(FONT);

        final JLabel purchaseDate = new JLabel(date);
        purchaseDate.setFont(FONT);

        // bookingPanel

        final int rows;
        if (this.booking.getState() == BookingState.RESERVED) {
            rows = 7;
        } else {
            rows = 6;
        }

        final JPanel bookingPanel = new JPanel();
        bookingPanel.setBorder(BORDER);
        bookingPanel.setLayout(new GridLayout(rows, 2));

        bookingPanel.add(seatNumberLabel);
        bookingPanel.add(seatNumber);
        bookingPanel.add(seatStateLabel);
        bookingPanel.add(seatState);
        bookingPanel.add(customerIdLabel);
        bookingPanel.add(customerId);
        bookingPanel.add(customerNameLabel);
        bookingPanel.add(customerName);
        bookingPanel.add(purchaseMethodLabel);
        bookingPanel.add(purchaseMethod);
        bookingPanel.add(purchaseDateLabel);
        bookingPanel.add(purchaseDate);

        // price

        if (this.booking.getState() == BookingState.RESERVED) {
            final JLabel priceLabel = new JLabel("Price");
            priceLabel.setFont(FONT);

            final JLabel price = new JLabel("\u00A3" + this.booking.getPrice());
            price.setFont(FONT);

            bookingPanel.add(priceLabel);
            bookingPanel.add(price);
        }

        // status

        final String status;
        if (this.booking.getState() == BookingState.RESERVED) {
            status = BookingState.RESERVED.name();
        } else {
            status =
                this.booking.getCollected() ? "Collected" : "Not Collected";
        }

        // buttonPanel

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BORDER);
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 20));

        if (this.booking.getState() == BookingState.RESERVED) {
            // cancelButton

            final BookingCancelAl cancelAl = new BookingCancelAl(this);

            final JButton cancelButton = new JButton();
            cancelButton.addActionListener(cancelAl);
            cancelButton.setBackground(new Color(1, 47, 204));
            cancelButton.setFocusable(false);
            cancelButton.setFocusPainted(false);
            cancelButton.setForeground(Color.white);
            cancelButton.setPreferredSize(new Dimension(40, 30));
            cancelButton.setRequestFocusEnabled(false);
            cancelButton.setText("Cancel");

            // addToCart

            final ActionListener addToCartAl;
            if (this.addToTicket) {
                addToCartAl = new BookingAddToTicketAl(this);
            } else {
                addToCartAl = new BookingAddToCartAl(this);
            }

            final JButton addToCartButton = new JButton();
            addToCartButton.addActionListener(addToCartAl);
            addToCartButton.setBackground(new Color(1, 47, 204));
            addToCartButton.setFocusable(false);
            addToCartButton.setFocusPainted(false);
            addToCartButton.setForeground(Color.white);
            addToCartButton.setRequestFocusEnabled(false);
            addToCartButton.setText("Add to Cart");

            buttonPanel.add(cancelButton);
            buttonPanel.add(addToCartButton);
        } else if(this.booking.getState() == BookingState.LOCKED){
        	// TODO check if the seat was locked at the current venue
        	final BookingCancelLockedAl cancelLocked = new BookingCancelLockedAl(this);

            final JButton printButton = new JButton();
            printButton.addActionListener(cancelLocked);
            printButton.setBackground(new Color(1, 47, 204));
            printButton.setFocusable(false);
            printButton.setFocusPainted(false);
            printButton.setForeground(Color.white);
            printButton.setMargin(new Insets(10, 20, 10, 20));
            printButton.setRequestFocusEnabled(false);
            printButton.setText("Remove");

            buttonPanel.add(new JLabel());
            buttonPanel.add(printButton);
        }else {
            // printButton

            final ActionListener listener = new BookedTicketDetailsAl(this);

            final JButton printButton = new JButton();
            printButton.addActionListener(listener);
            printButton.setBackground(new Color(1, 47, 204));
            printButton.setFocusable(false);
            printButton.setFocusPainted(false);
            printButton.setForeground(Color.white);
            printButton.setIcon(ICON_PRINT);
            printButton.setMargin(new Insets(10, 20, 10, 20));
            printButton.setRequestFocusEnabled(false);
            printButton.setText("Print");

            buttonPanel.add(new JLabel());
            buttonPanel.add(printButton);
        }

        // statusPanel

        final JLabel statusLabel = new JLabel(status);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(FONT_STATUS);
        statusLabel.setForeground(new Color(154, 0, 0));

        // mainPanel

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(bookingPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(statusLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        final Point centerPoint =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();

        this.setBounds(centerPoint.x - (POPUP_WIDTH / 2), centerPoint.y
            - (POPUP_HEIGHT / 2), POPUP_WIDTH, POPUP_HEIGHT);
        this.getContentPane().add(mainPanel);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setResizable(false);
        this.setTitle("#" + this.booking.getId());
    }

    /**
     */
    public void print() {
        PrintableDocument.printComponent(this);
        this.dispose();
    }

    /**
     * @return the addToTicket
     */
    public boolean isAddToTicket() {
        return this.addToTicket;
    }

    /**
     * @param addToTicket the addToTicket to set
     */
    public void setAddToTicket(final boolean addToTicket) {
        this.addToTicket = addToTicket;
    }

    /**
     * @return the booking
     */
    public Booking getBooking() {
        return this.booking;
    }

    /**
     * @param booking the booking to set
     */
    public void setBooking(final Booking booking) {
        this.booking = booking;
    }

    /**
     * @return the panel
     */
    public CinemaReservationMap getPanel() {
        return this.panel;
    }

    /**
     * @param panel the panel to set
     */
    public void setPanel(final CinemaReservationMap panel) {
        this.panel = panel;
    }
}
