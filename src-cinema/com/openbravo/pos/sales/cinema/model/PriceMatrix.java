package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class PriceMatrix implements IKeyed, Serializable, SerializableRead {

    /**
     */
    private static final long serialVersionUID = -2278064049340225374L;

    /**
     */
    public static final DateFormat TIME_FORMAT = new SimpleDateFormat(
        "HH:mm:ss");

    /**
     */
    private Day day;

    /**
	 */
    private Long id;

    /**
     */
    private String name;

    /**
     */
    private Double priceFull;

    /**
     */
    private Double priceGold;

    /**
     */
    private Double priceKinoFriends;

    /**
     */
    private Double priceKinoStaff;

    /**
     */
    private Double priceSenior;

    /**
     */
    private Double priceSilver;

    /**
     */
    private Double priceStudent;

    /**
     */
    private Double priceU16;

    /**
     */
    private Byte ticketsForOffer;

    /**
     */
    private Date timeBegin;

    /**
     */
    private Date timeEnd;

    /**
     */
    private Venue venue;

    /**
	 */
    public PriceMatrix() {
        super();
    }

    /**
     * @param id
     */
    public PriceMatrix(final Long id) {
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
        this.day = Day.fromName(dr.getString(++index));
        this.id = dr.getLong(++index);
        this.name = dr.getString(++index);
        this.priceFull = dr.getDouble(++index);
        this.priceGold = dr.getDouble(++index);
        this.priceKinoFriends = dr.getDouble(++index);
        this.priceKinoStaff = dr.getDouble(++index);
        this.priceSenior = dr.getDouble(++index);
        this.priceSilver = dr.getDouble(++index);
        this.priceStudent = dr.getDouble(++index);
        this.priceU16 = dr.getDouble(++index);
        this.ticketsForOffer = dr.getByte(++index);
        try {
            this.timeBegin = TIME_FORMAT.parse(dr.getString(++index));
            this.timeEnd = TIME_FORMAT.parse(dr.getString(++index));
        } catch (final ParseException ex) {
            throw new BasicException(ex.getMessage(), ex);
        }
        this.venue = new Venue(dr.getLong(++index));
    }

    /**
     * @return the day
     */
    public Day getDay() {
        return this.day;
    }

    /**
     * @param day the day to set
     */
    public void setDay(final Day day) {
        this.day = day;
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
     * @return the priceFull
     */
    public Double getPriceFull() {
        return this.priceFull;
    }

    /**
     * @param priceFull the priceFull to set
     */
    public void setPriceFull(final Double priceFull) {
        this.priceFull = priceFull;
    }

    /**
     * @return the priceGold
     */
    public Double getPriceGold() {
        return this.priceGold;
    }

    /**
     * @param priceGold the priceGold to set
     */
    public void setPriceGold(final Double priceGold) {
        this.priceGold = priceGold;
    }

    /**
     * @return the priceKinoFriends
     */
    public Double getPriceKinoFriends() {
        return this.priceKinoFriends;
    }

    /**
     * @param priceKinoFriends the priceKinoFriends to set
     */
    public void setPriceKinoFriends(final Double priceKinoFriends) {
        this.priceKinoFriends = priceKinoFriends;
    }

    /**
     * @return the priceKinoStaff
     */
    public Double getPriceKinoStaff() {
        return this.priceKinoStaff;
    }

    /**
     * @param priceKinoStaff the priceKinoStaff to set
     */
    public void setPriceKinoStaff(final Double priceKinoStaff) {
        this.priceKinoStaff = priceKinoStaff;
    }

    /**
     * @return the priceSenior
     */
    public Double getPriceSenior() {
        return this.priceSenior;
    }

    /**
     * @param priceSenior the priceSenior to set
     */
    public void setPriceSenior(final Double priceSenior) {
        this.priceSenior = priceSenior;
    }

    /**
     * @return the priceSilver
     */
    public Double getPriceSilver() {
        return this.priceSilver;
    }

    /**
     * @param priceSilver the priceSilver to set
     */
    public void setPriceSilver(final Double priceSilver) {
        this.priceSilver = priceSilver;
    }

    /**
     * @return the priceStudent
     */
    public Double getPriceStudent() {
        return this.priceStudent;
    }

    /**
     * @param priceStudent the priceStudent to set
     */
    public void setPriceStudent(final Double priceStudent) {
        this.priceStudent = priceStudent;
    }

    /**
     * @return the priceU16
     */
    public Double getPriceU16() {
        return this.priceU16;
    }

    /**
     * @param priceU16 the priceU16 to set
     */
    public void setPriceU16(final Double priceU16) {
        this.priceU16 = priceU16;
    }

    /**
     * @return the ticketsForOffer
     */
    public Byte getTicketsForOffer() {
        return this.ticketsForOffer;
    }

    /**
     * @param ticketsForOffer the ticketsForOffer to set
     */
    public void setTicketsForOffer(final Byte ticketsForOffer) {
        this.ticketsForOffer = ticketsForOffer;
    }

    /**
     * @return the timeBegin
     */
    public Date getTimeBegin() {
        return this.timeBegin;
    }

    /**
     * @param timeBegin the timeBegin to set
     */
    public void setTimeBegin(final Date timeBegin) {
        this.timeBegin = timeBegin;
    }

    /**
     * @return the timeEnd
     */
    public Date getTimeEnd() {
        return this.timeEnd;
    }

    /**
     * @param timeEnd the timeEnd to set
     */
    public void setTimeEnd(final Date timeEnd) {
        this.timeEnd = timeEnd;
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
