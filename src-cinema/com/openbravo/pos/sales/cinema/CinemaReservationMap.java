package com.openbravo.pos.sales.cinema;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.sales.JPanelTicket;
import com.openbravo.pos.sales.JTicketsBag;
import com.openbravo.pos.sales.TicketsEditor;
import com.openbravo.pos.sales.cinema.listener.CancelReservedAl;
import com.openbravo.pos.sales.cinema.listener.CartCancelAl;
import com.openbravo.pos.sales.cinema.listener.DateCbAl;
import com.openbravo.pos.sales.cinema.listener.MovieCbAl;
import com.openbravo.pos.sales.cinema.listener.SeatButtonAl;
import com.openbravo.pos.sales.cinema.listener.TimeCbAl;
import com.openbravo.pos.sales.cinema.model.Booking;
import com.openbravo.pos.sales.cinema.model.BookingState;
import com.openbravo.pos.sales.cinema.model.Customer;
import com.openbravo.pos.sales.cinema.model.Event;
import com.openbravo.pos.sales.cinema.model.MembershipType;
import com.openbravo.pos.sales.cinema.model.PriceMatrix;
import com.openbravo.pos.sales.cinema.model.PriceSpecial;
import com.openbravo.pos.sales.cinema.model.PriceType;
import com.openbravo.pos.sales.cinema.model.Screen;
import com.openbravo.pos.sales.cinema.model.Seat;
import com.openbravo.pos.sales.cinema.model.SeatState;
import com.openbravo.pos.sales.cinema.model.Venue;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 */
public class CinemaReservationMap extends JTicketsBag {

    /**
     */
    private static final long serialVersionUID = -2612013319133912992L;

    /**
     */
    private static final Logger LOGGER = Logger
        .getLogger(CinemaReservationMap.class.getName());

    /**
     */
    static final Color COLOR_BACKGROUND = new Color(93, 93, 93);

    /**
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
        "yyyy-MM-dd");

    /**
     */
    private static final DateFormat DATE_BEGIN_FORMAT = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm");

    /**
     */
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    /**
     */
    private static final Font FONT_COMBOBOX = new Font("Arial", Font.BOLD, 22);

    /**
     */
    private static final Font FONT_SEAT = new Font("Arial", Font.BOLD, 12);

    /**
     */
    private static final Icon ICON_CANCEL = new ImageIcon(
        CinemaReservationMap.class
            .getResource("/com/openbravo/images/button_cancel.png"));

    /**
     * @param day
     * @return the suffix of the specified day of the month
     */
    public static String getDayOfMonthSuffix(final int day) {
        if ((day >= 11) && (day <= 13)) {
            return day + "th";
        }
        switch (day % 10) {
            case 1:
                return day + "st";
            case 2:
                return day + "nd";
            case 3:
                return day + "rd";
            default:
                return day + "th";
        }
    }

    /**
     * @param component
     * @return the {@link Window} containing the specified {@link Component}
     */
    public static Window getWindow(final Component component) {
        if (component == null) {
            return new JFrame();
        } else if ((component instanceof Frame)
            || (component instanceof Dialog)) {
            return (Window) component;
        } else {
            return getWindow(component.getParent());
        }
    }

    /**
     * @param booking
     * @return a {@link TicketLineInfo} generated from the specified
     * {@link Booking}
     */
    private static TicketLineInfo toLine(final Booking booking) {
        // final String productid = String.valueOf(booking.getEvent().getId());
        final String productname = booking.getEvent().getName();
        final String producttaxcategory = "000";
        final double dMultiply = 1.0;
        final double dPrice = booking.getPrice();
        final TaxInfo tax = null;

        final TicketLineInfo line =
            new TicketLineInfo(productname, producttaxcategory, dMultiply,
                dPrice, tax);
        // final TicketLineInfo line =
        // new TicketLineInfo(productid, productname, producttaxcategory,
        // dMultiply, dPrice, tax);

        return line;
    }

