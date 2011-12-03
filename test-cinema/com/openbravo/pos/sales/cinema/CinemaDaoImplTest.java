package com.openbravo.pos.sales.cinema;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppViewConnection;
import com.openbravo.pos.sales.cinema.model.Booking;
import com.openbravo.pos.sales.cinema.model.BookingState;
import com.openbravo.pos.sales.cinema.model.Event;
import com.openbravo.pos.sales.cinema.model.PriceMatrix;
import com.openbravo.pos.sales.cinema.model.PriceType;
import com.openbravo.pos.sales.cinema.model.Screen;
import com.openbravo.pos.sales.cinema.model.Seat;
import com.openbravo.pos.sales.cinema.model.Venue;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 */
public class CinemaDaoImplTest {

    /**
     */
    private static CinemaDaoImpl dao;

    /**
     * @throws BasicException
     */
    @BeforeClass
    public static void setUpClass() throws BasicException {
        final AppConfig config = new AppConfig(new String[] {});
        config.load();

        final Session session = AppViewConnection.createSession(config);

        dao = new CinemaDaoImpl();
        dao.init(session);
    }

    /**
     * @throws BasicException
     */
    @SuppressWarnings("static-method")
    @Test
    public void testCreateBooking() throws BasicException {
        Booking booking = new Booking();
        booking.setBarcode("000001");
        booking.setCollected(false);
        booking.setCustomer(null);
        booking.setCustomerName("Cimballi");
        booking.setCustomerPhone("9876543210");
        booking.setDate(new Date());
        booking.setEvent(new Event(14L));
        booking.setPrice(1.23);
        booking.setPriceMatrix(new PriceMatrix(7L));
        booking.setPriceType(PriceType.GOLD);
        booking.setSalesChannel("Hawkhurst");
        booking.setSeatCoordinates("c3");
        booking.setState(BookingState.LOCKED);

        dao.createBooking(booking);

        booking = dao.getBooking(new Event(14L), "c3");
        Assert.assertNotNull(booking);

        dao.deleteBooking(booking);

        booking = dao.getBooking(new Event(14L), "c3");
        Assert.assertNull(booking);
    }

    /**
     * @throws BasicException
     */
    @SuppressWarnings("static-method")
    @Test
    public void testGetMaxBarcode() throws BasicException {
        final int barcode = dao.getBookingBarcodeMax();
        Assert.assertEquals(10000371, barcode);
    }

    /**
     * @throws BasicException
     */
    @SuppressWarnings("static-method")
    @Test
    public void testGetNextAvailableFilm() throws BasicException {
        @SuppressWarnings("deprecation")
        final Event event =
            dao.getNextAvailableFilm(new Venue(26L), new Date(111, 11, 31));
        Assert.assertNull(event);
    }

    /**
     * @throws BasicException
     */
    @SuppressWarnings("static-method")
    @Test
    public void testGetScreenByNumber() throws BasicException {
        final Screen screen = dao.getScreenByNumber(new Venue(26L), (byte) 2);
        Assert.assertNotNull(screen);
    }

    /**
     * @throws BasicException
     */
    @SuppressWarnings("static-method")
    @Test
    public void testGetVenue() throws BasicException {
        final Venue venue = dao.getVenue("Hawkhurst");
        Assert.assertNotNull(venue);
    }

    /**
     */
    @SuppressWarnings("static-method")
    @Test
    public void testListDateThreeWeeks() {
        final List<Date> dates = dao.listDateThreeWeeks(new Date());
        Assert.assertEquals(21, dates.size());
    }

    /**
     * @throws BasicException
     */
    @SuppressWarnings("static-method")
    @Test
    public void testListFilm() throws BasicException {
        @SuppressWarnings("deprecation")
        final List<Event> events =
            dao.listEvent(new Venue(26L), new Date(111, 9, 02));
        Assert.assertEquals(1, events.size());
        Assert.assertEquals("Harry Potter and the Deathly Hallows: Part 2",
            events.get(0).getName());
    }

    /**
     * @throws BasicException
     */
    @SuppressWarnings("static-method")
    @Test
    public void testListSeat() throws BasicException {
        final Screen screen = new Screen(3L);

        final Event event = new Event(14L);
        event.setScreen(screen);

        final List<Seat> seats = dao.listSeat(event);
        Assert.assertEquals(108, seats.size());
    }
}
