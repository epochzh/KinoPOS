package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;

/**
 */
public class Seat implements IKeyed, Serializable, SerializableRead {

    /**
     */
    private static final long serialVersionUID = 1063877592798509904L;

    /**
     * Not persisted.
     */
    private Booking booking;

    /**
     */
    private Byte column;

    /**
	 */
    private Long id;

    /**
	 */
    private Character row;

    /**
	 */
    private Screen screen;

    /**
	 */
    private SeatState state;

    /**
	 */
    public Seat() {
        super();
    }

    /**
     * @param id
     */
    public Seat(final Long id) {
        this.id = id;
    }

    /**
     * @see com.openbravo.data.loader.IKeyed#getKey()
     */
    @Override
    public Object getKey() {
        return this.id;
    }

    /**
     * @see com.openbravo.data.loader.SerializableRead#readValues(com.openbravo.data.loader.DataRead)
     */
    @Override
    public void readValues(final DataRead dr) throws BasicException {
        int index = 0;
        this.column = dr.getByte(++index);
        this.id = dr.getLong(++index);
        this.row = dr.getCharacter(++index);
        this.screen = new Screen(dr.getLong(++index));
        this.state = SeatState.fromState(dr.getByte(++index));
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
     * @return the column
     */
    public Byte getColumn() {
        return this.column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(final Byte column) {
        this.column = column;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the row
     */
    public Character getRow() {
        return this.row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(final Character row) {
        this.row = row;
    }

    /**
     * @return the screen
     */
    public Screen getScreen() {
        return this.screen;
    }

    /**
     * @param screen the screen to set
     */
    public void setScreen(final Screen screen) {
        this.screen = screen;
    }

    /**
     * @return the state
     */
    public SeatState getState() {
        return this.state;
    }

    /**
     * @param state the state to set
     */
    public void setState(final SeatState state) {
        this.state = state;
    }
}
