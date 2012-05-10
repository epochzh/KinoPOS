package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;

/**
 */
public class Expense implements IKeyed, Serializable, SerializableRead {

    /**
     */
    private static final long serialVersionUID = -881663203933608241L;

    /**
     */
    private Integer id;

    /**
     */
    private String name;

    /**
     */
    private String supplier;

    /**
     */
    private String amount;

    /**
     * @return the id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final Integer id) {
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
     * @return the supplier
     */
    public String getSupplier() {
        return this.supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(final String supplier) {
        this.supplier = supplier;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return this.amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(final String amount) {
        this.amount = amount;
    }

    /**
     * @see com.openbravo.data.loader.SerializableRead#readValues(com.openbravo.data.loader.DataRead)
     */
    @Override
    public void readValues(final DataRead dr) throws BasicException {
        int index = 0;
        this.amount = dr.getString(++index);
        this.id = dr.getInt(++index);
        this.name = dr.getString(++index);
        this.supplier = dr.getString(++index);
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
     * @return
     */
    public Boolean requiredFields() {
        if ((this.name != null) && (this.supplier != null)
            && (this.amount != null)) {
            return true;
        }

        return false;
    }
}
