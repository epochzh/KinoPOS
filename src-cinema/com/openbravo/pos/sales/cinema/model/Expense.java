package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.pos.sales.cinema.CinemaDaoImpl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 */
public class Expense implements IKeyed, Serializable, SerializableRead {

	
	/**
     */
	private static final long serialVersionUID = -881663203933608241L;

	/**
     */
	private static final Logger LOGGER = Logger.getLogger(CinemaDaoImpl.class
			.getName());

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
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the supplier
	 */
	public String getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	
	
	
	
	/**
	 * @see com.openbravo.data.loader.SerializableRead#readValues(com.openbravo.data.loader.DataRead)
	 */
	@Override
	public void readValues(final DataRead dr) throws BasicException {
		int index = 0;
		this.id = dr.getInt(++index);
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
		result = (prime * result)
				+ ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}
	
	
	public Boolean requiredFields() {
		if ((this.name != null) && (this.supplier != null)
				&& (this.amount != null)) {
			return true;
		} else {
			return false;
		}
	}
	
	
}