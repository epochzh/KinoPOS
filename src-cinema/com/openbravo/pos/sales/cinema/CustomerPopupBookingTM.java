package com.openbravo.pos.sales.cinema;

import com.openbravo.pos.sales.cinema.model.Booking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 */
public class CustomerPopupBookingTM extends AbstractTableModel {

    /**
     */
    private static final long serialVersionUID = 859202123096312876L;

    /**
     */
    private static final String[] COLUMN_NAMES = {
        "Film", "Start date / time", "Seat", "State",
    };

    /**
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMM");

    /**
     */
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    /**
     */
    private final List<Booking> bookings;

    /**
     * @param bookings
     */
    public CustomerPopupBookingTM(final List<Booking> bookings) {
        this.bookings = bookings;
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return this.bookings.size();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    /**
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(final int column) {
        return COLUMN_NAMES[column];
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final Object object;

        final Booking booking = this.bookings.get(rowIndex);
        if (columnIndex == 0) {
            object = " " + booking.getEvent().getName();
        } else if (columnIndex == 1) {
            final Date dateBegin = booking.getEvent().getDateBegin();
            object =
                " "
                    + CinemaReservationMap.getDayOfMonthSuffix(dateBegin
                        .getDate()) + " " + DATE_FORMAT.format(dateBegin)
                    + " / " + TIME_FORMAT.format(dateBegin);
        } else if (columnIndex == 2) {
            object = " " + booking.getSeatCoordinates().toUpperCase();
        } else if (columnIndex == 3) {
            object = " " + booking.getState();
        } else {
            throw new IllegalArgumentException("columnIndex: " + columnIndex);
        }

        return object;
    }

    /**
     * @return the bookings
     */
    public List<Booking> getBookings() {
        return this.bookings;
    }
}
