package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;

/**
 */
public class Screen implements IKeyed, Serializable, SerializableRead {

    /**
	 */
    private static final long serialVersionUID = 204909970579725257L;

    /**
	 */
    private Short capacity;

    /**
	 */
    private Long id;

    /**
	 */
    private Byte nbOfCols;

    /**
	 */
    private Byte nbOfRows;

    /**
	 */
    private Byte number;

    /**
	 */
    private Venue venue;

    /**
	 */
    public Screen() {
        super();
    }

    /**
     * @param id
     */
    public Screen(final Long id) {
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
        this.capacity = dr.getShort(++index);
        this.id = dr.getLong(++index);
        this.nbOfCols = dr.getByte(++index);
        this.nbOfRows = dr.getByte(++index);
        this.number = dr.getByte(++index);
        this.venue = new Venue(dr.getLong(++index));
    }

    /**
     * @return the capacity
     */
    public Short getCapacity() {
        return this.capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(final Short capacity) {
        this.capacity = capacity;
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
     * @return the nbOfCols
     */
    public Byte getNbOfCols() {
        return this.nbOfCols;
    }

    /**
     * @param nbOfCols the nbOfCols to set
     */
    public void setNbOfCols(final Byte nbOfCols) {
        this.nbOfCols = nbOfCols;
    }

    /**
     * @return the nbOfRows
     */
    public Byte getNbOfRows() {
        return this.nbOfRows;
    }

    /**
     * @param nbOfRows the nbOfRows to set
     */
    public void setNbOfRows(final Byte nbOfRows) {
        this.nbOfRows = nbOfRows;
    }

    /**
     * @return the number
     */
    public Byte getNumber() {
        return this.number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(final Byte number) {
        this.number = number;
    }

    /**
     * @return the venue
     */
    public Venue getVenue() {
        return this.venue;
    }

    /**
     * @param venue the venue to set
     */
    public void setVenue(final Venue venue) {
        this.venue = venue;
    }
}
