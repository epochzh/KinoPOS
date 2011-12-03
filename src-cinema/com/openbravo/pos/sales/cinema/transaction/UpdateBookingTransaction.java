package com.openbravo.pos.sales.cinema.transaction;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.Transaction;
import com.openbravo.pos.sales.cinema.model.Booking;

import java.util.List;

/**
 */
public class UpdateBookingTransaction extends Transaction<Integer> {

    /**
     */
    private List<Booking> bookings;

    /**
     */
    private BaseSentence sentence;

    /**
     * @param session
     */
    public UpdateBookingTransaction(final Session session) {
        super(session);
    }

    /**
     * @return the bookings
     */
    public List<Booking> getBookings() {
        return this.bookings;
    }

    /**
     * @param bookings the bookings to set
     */
    public void setBookings(final List<Booking> bookings) {
        this.bookings = bookings;
    }

    /**
     * @return the sentence
     */
    public BaseSentence getSentence() {
        return this.sentence;
    }

    /**
     * @param sentence the sentence to set
     */
    public void setSentence(final BaseSentence sentence) {
        this.sentence = sentence;
    }

    /**
     * @see com.openbravo.data.loader.Transaction#transact()
     */
    @Override
    protected Integer transact() throws BasicException {
        int result = 0;

        for (final Booking booking : this.bookings) {
            result += this.transact(booking);
        }

        return result;
    }

    /**
     * @param booking
     * @return the current result as an update count; -1 if the current result
     * is a <code>ResultSet</code> object or there are no more results
     * @throws BasicException
     */
    protected int transact(final Booking booking) throws BasicException {
        final Long customerId;
        if (booking.getCustomer() == null) {
            customerId = null;
        } else {
            customerId = booking.getCustomer().getId();
        }

        return this.sentence.exec(booking.getBarcode(), booking.getCollected(),
            customerId, booking.getCustomerName(), booking.getCustomerPhone(),
            booking.getDate(), booking.getEvent().getId(), booking.getPrice(),
            booking.getPriceMatrix().getId(), booking.getPriceType().getType(),
            booking.getSalesChannel(), booking.getSeatCoordinates(), booking
                .getState().getState(), booking.getId());
    }
}
