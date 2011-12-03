package com.openbravo.pos.sales.cinema;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.loader.DataResultSet;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.sales.cinema.model.Booking;
import com.openbravo.pos.sales.cinema.model.BookingState;
import com.openbravo.pos.sales.cinema.model.Customer;
import com.openbravo.pos.sales.cinema.model.CustomerMeta;
import com.openbravo.pos.sales.cinema.model.Day;
import com.openbravo.pos.sales.cinema.model.Event;
import com.openbravo.pos.sales.cinema.model.MembershipType;
import com.openbravo.pos.sales.cinema.model.PriceMatrix;
import com.openbravo.pos.sales.cinema.model.PriceSpecial;
import com.openbravo.pos.sales.cinema.model.Screen;
import com.openbravo.pos.sales.cinema.model.Seat;
import com.openbravo.pos.sales.cinema.model.Venue;
import com.openbravo.pos.sales.cinema.transaction.CreateBookingTransaction;
import com.openbravo.pos.sales.cinema.transaction.DeleteBookingTransaction;
import com.openbravo.pos.sales.cinema.transaction.UpdateBookingTransaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.lorecraft.phparser.SerializedPhpParser;
import org.lorecraft.phparser.SerializedPhpParser.PhpObject;

/**
 */
public class CinemaDaoImpl extends BeanFactoryDataSingle {

    /**
     */
    private static final Logger LOGGER = Logger.getLogger(CinemaDaoImpl.class
        .getName());

    /**
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
        "yyyy-MM-dd");

    /**
     */
    private Session session;

    /**
     */
    private BaseSentence createBooking;

    /**
     */
    private BaseSentence deleteBooking;

    /**
     */
    private BaseSentence getBookingByBarcode;

    /**
     */
    private BaseSentence getBookingBySeat;

    /**
     */
    private BaseSentence getBookingBarcodeMax;

    /**
     */
    private BaseSentence getCustomer;

    /**
     */
    private BaseSentence getCustomerMetaByPin;

    /**
     */
    private BaseSentence getEvent;

    /**
     */
    private BaseSentence getEventByName;

    /**
     */
    private BaseSentence getFirstFilm;

    /**
     */
    private BaseSentence getNextAvailableFilm;

    /**
     */
    private BaseSentence getPriceFirstFilm;

    /**
     */
    private BaseSentence getScreenByNumber;

    /**
     */
    private BaseSentence getVenue;

    /**
     */
    private BaseSentence listBookingByCustomer;

    /**
     */
    private BaseSentence listBookingByEvent;

    /**
     */
    private BaseSentence listCustomer;

    /**
     */
    private BaseSentence listCustomerMeta;

    /**
     */
    private BaseSentence listEventByDate;

    /**
     */
    private BaseSentence listEventByName;

    /**
     */
    private BaseSentence listPrice;

    /**
     */
    private BaseSentence listPriceByDate;

    /**
     */
    private BaseSentence listSeatByScreen;

    /**
     */
    private BaseSentence searchCustomerMeta;

    /**
     */
    private BaseSentence updateBooking;

    /**
     */
    public CinemaDaoImpl() {
        super();
    }