    /**
     * @param seat
     * @return a {@link Color} generated from the specified {@link Seat}
     */
    private static Color toColor(final Seat seat) {
        final Color seatColor;

        if (seat.getBooking() == null) {
            if (seat.getState() == SeatState.WHEELCHAIR) {
                seatColor = ColorPanel.COLOR_REMOVABLE;
            } else {
                seatColor = ColorPanel.COLOR_AVAILABLE;
            }
        } else {
            final BookingState bookingState = seat.getBooking().getState();
            if (bookingState == BookingState.LOCKED) {
                seatColor = ColorPanel.COLOR_LOCKED;
            } else if (bookingState == BookingState.RESERVED) {
                seatColor = ColorPanel.COLOR_RESERVED;
            } else if (bookingState == BookingState.TAKEN) {
                seatColor = ColorPanel.COLOR_TAKEN;
            } else {
                throw new IllegalArgumentException("bookingState: "
                    + bookingState);
            }
        }

        return seatColor;
    }

    /**
     * @param matrix
     * @param priceType
     * @return the price
     */
    private static Double toPrice(final PriceMatrix matrix,
    final PriceType priceType) {
        Double price;

        if (priceType == null) {
            price = matrix.getPriceFull();
        } else if (priceType == PriceType.FULL_PRICE) {
            price = matrix.getPriceFull();
        } else if (priceType == PriceType.GOLD) {
            price = matrix.getPriceGold();
        } else if (priceType == PriceType.KINO_FRIENDS) {
            price = matrix.getPriceKinoFriends();
        } else if (priceType == PriceType.KINO_STAFF) {
            price = matrix.getPriceKinoStaff();
        } else if (priceType == PriceType.SENIOR) {
            price = matrix.getPriceSenior();
        } else if (priceType == PriceType.SILVER) {
            price = matrix.getPriceSilver();
        } else if (priceType == PriceType.STUDENT) {
            price = matrix.getPriceStudent();
        } else if (priceType == PriceType.U16) {
            price = matrix.getPriceU16();
        } else {
            throw new IllegalArgumentException("priceType: " + priceType);
        }

        return price;
    }

    /**
     */
    private final CinemaDaoImpl dao;

    /**
     */
    private final Venue venue;

    /**
     */
    private List<Booking> bookingsCart;

    /**
     */
    private List<Booking> bookingsTicket;

    /**
     */
    private Customer customer;

    /**
     */
    private Event event;

    /**
     */
    private PriceType priceType;

    /**
     */
    private JLabel ticketStatusLabel;

    /**
     */
    private ButtonPanel buttonPanel;

    /**
     */
    private JScrollPane seatScrollPane;

    /**
     */
    private CartPanel cartPanel;

    /**
     */
    private JComboBox dateCb;

    /**
     */
    private JComboBox movieCb;

    /**
     */
    private ActionListener movieCbAl;

    /**
     */
    private JComboBox timeCb;

    /**
     */
    private ActionListener timeCbAl;

    /**
     * @param app
     * @param panelticket
     */
    public CinemaReservationMap(final AppView app,
    final TicketsEditor panelticket) {
        super(app, panelticket);

        LOGGER.info("CinemaReservationMap");

        this.dao =
            (CinemaDaoImpl) app
                .getBean("com.openbravo.pos.sales.cinema.CinemaDaoImpl");

        final String venueName =
            super.m_App.getProperties().getProperty("machine.venueName");
        try {
            this.venue = this.dao.getVenue(venueName);
        } catch (final BasicException ex) {
            throw new RuntimeException("Invalid venue name: " + venueName);
        }

        this.initComponents();

        this.dateCb.setSelectedIndex(0);
    }

    /**
     * @see com.openbravo.pos.sales.JTicketsBag#activate()
     */
    @Override
    public void activate() {
        LOGGER.info("activate");

        this.bookingsCart = new ArrayList<Booking>();
        this.bookingsTicket = new ArrayList<Booking>();
        this.customer = null;
        this.event = null;
        this.priceType = null;

        this.buttonPanel.enablePriceType();

        final TicketInfo ticket = new TicketInfo();
        this.m_panelticket.setActiveTicket(ticket, null);
    }

    /**
     * @see com.openbravo.pos.sales.JTicketsBag#deactivate()
     */
    @Override
    public boolean deactivate() {
        LOGGER.info("deactivate");

        this.m_panelticket.setActiveTicket(null, null);

        return true;
    }

