package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;

/**
 */
public class Venue implements IKeyed, Serializable, SerializableRead {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4804469829741015739L;

    /**
	 * 
	 */
    private String color;

    /**
	 * 
	 */
    private Long id;

    /**
	 * 
	 */
    private String name;

    /**
     */
    public Venue() {
        super();
    }

    /**
     * @param id
     */
    public Venue(final Long id) {
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
        this.color = dr.getString(++index);
        this.id = dr.getLong(++index);
        this.name = dr.getString(++index);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(" id=").append(this.id);
        sb.append(" name=").append(this.name);
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * @return the color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(final String color) {
        this.color = color;
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
}