    /**
     * @see com.openbravo.pos.forms.BeanFactoryDataSingle#init(com.openbravo.data.loader.Session)
     */
    @Override
    public void init(@SuppressWarnings("hiding") final Session session) {
        this.session = session;

        this.createBooking =
            new StaticSentence(this.session, "INSERT INTO dd_bookings "
                + "(barcode, collected, userid, purchasename, "
                + "purchasetelephone, datepurchased, eventsidfk, price, "
                + "pricingmatrixidfk, purchasedmethod, purchasefrom, "
                + "seatnumber, state) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
                new SerializerWriteBasic(Datas.STRING, Datas.BOOLEAN,
                    Datas.LONG, Datas.STRING, Datas.STRING, Datas.TIMESTAMP,
                    Datas.LONG, Datas.DOUBLE, Datas.LONG, Datas.STRING,
                    Datas.STRING, Datas.STRING, Datas.STRING));

        this.deleteBooking =
            new StaticSentence(this.session, "DELETE FROM dd_bookings "
                + "WHERE (id = ?) ", new SerializerWriteBasic(Datas.LONG));

        this.getBookingByBarcode =
            new StaticSentence(this.session,
                "SELECT barcode, collected, userid, purchasename, "
                    + "purchasetelephone, datepurchased, eventsidfk, id, "
                    + "price, pricingmatrixidfk, purchasedmethod, "
                    + "purchasefrom, seatnumber, state " + "FROM dd_bookings "
                    + "WHERE (barcode = ?) ", new SerializerWriteBasic(
                    Datas.STRING), new SerializerReadClass(Booking.class));

        this.getBookingBySeat =
            new StaticSentence(this.session,
                "SELECT barcode, collected, userid, purchasename, "
                    + "purchasetelephone, datepurchased, eventsidfk, id, "
                    + "price, pricingmatrixidfk, purchasedmethod, "
                    + "purchasefrom, seatnumber, state " + "FROM dd_bookings "
                    + "WHERE (eventsidfk = ?) " + "AND (seatnumber = ?) ",
                new SerializerWriteBasic(Datas.LONG, Datas.STRING),
                new SerializerReadClass(Booking.class));

        this.getBookingBarcodeMax =
            new StaticSentence(this.session, "SELECT MAX(barcode) "
                + "FROM dd_bookings ");

        this.getCustomer =
            new StaticSentence(this.session, "SELECT user_email, ID "
                + "FROM wp_users " + "WHERE (ID = ?) ",
                new SerializerWriteBasic(Datas.LONG), new SerializerReadClass(
                    Customer.class));

        this.getCustomerMetaByPin =
            new StaticSentence(this.session,
                "SELECT umeta_id, meta_key, meta_value, user_id "
                    + "FROM wp_usermeta " + "WHERE (meta_key = 'ym_pin') "
                    + "AND (meta_value = ?) ", new SerializerWriteBasic(
                    Datas.LONG), new SerializerReadClass(CustomerMeta.class));

        this.getEvent =
            new StaticSentence(this.session,
                "SELECT start_date, end_date, event_id, event_name, screen, event_type "
                    + "FROM events " + "WHERE (event_type = 'film') "
                    + "AND (event_id = ?) ", new SerializerWriteBasic(
                    Datas.LONG), new SerializerReadClass(Event.class));

        this.getEventByName =
            new StaticSentence(this.session,
                "SELECT start_date, end_date, event_id, event_name, screen, event_type "
                    + "FROM events " + "WHERE (event_type = 'film') "
                    + "AND (venue = ?) " + "AND (event_name = ?) "
                    + "AND (start_date = ?) ", new SerializerWriteBasic(
                    Datas.LONG, Datas.STRING, Datas.TIMESTAMP),
                new SerializerReadClass(Event.class));

        this.getFirstFilm =
            new StaticSentence(
                this.session,
                "SELECT start_date, end_date, event_id, event_name, screen, event_type "
                    + "FROM events " + "WHERE (event_type = 'film') "
                    + "AND (venue = ?) " + "AND (start_date >= ?) "
                    + "AND (end_date < ?) " + "ORDER BY start_date ASC LIMIT 1",
                new SerializerWriteBasic(Datas.LONG, Datas.STRING, Datas.STRING),
                new SerializerReadClass(Event.class));

        this.getNextAvailableFilm =
            new StaticSentence(this.session,
                "SELECT start_date, end_date, event_id, event_name, screen, event_type "
                    + "FROM events " + "WHERE (venue = ?) "
                    + "AND (event_type = 'film') " + "AND (start_date >= ?) "
                    + "ORDER BY start_date ASC ", new SerializerWriteBasic(
                    Datas.LONG, Datas.STRING), new SerializerReadClass(
                    Event.class));

        this.getPriceFirstFilm =
            new StaticSentence(this.session,
                "SELECT id, special_name, special_price, venue_idfk "
                    + "FROM dd_specialprice " + "WHERE (venue_idfk = ?) "
                    // TODO: Should be "first_film" instead of "first film".
                    + "AND (special_name = 'first film') ",
                new SerializerWriteBasic(Datas.LONG), new SerializerReadClass(
                    PriceSpecial.class));

        this.getScreenByNumber =
            new StaticSentence(this.session,
                "SELECT screen_capacity, id, screen_col_count, screen_row_count, "
                    + "screen_number, venue_idfk " + "FROM dd_screen "
                    + "WHERE (venue_idfk = ?) " + "AND (screen_number = ?) ",
                new SerializerWriteBasic(Datas.LONG, Datas.INT),
                new SerializerReadClass(Screen.class));

        this.getVenue =
            new StaticSentence(this.session,
                "SELECT venue_colour, id, venue_name " + "FROM dd_venue "
                    + "WHERE (venue_name = ?) ", new SerializerWriteBasic(
                    Datas.STRING), new SerializerReadClass(Venue.class));

        this.listBookingByCustomer =
            new StaticSentence(this.session,
                "SELECT barcode, collected, userid, purchasename, "
                    + "purchasetelephone, datepurchased, eventsidfk, id, "
                    + "price, pricingmatrixidfk, purchasedmethod, "
                    + "purchasefrom, seatnumber, state " + "FROM dd_bookings "
                    + "WHERE (userid = ?) ", new SerializerWriteBasic(
                    Datas.LONG), new SerializerReadClass(Booking.class));

        this.listBookingByEvent =
            new StaticSentence(this.session,
                "SELECT barcode, collected, userid, purchasename, "
                    + "purchasetelephone, datepurchased, eventsidfk, id, "
                    + "price, pricingmatrixidfk, purchasedmethod, "
                    + "purchasefrom, seatnumber, state " + "FROM dd_bookings "
                    + "WHERE (eventsidfk = ?) ", new SerializerWriteBasic(
                    Datas.LONG), new SerializerReadClass(Booking.class));

        this.listCustomer =
            new StaticSentence(this.session, "SELECT user_email, ID "
                + "FROM wp_users ", null, new SerializerReadClass(
                Customer.class));

        this.listCustomerMeta =
            new StaticSentence(this.session,
                "SELECT umeta_id, meta_key, meta_value, user_id "
                    + "FROM wp_usermeta " + "WHERE (user_id = ?) ",
                new SerializerWriteBasic(Datas.LONG), new SerializerReadClass(
                    CustomerMeta.class));

        this.listEventByDate =
            new StaticSentence(
                this.session,
                "SELECT start_date, end_date, event_id, event_name, screen, event_type "
                    + "FROM events " + "WHERE (event_type = 'film') "
                    + "AND (venue = ?) " + "AND (start_date >= ?) "
                    + "AND (end_date < ?) " + "ORDER BY event_name ASC ",
                new SerializerWriteBasic(Datas.LONG, Datas.STRING, Datas.STRING),
                new SerializerReadClass(Event.class));

        this.listEventByName =
            new StaticSentence(this.session,
                "SELECT start_date, end_date, event_id, event_name, screen, event_type "
                    + "FROM events " + "WHERE (venue = ?) "
                    + "AND (event_type = 'film') " + "AND (event_name = ?) "
                    + "AND (start_date >= ?) " + "AND (end_date < ?) "
                    + "ORDER BY start_date ASC ", new SerializerWriteBasic(
                    Datas.LONG, Datas.STRING, Datas.TIMESTAMP, Datas.STRING),
                new SerializerReadClass(Event.class));

        this.listPrice =
            new StaticSentence(this.session,
                "SELECT day, id, name, full_price, gold, kino_friends, "
                    + "kino_staff, senior, silver, student, u16, "
                    + "ticketsForOffer, start_time, " + "end_time, venue_idfk "
                    + "FROM dd_pricematrix " + "WHERE (venue_idfk = ?) ",
                new SerializerWriteBasic(Datas.LONG), new SerializerReadClass(
                    PriceMatrix.class));

        this.listPriceByDate =
            new StaticSentence(this.session,
                "SELECT day, id, name, full_price, gold, kino_friends, "
                    + "kino_staff, senior, silver, student, u16, "
                    + "ticketsForOffer, start_time, " + "end_time, venue_idfk "
                    + "FROM dd_pricematrix " + "WHERE (venue_idfk = ?) "
                    + "AND (day = ?) " + "AND (start_time <= ?) "
                    + "AND (end_time > ?) ", new SerializerWriteBasic(
                    Datas.LONG, Datas.STRING, Datas.STRING, Datas.STRING),
                new SerializerReadClass(PriceMatrix.class));

        this.listSeatByScreen =
            new StaticSentence(this.session,
                "SELECT seatplanner_number, id, row_letter, screen_idfk, seatplanner_state "
                    + "FROM dd_seatplanner " + "WHERE (screen_idfk = ?) ",
                new SerializerWriteBasic(Datas.LONG), new SerializerReadClass(
                    Seat.class));

        this.searchCustomerMeta =
            new StaticSentence(this.session,
                "SELECT umeta_id, meta_key, meta_value, user_id "
                    + "FROM wp_usermeta " + "WHERE "
                    + "((meta_key = 'first_name') AND (meta_value LIKE ?)) "
                    + "OR "
                    + "((meta_key = 'last_name') AND (meta_value LIKE ?)) ",
                new SerializerWriteBasic(Datas.STRING, Datas.STRING),
                new SerializerReadClass(CustomerMeta.class));

        this.updateBooking =
            new StaticSentence(
                this.session,
                "UPDATE dd_bookings "
                    + "SET barcode = ?, collected = ?, userid = ?, purchasename = ?, "
                    + "purchasetelephone = ?, datepurchased = ?, eventsidfk = ?, "
                    + "price = ?, pricingmatrixidfk = ?, purchasedmethod = ?, "
                    + "purchasefrom = ?, seatnumber = ?, state = ? "
                    + "WHERE (id = ?) ", new SerializerWriteBasic(Datas.STRING,
                    Datas.BOOLEAN, Datas.LONG, Datas.STRING, Datas.STRING,
                    Datas.TIMESTAMP, Datas.LONG, Datas.DOUBLE, Datas.LONG,
                    Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,
                    Datas.LONG));
    }