    /**
     * @see com.openbravo.pos.sales.JTicketsBag#deleteTicket()
     */
    @Override
    public void deleteTicket() {
        LOGGER.info("deleteTicket");

        final TicketInfo ticket = super.m_panelticket.getActiveTicket();
        // TODO: Is this a correct test ? Should we use "ticket.save" event ?
        if (ticket.getDate() != null) {
            for (final Booking booking : this.bookingsTicket) {
                booking.setState(BookingState.TAKEN);
            }

            try {
                this.dao.updateBooking(this.bookingsTicket);
            } catch (final BasicException ex) {
                new MessageInf(ex).show(this);
                return;
            }
        }

        // Reinitialize the map screen and redirect to the ticket screen.
        this.activate();
    }

    /**
     * @see com.openbravo.pos.sales.JTicketsBag#getBagComponent()
     */
    @Override
    protected JComponent getBagComponent() {
        LOGGER.info("getBagComponent");

        this.buttonPanel = new ButtonPanel(this);

        return this.buttonPanel;
    }

    /**
     * @see com.openbravo.pos.sales.JTicketsBag#getNullComponent()
     */
    @Override
    protected JComponent getNullComponent() {
        LOGGER.info("getNullComponent");

        return this;
    }

    /**
     */
    public void doCustomerSearch() {
        final CustomerPopup popup =
            CustomerPopup.getCustomerPopup(this.dao, this);
        popup.setVisible(true);

        this.customer = popup.getSelectedCustomer();

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("customer: " + this.customer);
        }

