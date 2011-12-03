package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class Booking implements IKeyed, Serializable, SerializableRead {

    /**
     */
    private static final long serialVersionUID = -881663203933608241L;

    /**
     */
    private String barcode;

    /**
     */
    private Boolean collected;

    /**
     */
    private Customer customer;

    /**
     */
    private String customerName;

    /**
     */
    private String customerPhone;

    /**
     */
    private Date date;

    /**
     */
    private Event event;

    /**
	 */
    private Long id;

    /**
     */
    private Double price;

    /**
     */
    private PriceMatrix priceMatrix;

    /**
     */
    private PriceType priceType;

    /**
     */
    private String salesChannel;

    /**
     */
    private String seatCoordinates;

    /**
	 */
    private BookingState state;

    /**
	 */
    public Booking() {
        super();
    }

    /**
     * @param id
     */
    public Booking(final Long id) {
        this.id = id;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Booking other = (Booking) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @see com.openbravo.data.loader.IKeyed#getKey()
     */
    @Override
    public Object getKey() {
        return this.id;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
            (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    /**
     * @see com.openbravo.data.loader.SerializableRead#readValues(com.openbravo.data.loader.DataRead)
     */
    @Override
    public void readValues(final DataRead dr) throws BasicException {
        int index = 0;
        this.barcode = dr.getString(++index);
        this.collected = dr.getBoolean(++index);
        if (dr.getLong(++index) != null) {
            this.customer = new Customer(dr.getLong(index));
        }
        this.customerName = dr.getString(++index);
        this.customerPhone = dr.getString(++index);
        this.date = dr.getTimestamp(++index);
        this.event = new Event(dr.getLong(++index));
        this.id = dr.getLong(++index);
        this.price = dr.getDouble(++index);
        this.priceMatrix = new PriceMatrix(dr.getLong(++index));
        this.priceType = PriceType.fromType(dr.getString(++index));
        this.salesChannel = dr.getString(++index);
        this.seatCoordinates = dr.getString(++index);
        this.state = BookingState.fromState(dr.getString(++index));
    }

    /**
     * @return the barcode
     */
    public String getBarcode() {
        return this.barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the collected
     */
    public Boolean getCollected() {
        return this.collected;
    }

    /**
     * @param collected the collected to set
     */
    public void setCollected(final Boolean collected) {
        this.collected = collected;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return this.customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return this.customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(final String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the customerPhone
     */
    public String getCustomerPhone() {
        return this.customerPhone;
    }

    /**
     * @param customerPhone the customerPhone to set
     */
    public void setCustomerPhone(final String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(final Date date) {
        this.date = date;
    }

    /**
     * @return the event
     */
    public Event getEvent() {
        return this.event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(final Event event) {
        this.event = event;
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
     * @return the priceMatrix
     */
    public PriceMatrix getPriceMatrix() {
        return this.priceMatrix;
    }

    /**
     * @param priceMatrix the priceMatrix to set
     */
    public void setPriceMatrix(final PriceMatrix priceMatrix) {
        this.priceMatrix = priceMatrix;
    }

    /**
     * @return the priceType
     */
    public PriceType getPriceType() {
        return this.priceType;
    }

    /**
     * @param priceType the priceType to set
     */
    public void setPriceType(final PriceType priceType) {
        this.priceType = priceType;
    }

    /**
     * @return the salesChannel
     */
    public String getSalesChannel() {
        return this.salesChannel;
    }

    /**
     * @param salesChannel the salesChannel to set
     */
    public void setSalesChannel(final String salesChannel) {
        this.salesChannel = salesChannel;
    }

    /**
     * @return the seatCoordinates
     */
    public String getSeatCoordinates() {
        return this.seatCoordinates;
    }

    /**
     * @param seatCoordinates the seatCoordinates to set
     */
    public void setSeatCoordinates(final String seatCoordinates) {
        this.seatCoordinates = seatCoordinates;
    }

    /**
     * @return the state
     */
    public BookingState getState() {
        return this.state;
    }

    /**
     * @param state the state to set
     */
    public void setState(final BookingState state) {
        this.state = state;
    }
}