    /**
     * @param booking
     * @throws BasicException
     */
    public void createBooking(final Booking booking) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("booking: " + booking);
        }

        final CreateBookingTransaction transaction =
            new CreateBookingTransaction(this.session);
        transaction.setBooking(booking);
        transaction.setSentence(this.createBooking);

        transaction.execute();
    }

    /**
     * @param booking
     * @throws BasicException
     */
    public void deleteBooking(final Booking booking) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("booking: " + booking);
        }

        this.deleteBooking(Arrays.asList(booking));
    }

    /**
     * @param bookings
     * @throws BasicException
     */
    public void deleteBooking(final List<Booking> bookings)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("bookings: " + bookings.size());
        }

        if (bookings.isEmpty()) {
            return;
        }

        final DeleteBookingTransaction transaction =
            new DeleteBookingTransaction(this.session);
        transaction.setBookings(bookings);
        transaction.setSentence(this.deleteBooking);

        final Integer result = transaction.execute();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("result: " + result);
        }
    }

    /**
     * @param event
     * @throws BasicException
     */
    public void deleteReserved(final Event event) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("event: " + event);
        }

        final List<Booking> deletes = new ArrayList<Booking>();

        @SuppressWarnings("unchecked")
        final List<Booking> bookings =
            this.listBookingByEvent.list(event.getId(), null);
        for (final Booking booking : bookings) {
            if (booking.getState() == BookingState.RESERVED) {
                deletes.add(booking);
            }
        }

        final DeleteBookingTransaction transaction =
            new DeleteBookingTransaction(this.session);
        transaction.setBookings(deletes);
        transaction.setSentence(this.deleteBooking);

        transaction.execute();
    }

    /**
     * @param event
     * @param seatCoordinates
     * @return the {@link Booking} associated to the specified {@link Event} and
     * seatCoordinates
     * @throws BasicException
     */
    public Booking getBooking(final Event event, final String seatCoordinates)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("event: " + event + ", seatCoordinates: "
                + seatCoordinates);
        }

        final Booking booking =
            (Booking) this.getBookingBySeat
                .find(event.getId(), seatCoordinates);

        return booking;
    }

    /**
     * @param barcode
     * @return the {@link Booking} associated to the specified barcode
     * @throws BasicException
     */
    public Booking getBookingByBarcode(final String barcode)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("barcode: " + barcode);
        }

        final Booking booking =
            (Booking) this.getBookingByBarcode.find(barcode, null);

        return booking;
    }

    /**
     * @return the max barcode
     * @throws BasicException
     */
    public int getBookingBarcodeMax() throws BasicException {
        final DataResultSet resultSet =
            this.getBookingBarcodeMax.openExec(null);
        resultSet.next();
        final int maxBarcode = resultSet.getInt(1);
        resultSet.close();
        this.getBookingBarcodeMax.closeExec();

        return maxBarcode;
    }

    /**
     * @param id
     * @return the {@link Customer} associated to the specified id
     * @throws BasicException
     */
    public Customer getCustomer(final Long id) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("id: " + id);
        }

        final Customer customer = (Customer) this.getCustomer.find(id, null);
        this.fillCustomerMeta(customer);

        return customer;
    }

    /**
     * @param pin
     * @return the {@link Customer} associated to the specified pin
     * @throws BasicException
     */
    public Customer getCustomerByPin(final String pin) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("pin: " + pin);
        }

        final Customer customer;

        final CustomerMeta meta =
            (CustomerMeta) this.getCustomerMetaByPin.find(pin, null);
        if (meta == null) {
            customer = null;
        } else {
            customer = (Customer) this.getCustomer.find(meta.getUserId(), null);
            this.fillCustomerMeta(customer);
        }

        return customer;
    }

    /**
     * @param id
     * @return the {@link Event} associated to the specified id
     * @throws BasicException
     */
    public Event getEvent(final Long id) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("id: " + id);
        }

        final Event event = (Event) this.getEvent.find(id, null);

        return event;
    }

    /**
     * @param venue
     * @param name
     * @param dateBegin
     * @return the {@link Event} associated to the specified {@link Venue}, name
     * and dateBegin
     * @throws BasicException
     */
    public Event getEvent(final Venue venue, final String name,
    final Date dateBegin) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("venue: " + venue + ", name: " + name + ", datebegin: "
                + dateBegin);
        }

        final Event event =
            (Event) this.getEventByName.find(venue.getId(), name, dateBegin);

        return event;
    }

    /**
     * @param venue
     * @return the next available film for the specified {@link Venue}
     * @throws BasicException
     */
    public Event getNextAvailableFilm(final Venue venue) throws BasicException {
        final Date date = new Date();

        return this.getNextAvailableFilm(venue, date);
    }

    /**
     * @param venue
     * @param date
     * @return the next available film for the specified {@link Venue} and the
     * specified date
     * @throws BasicException
     */
    public Event getNextAvailableFilm(final Venue venue, final Date date)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("venue: " + venue + ", date: " + date);
        }

        final Event event =
            (Event) this.getNextAvailableFilm.find(venue.getId(), DATE_FORMAT
                .format(date));

        return event;
    }

    /**
     * @param events
     * @param date
     * @return the next available film
     */
    public static Event getNextAvailableFilm(final List<Event> events,
    final Date date) {
        final Calendar calendar = Calendar.getInstance();

        Date dateBegin = date;
        dateBegin =
            DateUtils.setHours(dateBegin, calendar.get(Calendar.HOUR_OF_DAY));
        dateBegin =
            DateUtils.setMinutes(dateBegin, calendar.get(Calendar.MINUTE));

        Event next = null;
        for (final Event event : events) {
            if (next == null) {
                if (event.getDateBegin().after(dateBegin)) {
                    next = event;
                }
                continue;
            }

            if (event.getDateBegin().after(dateBegin)
                && event.getDateBegin().before(next.getDateBegin())) {
                next = event;
            }
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("next: " + next);
        }

        if ((next == null) && !events.isEmpty()) {
            next = events.get(events.size() - 1);
        }

        return next;
    }

    /**
     * @param venue
     * @return the {@link PriceSpecial} of the first film for the specified
     * {@link Venue}
     * @throws BasicException
     */
    public PriceSpecial getPriceFirstFilm(final Venue venue)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("venue: " + venue);
        }

        final PriceSpecial price =
            (PriceSpecial) this.getPriceFirstFilm.find(venue.getId(), null);

        return price;
    }

    /**
     * @param venue
     * @param number
     * @return the {@link Screen} associated to the specified {@link Venue} and
     * number
     * @throws BasicException
     */
    public Screen getScreenByNumber(final Venue venue, final Byte number)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("venue: " + venue + ", number: " + number);
        }

        final Screen screen =
            (Screen) this.getScreenByNumber.find(venue.getId(), number
                .intValue());

        return screen;
    }

    /**
     * @param name
     * @return the {@link Venue} associated to the specified name
     * @throws BasicException
     */
    public Venue getVenue(final String name) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("name: " + name);
        }

        final Venue venue = (Venue) this.getVenue.find(name, null);

        return venue;
    }

    /**
     * @param venue
     * @param event
     * @return <code>true</code> if the first film special price is application
     * to the specified event, <code>false</code> otherwise
     * @throws BasicException
     */
    public boolean isFirstFilmApplicable(final Venue venue, final Event event)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("venue: " + venue + ", event: " + event);
        }

        final boolean isFirstFilmApplicable;

        final Calendar dateBegin = Calendar.getInstance();
        dateBegin.setTime(event.getDateBegin());

        // The first film day must be a MONDAY, WEDNESDAY, THURSDAY or FRIDAY.
        final int dayOfWeek = dateBegin.get(Calendar.DAY_OF_WEEK);
        final boolean dayOfWeekOk =
            (dayOfWeek == Calendar.MONDAY) || (dayOfWeek == Calendar.WEDNESDAY)
                || (dayOfWeek == Calendar.THURSDAY)
                || (dayOfWeek == Calendar.FRIDAY);
        if (!dayOfWeekOk) {
            return false;
        }

        dateBegin.set(Calendar.HOUR_OF_DAY, 0);
        dateBegin.set(Calendar.MINUTE, 0);
        dateBegin.set(Calendar.SECOND, 0);
        dateBegin.set(Calendar.MILLISECOND, 0);

        // The first film time must start before 10:30.
        final Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTime(dateBegin.getTime());
        dateEnd.set(Calendar.HOUR_OF_DAY, 10);
        dateEnd.set(Calendar.MINUTE, 30);

        final Event firstFilm =
            (Event) this.getFirstFilm.find(venue.getId(), Event.DATE_FORMAT
                .format(dateBegin.getTime()), Event.DATE_FORMAT.format(dateEnd
                .getTime()));

        isFirstFilmApplicable =
            (firstFilm != null) && (firstFilm.getId().equals(event.getId()));

        return isFirstFilmApplicable;
    }

    /**
     * @param customer
     * @return the list of {@link Booking} associated to the specified
     * {@link Customer}
     * @throws BasicException
     */
    public List<Booking> listBookingByCustomer(final Customer customer)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("customer: " + customer);
        }

        @SuppressWarnings("unchecked")
        final List<Booking> bookings =
            this.listBookingByCustomer.list(customer.getId(), null);
        for (final Booking booking : bookings) {
            final Event event = this.getEvent(booking.getEvent().getId());
            if (event == null) {
                continue;
            }
            booking.setEvent(event);
        }

        return bookings;
    }

    /**
     * @return the list of {@link Customer}
     * @throws BasicException
     */
    public List<Customer> listCustomer() throws BasicException {
        final List<Customer> customers = new ArrayList<Customer>();

        @SuppressWarnings("unchecked")
        final List<Customer> temp = this.listCustomer.list();

        for (final Customer customer : temp) {
            this.fillCustomerMeta(customer);

            if (customer.getPin() == null) {
                continue;
            }

            customers.add(customer);
        }

        return customers;
    }

    /**
     * @return the list of the dates for the next three weeks
     */
    public List<Date> listDateThreeWeeks() {
        final Date date = new Date();

        return this.listDateThreeWeeks(date);
    }

    /**
     * @param date
     * @return the list of the dates for the next three weeks starting at the
     * specified date
     */
    @SuppressWarnings("static-method")
    public List<Date> listDateThreeWeeks(final Date date) {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("date: " + date);
        }

        final List<Date> dates = new ArrayList<Date>();
        dates.add(date);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i = 0; i < 20; ++i) {
            calendar.add(Calendar.DATE, 1);

            dates.add(calendar.getTime());
        }

        return dates;
    }

    /**
     * @param venue
     * @param name
     * @param date
     * @return the list of {@link Event} associated to the specified
     * {@link Venue}, name and date
     * @throws BasicException
     */
    public List<Event> listEvent(final Venue venue, final String name,
    final Date date) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("venue: " + venue + ", name: " + name + ", date: "
                + date);
        }

        final Date dateBegin = date;
        final Date dateEnd = DateUtils.addDays(dateBegin, 1);

        @SuppressWarnings("unchecked")
        final List<Event> events =
            this.listEventByName.list(venue.getId(), name, dateBegin,
                DATE_FORMAT.format(dateEnd));

        return events;
    }

    /**
     * @param venue
     * @param date
     * @return the list of {@link Event} matching the specified {@link Venue}
     * and {@link Date}
     * @throws BasicException
     */
    public List<Event> listEvent(final Venue venue, final Date date)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("venue: " + venue + ", date: " + date);
        }

        final Calendar calendar = Calendar.getInstance();

        Date dateBegin = date;
        dateBegin =
            DateUtils.setHours(dateBegin, calendar.get(Calendar.HOUR_OF_DAY));
        dateBegin =
            DateUtils.setMinutes(dateBegin, calendar.get(Calendar.MINUTE));
        final Date dateEnd = DateUtils.addDays(dateBegin, 1);

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("dateBegin: " + dateBegin + ", dateEnd: " + dateEnd);
        }

        @SuppressWarnings("unchecked")
        final List<Event> events =
            this.listEventByDate.list(venue.getId(), DATE_FORMAT
                .format(dateBegin), DATE_FORMAT.format(dateEnd));

        return events;
    }

    /**
     * @param venue
     * @return the list of {@link PriceMatrix} associated to the specified
     * {@link Venue}
     * @throws BasicException
     */
    public List<PriceMatrix> listPrice(final Venue venue) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("venue: " + venue);
        }

        @SuppressWarnings("unchecked")
        final List<PriceMatrix> prices =
            this.listPrice.list(venue.getId(), null);

        return prices;
    }

    /**
     * @param venue
     * @param day
     * @param timeBegin
     * @return the list of {@link PriceMatrix} associated to the specified
     * {@link Venue}, {@link Day} and timeBegin
     * @throws BasicException
     */
    public List<PriceMatrix> listPrice(final Venue venue, final Day day,
    final Date timeBegin) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("venue: " + venue + ", day: " + day + ", timeBegin: "
                + timeBegin);
        }

        final String time = PriceMatrix.TIME_FORMAT.format(timeBegin);

        @SuppressWarnings("unchecked")
        final List<PriceMatrix> prices =
            this.listPriceByDate.list(venue.getId(), day.getName(), time, time);

        return prices;
    }

    /**
     * @param venue
     * @param event
     * @return the list of {@link PriceMatrix} associated to the specified
     * {@link Venue} and {@link Event}
     * @throws BasicException
     */
    public List<PriceMatrix> listPrice(final Venue venue, final Event event)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("venue: " + venue + ", event: " + event);
        }

        @SuppressWarnings("deprecation")
        final Day day = Day.values()[event.getDateBegin().getDay()];

        final List<PriceMatrix> prices =
            this.listPrice(venue, day, event.getDateBegin());

        return prices;
    }

    /**
     * @param event
     * @return the list of the {@link Seat} associated to the specified
     * {@link Event}
     * @throws BasicException
     */
    public List<Seat> listSeat(final Event event) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("event: " + event);
        }

        @SuppressWarnings("unchecked")
        final List<Booking> bookings =
            this.listBookingByEvent.list(event.getId(), null);

        @SuppressWarnings("unchecked")
        final List<Seat> seats =
            this.listSeatByScreen.list(event.getScreen().getId(), null);

        final Map<String, Seat> map = new Hashtable<String, Seat>(seats.size());
        for (final Seat seat : seats) {
            final String key =
                String.valueOf(seat.getRow())
                    + String.valueOf(seat.getColumn());
            map.put(key, seat);
        }

        for (final Booking booking : bookings) {
            final String key = booking.getSeatCoordinates();
            map.get(key).setBooking(booking);
        }

        return seats;
    }

    /**
     * @param booking
     * @throws BasicException
     */
    public void saveBooking(final Booking booking) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("booking: " + booking);
        }

        if (booking.getId() == null) {
            this.createBooking(booking);
        } else {
            this.updateBooking(booking);
        }
    }

    /**
     * @param name
     * @return the list of {@link Customer} matching the specified name
     * @throws BasicException
     */
    public List<Customer> searchCustomer(final String name)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("name: " + name);
        }

        final List<Customer> customers = new ArrayList<Customer>();

        final Set<Long> ids = new TreeSet<Long>();

        final String firstName = "%" + name + "%";
        final String lastName = "%" + name + "%";

        @SuppressWarnings("unchecked")
        final List<CustomerMeta> temp =
            this.searchCustomerMeta.list(firstName, lastName);

        for (final CustomerMeta meta : temp) {
            final Long id = meta.getUserId();
            if (ids.contains(id)) {
                continue;
            }
            ids.add(id);

            final Customer customer =
                (Customer) this.getCustomer.find(id, null);
            this.fillCustomerMeta(customer);

            if (customer.getPin() == null) {
                continue;
            }

            customers.add(customer);
        }

        return customers;
    }

    /**
     * @param booking
     * @throws BasicException
     */
    public void updateBooking(final Booking booking) throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("booking: " + booking);
        }

        final List<Booking> bookings = Arrays.asList(booking);

        this.updateBooking(bookings);
    }

    /**
     * @param bookings
     * @throws BasicException
     */
    public void updateBooking(final List<Booking> bookings)
    throws BasicException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("bookings: " + bookings.size());
        }

        final UpdateBookingTransaction transaction =
            new UpdateBookingTransaction(this.session);
        transaction.setBookings(bookings);
        transaction.setSentence(this.updateBooking);

        transaction.execute();
    }

    /**
     * @param customer
     * @throws BasicException
     */
    private void fillCustomerMeta(final Customer customer)
    throws BasicException {
        final Map<String, CustomerMeta> map =
            new Hashtable<String, CustomerMeta>();

        @SuppressWarnings("unchecked")
        final List<CustomerMeta> metas =
            this.listCustomerMeta.list(customer.getId(), null);
        for (final CustomerMeta meta : metas) {
            map.put(meta.getMetaKey(), meta);
        }

        if (map.get("ym_custom_fields") == null) {
            return;
        }

        SerializedPhpParser parser =
            new SerializedPhpParser(map.get("ym_user").getMetaValue());
        final PhpObject ymUser = (PhpObject) parser.parse();

        parser =
            new SerializedPhpParser(map.get("ym_custom_fields").getMetaValue());
        @SuppressWarnings("unchecked")
        final Map<Integer, String> ymCustom =
            (Map<Integer, String>) parser.parse();

        final Date birthdate;
        final Date membershipBegin;
        final Date membershipEnd;
        final Integer membershipPackId;
        try {
            birthdate =
                CustomerMeta.DATE_FORMAT_BIRTHDATE.parse(ymCustom.get(4));
            String string = (String) ymUser.attributes.get("last_pay_date");
            if (StringUtils.isEmpty(string)) {
                membershipBegin = null;
            } else {
                membershipBegin = CustomerMeta.DATE_FORMAT_MS.parse(string);
            }
            string = (String) ymUser.attributes.get("expire_date");
            if (StringUtils.isEmpty(string)) {
                membershipEnd = null;
            } else {
                membershipEnd = CustomerMeta.DATE_FORMAT_MS.parse(string);
            }
            string = (String) ymUser.attributes.get("pack_id");
            if (StringUtils.isEmpty(string)) {
                membershipPackId = null;
            } else {
                membershipPackId = Integer.valueOf(string);
            }
        } catch (final ParseException ex) {
            throw new BasicException(ex.getMessage());
        }

        customer.setAddress1(ymCustom.get(7));
        customer.setAddress2(ymCustom.get(8));
        customer.setCity(ymCustom.get(9));
        customer.setBirthdate(birthdate);
        customer.setFirstName(map.get("first_name").getMetaValue());
        customer.setLastName(map.get("last_name").getMetaValue());
        customer.setMsAmount(Double.valueOf((String) ymUser.attributes
            .get("amount")));
        customer.setMsCurrency((String) ymUser.attributes.get("currency"));
        customer.setMsBegin(membershipBegin);
        customer.setMsEnd(membershipEnd);
        customer.setMsType(MembershipType.fromType((String) ymUser.attributes
            .get("account_type")));
        customer.setMsDuration(Integer.valueOf((String) ymUser.attributes
            .get("duration")));
        customer.setMsDurationType(((String) ymUser.attributes
            .get("duration_type")).charAt(0));
        customer.setMsGateway((String) ymUser.attributes.get("gateway_used"));
        customer.setMsPackId(membershipPackId);
        customer
            .setMsStatusString((String) ymUser.attributes.get("status_str"));
        customer.setPin(ymCustom.get(17));
        customer.setStudent("Yes".equals(ymCustom.get(5)));
    }
}
