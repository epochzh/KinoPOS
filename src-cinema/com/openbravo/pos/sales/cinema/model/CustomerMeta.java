package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 */
public class CustomerMeta implements IKeyed, Serializable, SerializableRead {

    /**
     */
    public static final DateFormat DATE_FORMAT_BIRTHDATE =
        new SimpleDateFormat("MM-dd-yyyy");

    /**
     */
    public static final DateFormat DATE_FORMAT_MS = new SimpleDateFormat(
        "yyyy-MM-dd");

    /**
     */
    private static final long serialVersionUID = -6887998750340699182L;

    /**
	 */
    private Long id;

    /**
     */
    private String metaKey;

    /**
     */
    private String metaValue;

    /**
     */
    private Long userId;

    /**
	 */
    public CustomerMeta() {
        super();
    }

    /**
     * @param id
     */
    public CustomerMeta(final Long id) {
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
        this.metaKey = dr.getString(++index);
        this.metaValue = dr.getString(++index);
        this.userId = dr.getLong(++index);
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
     * @return the metaKey
     */
    public String getMetaKey() {
        return this.metaKey;
    }

    /**
     * @param metaKey the metaKey to set
     */
    public void setMetaKey(final String metaKey) {
        this.metaKey = metaKey;
    }

    /**
     * @return the metaValue
     */
    public String getMetaValue() {
        return this.metaValue;
    }

    /**
     * @param metaValue the metaValue to set
     */
    public void setMetaValue(final String metaValue) {
        this.metaValue = metaValue;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return this.userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}
