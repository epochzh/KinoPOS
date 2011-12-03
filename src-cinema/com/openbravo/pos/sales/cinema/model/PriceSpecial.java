package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;

/**
 */
public class PriceSpecial implements IKeyed, Serializable, SerializableRead {

    /**
     */
    private static final long serialVersionUID = -6999065539885000590L;

    /**
	 */
    private Long id;

    /**
     */
    private String name;

    /**
     */
    private Double price;

    /**
     */
    private Venue venue;

    /**
	 */
    public PriceSpecial() {
        super();
    }

    /**
     * @param id
     */
    public PriceSpecial(final Long id) {
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
        this.id = dr.getLong(++index);
        this.name = dr.getString(++index);
        this.price = dr.getDouble(++index);
        this.venue = new Venue(dr.getLong(++index));
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
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return this.price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(final Double price) {
        this.price = price;
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
