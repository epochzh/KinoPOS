package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class Event implements IKeyed, Serializable, SerializableRead {

    /**
     */
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss");

    /**
	 */
    private static final long serialVersionUID = -6852887028494873394L;

    /**
	 */
    private Date dateBegin;

    /**
	 */
    private Date dateEnd;

    /**
	 */
    private Long id;

    /**
	 */
    private String name;

    /**
	 */
    private Screen screen;

    /**
	 */
    private EventType type;

    /**
	 */
    public Event() {
        super();
    }

    /**
     * @param id
     */
    public Event(final Long id) {
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
        this.dateBegin = dr.getTimestamp(++index);
        this.dateEnd = dr.getTimestamp(++index);
        this.id = dr.getLong(++index);
        this.name = dr.getString(++index);
        this.screen = new Screen();
        this.screen.setNumber(dr.getByte(++index));
        this.type = EventType.fromType(dr.getString(++index));
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * @return the dateBegin
     */
    public Date getDateBegin() {
        return this.dateBegin;
    }

    /**
     * @param dateBegin the dateBegin to set
     */
    public void setDateBegin(final Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    /**
     * @return the dateEnd
     */
    public Date getDateEnd() {
        return this.dateEnd;
    }

    /**
     * @param dateEnd the dateEnd to set
     */
    public void setDateEnd(final Date dateEnd) {
        this.dateEnd = dateEnd;
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
     * @return the type
     */
    public EventType getType() {
        return this.type;
    }
    
    /**
     * @return the type as a string
     */
    public String getTypeAsString() {
        return this.type.getType();
    }

    /**
     * @param type the type to set
     */
    public void setType(final EventType type) {
        this.type = type;
    }
}
