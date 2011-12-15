package com.openbravo.pos.sales.cinema.transaction;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.Transaction;
import com.openbravo.pos.sales.cinema.model.Booking;

/**
 */
public class CreateBookingTransaction extends Transaction<Integer> {

    /**
     */
    private Booking booking;

    /**
     */
    private BaseSentence sentence;

    /**
     * @param session
     */
    public CreateBookingTransaction(final Session session) {
        super(session);
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
        final Long customerId;
        if (this.booking.getCustomer() == null) {
            customerId = null;
        } else {
            customerId = this.booking.getCustomer().getId();
        }

        // LOGGER.info("firstfilmprice inside if else: "+this.booking.getPriceType());

        return this.sentence.exec(this.booking.getBarcode(), this.booking
            .getCollected(), customerId, this.booking.getCustomerName(),
            this.booking.getCustomerPhone(), this.booking.getDate(),
            this.booking.getEvent().getId(), this.booking.getPrice(),
            this.booking.getPriceMatrix().getId(), this.booking.getPriceType()
                .getType(), this.booking.getSalesChannel(), this.booking
                .getSeatCoordinates(), this.booking.getState().getState());
    }
}