        if (this.customer == null) {
            // Nothing to do.
        } else if (this.customer.getMsType() == MembershipType.GOLD) {
            this.priceType = PriceType.GOLD;
        } else if (this.customer.getMsType() == MembershipType.SILVER) {
            this.priceType = PriceType.SILVER;
        }
    }

    /**
     */
    public void doMembershipAdd() {
        final MemberPopup popup = MemberPopup.getMemberPopup(this.dao, this);
        popup.setVisible(true);

        // this.customer = popup.getSelectedCustomer();
        //
        // if (LOGGER.isLoggable(Level.INFO)) {
        // LOGGER.info("customer: " + this.customer);
        // }
        //
        // if (this.customer == null) {
        // // Nothing to do.
        // } else if (this.customer.getMsType() == MembershipType.GOLD) {
        // this.priceType = PriceType.GOLD;
        // } else if (this.customer.getMsType() == MembershipType.SILVER) {
        // this.priceType = PriceType.SILVER;
        // }
    }

    /**
     */
    public void onDateAction() {
        this.movieCb.removeAllItems();

        final Date date;
        final List<Event> events;
        try {
            date = DATE_FORMAT.parse(this.dateCb.getSelectedItem().toString());
            events = this.dao.listEvent(this.venue, date);
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
            return;
        } catch (final ParseException ex) {
            new MessageInf(ex).show(this);
            return;
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("events: " + events.size());
        }

        // Reinitialize the movie combo box.

        this.movieCb.removeActionListener(this.movieCbAl);

        final Set<String> names = new TreeSet<String>();
        for (final Event ev : events) {
            final String name = ev.getName();
            if (names.contains(name)) {
                continue;
            }

            this.movieCb.addItem(name);
            names.add(name);
        }

        this.movieCb.addActionListener(this.movieCbAl);

        if (events.size() > 0) {
            final Event ev = CinemaDaoImpl.getNextAvailableFilm(events, date);
            this.movieCb.setSelectedItem(ev.getName());

            final String status = this.getStatus(ev);
            this.ticketStatusLabel.setText(status);
        } else {
            this.movieCb.setSelectedIndex(-1);
        }
    }

    /**
     */
    public void onMovieAction() {
        LOGGER.info("onMovieAction: " + this.movieCb.getSelectedIndex());

        this.timeCb.removeAllItems();

        if (this.movieCb.getSelectedIndex() == -1) {
            // this.timeCB.setSelectedIndex(-1);

            return;
        }

        final String name = (String) this.movieCb.getSelectedItem();

        final Date date;
        final List<Event> events;
        try {
            date = DATE_FORMAT.parse(this.dateCb.getSelectedItem().toString());
            events = this.dao.listEvent(this.venue, name, date);
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
            return;
        } catch (final ParseException ex) {
            new MessageInf(ex).show(this);
            return;
        }

        // Reinitialize the time combo box.

        this.timeCb.removeActionListener(this.timeCbAl);

        for (final Event showing : events) {
            this.timeCb.addItem(TIME_FORMAT.format(showing.getDateBegin()));
        }

        this.timeCb.addActionListener(this.timeCbAl);

        if (events.size() > 0) {
            final Event ev = CinemaDaoImpl.getNextAvailableFilm(events, date);
            this.timeCb.setSelectedIndex(events.indexOf(ev));
            final String status = this.getStatus(ev);
            this.ticketStatusLabel.setText(status);
        } else {
            this.timeCb.setSelectedIndex(-1);
        }
    }

    /**
     */
    public void onTimeAction() {
        LOGGER.info("onTimeAction: " + this.timeCb.getSelectedIndex());

        if (this.timeCb.getSelectedIndex() == -1) {
            this.event = null;
            this.seatScrollPane.setVisible(false);

            return;
        }

        final Screen screen;
        try {
            final Date datebegin =
                DATE_BEGIN_FORMAT.parse(this.dateCb.getSelectedItem() + " "
                    + this.timeCb.getSelectedItem());
            this.event =
                this.dao.getEvent(this.venue, (String) this.movieCb
                    .getSelectedItem(), datebegin);
            screen =
                this.dao.getScreenByNumber(this.venue, this.event.getScreen()
                    .getNumber());
            this.event.setScreen(screen);
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
            return;
        } catch (final ParseException ex) {
            new MessageInf(ex).show(this);
            return;
        }

        LOGGER.info("eventId: " + this.event.getId() + ", screenId: "
            + screen.getId() + ", eventScreenId: "
            + this.event.getScreen().getId());

        final List<Seat> seats;
        try {
            seats = this.dao.listSeat(this.event);
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
            return;
        }

        LOGGER.info("seats: " + seats.size());

        final Map<String, Seat> map = new Hashtable<String, Seat>(seats.size());
        for (final Seat seat : seats) {
            final String key =
                String.valueOf(seat.getRow()).toUpperCase()
                    + String.valueOf(seat.getColumn());
            map.put(key, seat);
        }

        final byte rows = this.event.getScreen().getNbOfRows();
        final byte cols = this.event.getScreen().getNbOfCols();

        LOGGER.info("rows: " + rows + ", cols: " + cols);

        final JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(rows, cols));
        seatPanel.setBackground(COLOR_BACKGROUND);

        for (int i = 0; i < rows; ++i) {
            final char row = (char) (i + 65);

            for (int j = 1; j <= cols; ++j) {
                final String key = row + String.valueOf(j);
                final Seat seat = map.get(key);
                seatPanel.add(this.toComponent(seat));
            }
        }

        this.enableDisableComboBoxes();

        this.seatScrollPane.setViewportView(seatPanel);
        this.seatScrollPane.setVisible(true);
    }

    /**
     * @param seat
     */
    public void onSeatAction(final Seat seat) {
        LOGGER.info("seat: " + seat);

        Booking booking = seat.getBooking();
        if (booking == null) {
            if (this.customer == null) {
                final CustomerNamePopup popup =
                    new CustomerNamePopup((Frame) getWindow(this));
                popup.setVisible(true);

                this.customer = new Customer();
                this.customer.setLastName(popup.getCustomerName());
                this.customer.setPhoneNumber(popup.getCustomerPhone());
            }

            final String barcode;
            final boolean isFirstFilmApplicable;
            boolean isSpecialEvent = false;
            boolean isDoubleBillApplicable = false;
            final List<PriceMatrix> priceMatrixes;
            final PriceSpecial priceSpecial;
            final PriceMatrix specialEventPrice;
            try {
                barcode = String.valueOf(this.dao.getBookingBarcodeMax() + 1);
                isFirstFilmApplicable =
                    this.dao.isFirstFilmApplicable(this.venue, this.event);
                LOGGER.info("isFirstFilmApplicable: " + isFirstFilmApplicable);
                final String type = this.event.getTypeAsString();
                // checks to see if the event is marked as a special event

                if (type.contains("event")) {
                    isSpecialEvent = true;
                    specialEventPrice =
                        this.dao.getSpecialEventPrice(this.venue, type);
                    LOGGER.info("specialEventPrice: "
                        + specialEventPrice.getPriceGold());
                } else {
                    isSpecialEvent = false;
                    specialEventPrice = null;
                }

                LOGGER.info("isFirstFilmApplicable: " + isFirstFilmApplicable);
                if ((this.priceType != null)
                    && (this.priceType.getType() == "double_bill")) {
                    isDoubleBillApplicable = true;
                }
                priceMatrixes = this.dao.listPrice(this.venue, this.event);
                priceSpecial = this.dao.getPriceFirstFilm(this.venue);
            } catch (final BasicException ex) {
                new MessageInf(ex).show(this);
                return;
            }

            Double price = null;
            PriceMatrix priceMatrix = null;
            if (isSpecialEvent) {
                LOGGER.info("specialEventPrice inside if else: "
                    + specialEventPrice.getPriceGold());
                priceMatrix = specialEventPrice;
                price = toPrice(priceMatrix, this.priceType);
            } else if (isDoubleBillApplicable) {
                priceMatrix = priceMatrixes.get(0);
                try {
                    price = this.dao.getDoubleBillPrice(this.venue);
                } catch (final BasicException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                LOGGER.info("double bill price: " + price);
            } else if (isFirstFilmApplicable) {
                priceMatrix = priceMatrixes.get(0);
                price = priceSpecial.getPrice();
                LOGGER.info("firstfilmprice inside if else: " + price);
            } else if (priceMatrixes.size() == 1) {
                priceMatrix = priceMatrixes.get(0);
                price = toPrice(priceMatrix, this.priceType);
            } else {
                final int nb = this.bookingsCart.size() + 1;
                for (final PriceMatrix pm : priceMatrixes) {
                    final boolean even =
                        ((nb % 2) == 0) && (pm.getTicketsForOffer() == 2);
                    final boolean odd =
                        ((nb % 2) == 1) && (pm.getTicketsForOffer() == 1);
                    if (even || odd) {
                        priceMatrix = pm;
                        price = toPrice(priceMatrix, this.priceType);
                        break;
                    }
                }
            }

            booking = new Booking();
            booking.setBarcode(barcode);
            booking.setCollected(false);
            booking.setCustomer(this.customer);
            booking.setCustomerName(this.customer.getLastName());
            booking.setCustomerPhone(this.customer.getPhoneNumber());
            booking.setDate(new Date());
            booking.setEvent(this.event);
            booking.setPrice(price);
            booking.setPriceMatrix(priceMatrix);
            booking.setPriceType(PriceType.FULL_PRICE);
            booking.setSalesChannel(this.venue.getName());
            booking.setSeatCoordinates(seat.getRow() + "" + seat.getColumn());
            booking.setState(BookingState.LOCKED);

            try {
                this.dao.createBooking(booking);

                booking.setId(this.dao.getBookingByBarcode(barcode).getId());
            } catch (final BasicException ex) {
                new MessageInf(ex).show(this);
                return;
            }

            this.bookingsCart.add(booking);
            this.cartPanel.updateCart(this.bookingsCart);

            // Refresh the map.
            this.onTimeAction();

            return;
        }

        this.showBookingPopup(false, booking);
    }

    /**
     * @param priceType
     */
    public void onPriceTypeAction(
    @SuppressWarnings("hiding") final PriceType priceType) {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("priceType: " + priceType);
        }

        this.priceType = priceType;
    }

    /**
     * @param booking
     */
    public void addToCart(final Booking booking) {
        booking.setState(BookingState.LOCKED);
        try {
            this.dao.updateBooking(booking);
            booking.setEvent(this.event);

            if (booking.getCustomer() == null) {
                this.customer = new Customer();
                this.customer.setLastName(booking.getCustomerName());
                this.customer.setPhoneNumber(booking.getCustomerPhone());
            } else {
                this.customer =
                    this.dao.getCustomer(booking.getCustomer().getId());
            }
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
            return;
        }

        this.bookingsCart.add(booking);
        this.cartPanel.updateCart(this.bookingsCart);

        // Refresh the map.
        this.onTimeAction();
    }

    /**
     * @param booking
     */
    public void removeFromCart(final Booking booking) {
        this.bookingsCart.remove(booking);
        this.cartPanel.updateCart(this.bookingsCart);
        if (this.bookingsCart.isEmpty()) {
            // Redirect to the ticket screen.
            final TicketInfo ticket = super.m_panelticket.getActiveTicket();
            super.m_panelticket.setActiveTicket(ticket, null);
        }

        // Refresh the map.
        this.onTimeAction();
    }

    /**
     */
    public void addToReceipt() {
        if (this.bookingsCart.isEmpty()) {
            return;
        }

        this.addToReceipt(this.bookingsCart);
    }

    /**
     * @param bookings
     */
    public void addToReceipt(final List<Booking> bookings) {
        if (this.customer == null) {
            this.customer = bookings.get(0).getCustomer();
        }
        if (this.customer == null) {
            this.customer = new Customer();
            this.customer.setLastName(bookings.get(0).getCustomerName());
            this.customer.setPhoneNumber(bookings.get(0).getCustomerPhone());
        }

        final CustomerInfoExt customerInfoExt = new CustomerInfoExt(null);
        customerInfoExt.setName(this.customer.getLastName());
        customerInfoExt.setPhone(this.customer.getPhoneNumber());

        final TicketInfo ticket = super.m_panelticket.getActiveTicket();
        ticket.setCustomer(customerInfoExt);

        for (final Booking booking : bookings) {
            final TicketLineInfo line = toLine(booking);

            ticket.addLine(line);
        }

        this.bookingsTicket.addAll(this.bookingsCart);
        this.bookingsCart.clear();

        this.buttonPanel.disablePriceType();

        // Redirect to the ticket screen.
        super.m_panelticket.setActiveTicket(ticket, null);
    }

    /**
     * @param booking
     */
    public void cancelBooking(final Booking booking) {
        try {
            this.dao.deleteBooking(booking);
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
        }

        // Refresh the map.
        this.onTimeAction();
    }

    /**
     */
    public void cancelCart() {
        try {
            this.dao.deleteBooking(this.bookingsCart);
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
        }

        // Redirect to the ticket screen.
        final TicketInfo ticket = super.m_panelticket.getActiveTicket();
        super.m_panelticket.setActiveTicket(ticket, null);
    }

    /**
     */
    public void cancelReserved() {
        if (this.event == null) {
            return;
        }

        try {
            this.dao.deleteReserved(this.event);
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
            return;
        }

        // Refresh the map.
        this.onTimeAction();
    }

    /**
     */
    public void cancelTicket() {
        try {
            this.dao.deleteBooking(this.bookingsTicket);
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
        }

        // Reinitialize the map screen and redirect to the ticket screen.
        this.activate();
    }

    /**
     * @param booking
     */
    public void printBooking(final Booking booking) {
        // TODO: To complete.
        ((JPanelTicket) super.m_panelticket).printTicket("Printer.Ticket");
    }

    /**
     */
    public void reserveCart() {
        for (final Booking booking : this.bookingsCart) {
            booking.setState(BookingState.RESERVED);
        }

        try {
            this.dao.updateBooking(this.bookingsCart);
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
            return;
        }

        // Reinitialize the map screen and redirect to the ticket screen.
        this.activate();
    }

    /**
     */
    public void showBookingsDatabase() {
        final BookingsDatabasePopup popup =
            BookingsDatabasePopup.getPopup(this);
        popup.executeSearch();
        popup.setVisible(true);
    }

    /**
     * @param addToTicket
     * @param booking
     */
    public void showBookingPopup(final boolean addToTicket,
    final Booking booking) {
        final BookingState bookingState = booking.getState();
        if (bookingState == BookingState.LOCKED) {
            // Nothing to do.
        } else if (bookingState == BookingState.RESERVED) {
            final BookingPopup popup = BookingPopup.getPopup(this);
            popup.setAddToTicket(addToTicket);
            popup.setBooking(booking);
            popup.setPanel(this);
            popup.init();
            popup.setVisible(true);
        } else if (bookingState == BookingState.TAKEN) {
            final BookingPopup popup = BookingPopup.getPopup(this);
            popup.setAddToTicket(addToTicket);
            popup.setBooking(booking);
            popup.setPanel(this);
            popup.init();
            popup.setVisible(true);
        } else {
            throw new IllegalArgumentException("bookingState: " + bookingState);
        }
    }

    /**
     */
    public void showMap() {
        LOGGER.info("showMap");

        final JPanelTicket panel = (JPanelTicket) super.m_panelticket;
        final CardLayout cl = (CardLayout) panel.getLayout();
        cl.show(panel, "null");

        this.event = null;
        this.dateCb.setSelectedIndex(0);

        this.bookingsCart = new ArrayList<Booking>();
        this.cartPanel.updateCart(this.bookingsCart);

        this.enableDisableComboBoxes();
    }

    /**
     * @return the dao
     */
    public CinemaDaoImpl getDao() {
        return this.dao;
    }

    /**
     */
    private void initComboBoxes() {
        LOGGER.info("initComboBoxes");

        // dateCB

        final List<String> dateStrings = new ArrayList<String>(21);

        // TODO: Remove the static values.
        final List<Date> dates =
            this.dao.listDateThreeWeeks(new Date());
        for (final Date date : dates) {
            dateStrings.add(DATE_FORMAT.format(date));
        }

        final ActionListener dateCbAl = new DateCbAl(this);

        final ComboBoxValModel dateCbvm = new ComboBoxValModel(dateStrings);

        this.dateCb = new JComboBox();
        this.dateCb.addActionListener(dateCbAl);
        this.dateCb.setFont(FONT_COMBOBOX);
        this.dateCb.setModel(dateCbvm);

        // movieCB

        this.movieCbAl = new MovieCbAl(this);

        this.movieCb = new JComboBox();
        this.movieCb.setFont(FONT_COMBOBOX);

        // timeCB

        this.timeCbAl = new TimeCbAl(this);

        this.timeCb = new JComboBox();
        this.timeCb.setFont(FONT_COMBOBOX);
    }

    /**
     */
    private void initComponents() {
        LOGGER.info("initComponents");

        this.initComboBoxes();

        final JButton cancelButton = new JButton();
        cancelButton.addActionListener(new CartCancelAl(this));
        cancelButton.setFocusable(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setIcon(ICON_CANCEL);
        cancelButton.setMargin(new Insets(3, 10, 3, 10));
        cancelButton.setRequestFocusEnabled(false);

        final JButton cancelReservedButton = new JButton();
        cancelReservedButton.addActionListener(new CancelReservedAl(this));
        cancelReservedButton.setFocusable(false);
        cancelReservedButton.setFocusPainted(false);
        cancelReservedButton.setMargin(new Insets(6, 6, 6, 6));
        cancelReservedButton.setRequestFocusEnabled(false);
        cancelReservedButton.setText("Cancel Reserved");

        /*
         * TODO: needs the call back changing to show the ticket database for
         * this film
         */
        final JButton bookingsButton = new JButton();
        // ticketBookingsButton.addActionListener(new CancelReservedAl(this));
        bookingsButton.setFocusable(false);
        bookingsButton.setFocusPainted(false);
        bookingsButton.setMargin(new Insets(6, 6, 6, 6));
        bookingsButton.setRequestFocusEnabled(false);
        bookingsButton.setText("Bookings");

        this.ticketStatusLabel = new JLabel();

        final JPanel comboBoxesPanel = new JPanel();
        comboBoxesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        comboBoxesPanel.add(this.timeCb);
        comboBoxesPanel.add(this.movieCb);
        comboBoxesPanel.add(this.dateCb);
        comboBoxesPanel.add(cancelButton);
        comboBoxesPanel.add(cancelReservedButton);
        comboBoxesPanel.add(bookingsButton);
        comboBoxesPanel.add(this.ticketStatusLabel);

        // seatScrollPane

        this.seatScrollPane = new JScrollPane();
        this.seatScrollPane.applyComponentOrientation(this
            .getComponentOrientation());

        // colorPanel

        final ColorPanel colorPanel = new ColorPanel();
        colorPanel.setBackground(COLOR_BACKGROUND);
        colorPanel.setPreferredSize(new Dimension(275, 240));

        // cartPanel

        this.cartPanel = new CartPanel(this);
        this.cartPanel.setBackground(COLOR_BACKGROUND);
        this.cartPanel.setPreferredSize(new Dimension(275, 440));

        // eastPanel

        final JPanel eastPanel = new JPanel();
        eastPanel.setBackground(COLOR_BACKGROUND);
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setPreferredSize(new Dimension(275, 680));

        eastPanel.add(colorPanel);
        eastPanel.add(this.cartPanel);

        // mainPanel

        final JPanel mainPanel = new JPanel();
        mainPanel.setBackground(COLOR_BACKGROUND);
        mainPanel.setLayout(new BorderLayout());

        // mainPanel.add(screenPanel, BorderLayout.NORTH);
        mainPanel.add(this.seatScrollPane, BorderLayout.CENTER);
        mainPanel.add(eastPanel, BorderLayout.EAST);

        // mapPanel

        final JPanel mapPanel = new JPanel();
        mapPanel.setLayout(new BoxLayout(mapPanel, BoxLayout.Y_AXIS));

        mapPanel.add(comboBoxesPanel);
        mapPanel.add(mainPanel);

        this.setLayout(new CardLayout());

        this.add(mapPanel, "map");
    }

    /**
     */
    private void enableDisableComboBoxes() {
        if ((this.bookingsCart == null) || this.bookingsCart.isEmpty()) {
            this.dateCb.setEnabled(true);
            this.movieCb.setEnabled(true);
            this.timeCb.setEnabled(true);
        } else {
            this.dateCb.setEnabled(false);
            this.movieCb.setEnabled(false);
            this.timeCb.setEnabled(false);
        }
    }

    /**
     * @param ev
     * @return
     */
    private String getStatus(final Event ev) {
        String status;

        try {
            final String runTime = this.dao.getPostByName(ev.getName());

            if (runTime == null) {
                status = "null";
            } else {
                status = runTime;
            }
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
            status = "failed";
        }

        return status;
    }

    /**
     * @param seat
     * @return the {@link Component} generated from the specified {@link Seat}
     */
    private JComponent toComponent(final Seat seat) {
        if (seat == null) {
            final JLabel emptyLabel = new JLabel();

            return emptyLabel;
        }

        final Border BORDER =
            BorderFactory.createMatteBorder(3, 3, 3, 3, COLOR_BACKGROUND);

        final JComponent component;

        final SeatState seatState = seat.getState();
        if ((seatState == SeatState.WHEELCHAIR)
            || (seatState == SeatState.STANDARD)) {

            final ActionListener listener = new SeatButtonAl(this, seat);
            final Color seatColor = toColor(seat);

            String text =
                String.valueOf(seat.getRow()).toUpperCase()
                    + String.valueOf(seat.getColumn());
            if (seatState == SeatState.WHEELCHAIR) {
                text += "(WC)";
            }

            final JButton button = new JButton(text);
            button.addActionListener(listener);
            button.setBackground(seatColor);
            button.setFocusable(false);
            button.setFocusPainted(false);
            button.setFont(FONT_SEAT);
            button.setForeground(Color.WHITE);
            button.setRequestFocusEnabled(false);

            component = button;
        } else if (seatState == SeatState.EXIT) {
            final JLabel label = new JLabel("Exit");
            label.setForeground(Color.white);
            label.setFont(FONT_SEAT);
            label.setHorizontalAlignment(SwingConstants.CENTER);

            component = label;
        } else if (seatState == SeatState.NO_SEAT) {
            final JLabel label = new JLabel();

            component = label;
        } else {
            throw new IllegalArgumentException("seatState: " + seatState);
        }

        component.setBorder(BORDER);

        return component;
    }
}
