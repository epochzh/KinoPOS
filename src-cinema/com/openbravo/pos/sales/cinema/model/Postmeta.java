package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;

/**
 */
public class Postmeta implements IKeyed, Serializable, SerializableRead {

    /**
     */
    private static final long serialVersionUID = -681663203933608241L;

    /**
     */
    private Integer meta_id;

    /**
     */
    private String meta_key;

    /**
     */
    private String meta_value;
    
    
    /**
     */
    public Postmeta() {
        super();
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return this.meta_id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final Integer id) {
        this.meta_id = id;
    }

    /**
     * @return the meta key
     */
    public String getMetaKey() {
        return this.meta_key;
    }

    /**
     * @param set the meta key
     */
    public void setMetaKey(final String key) {
        this.meta_key = key;
    }
    
    /**
     * @return the meta value
     */
    public String getMetaValue() {
        return this.meta_value;
    }

    /**
     * @param set the meta value
     */
    public void setMetaValue(final String value) {
        this.meta_value = value;
    }

    /**
     * @see com.openbravo.data.loader.SerializableRead#readValues(com.openbravo.data.loader.DataRead)
     */
    @Override
    public void readValues(final DataRead dr) throws BasicException {
        int index = 0;
        this.meta_id = dr.getInt(++index);
        this.meta_key = dr.getString(++index);
        this.meta_value = dr.getString(++index);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.meta_key;
    }

    /**
     * @see com.openbravo.data.loader.IKeyed#getKey()
     */
    @Override
    public Object getKey() {
        return this.meta_id;
    }
}
