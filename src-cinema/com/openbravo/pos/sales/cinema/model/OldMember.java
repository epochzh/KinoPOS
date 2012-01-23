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
public class OldMember implements IKeyed, Serializable, SerializableRead {

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
	private String association = "0";

	/**
     */
	private String firstName;

	/**
     */
	private String firstName2;

	/**
     */
	private String lastName;

	/**
     */
	private String oldPin;
	/**
     */
	private String oldPin2;

	/**
     */
	private String expiry;
	
	/*
	 * 
	 */
	private String lastPayDate;

	/**
     */
	private Timestamp explarationDate;

	/**
     */
	private String lastName2;

	/**
     */
	private String address1;

	/**
     */
	private String address2;

	/**
     */
	private String city;

	/**
     */
	private String freeTickets = "2";

	/**
     */
	private String memberShipType;

	/**
     */
	private String packId;

	/**
     */
	private String postcode;

	/**
     */
	private String telephone;

	/**
     */
	private String mobile;

	/**
     */
	private String dob;

	/**
     */
	private String dob2;
	
	/**
     */
	private String pin;

	/**
     */
	private String registered;

	/**
     */
	private Timestamp registeredDate;

	/**
     */
	private Timestamp expirationDate;

	/**
	 */
	public OldMember() {
		super();
	}

	/**
	 * @param id
	 */
	public OldMember(final Integer id) {
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
		final OldMember other = (OldMember) obj;
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
		result = (prime * result)
				+ ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	public String getAssociation() {
		return this.association;
	}

	/**
	 * @param association
	 *            of joint membership
	 */
	public void setAssociation(final String association) {
		this.association = association;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		if (this.firstName == null) {
			return "";
		} else {
			return this.firstName;
		}
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName2(final String firstName2) {
		this.firstName2 = firstName2;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName2() {
		if (this.firstName2 == null) {
			return "";
		} else {
			return this.firstName2;
		}
	}

	/**
	 * @return the firstName
	 */
	public String getFreeTickets() {
		return this.freeTickets;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFreeTickets(final String freeTickets) {
		this.freeTickets = freeTickets;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		if (this.lastName == null) {
			return "";
		} else {
			return this.lastName;
		}
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName2() {
		if (this.lastName2 == null) {
			return "";
		} else {
			return this.lastName2;
		}
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName2(final String lastName2) {
		this.lastName2 = lastName2;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		if (this.address1 == null) {
			return "";
		} else {
			return this.address1;
		}
	}

	/**
	 * @param address1
	 *            the address1 to set
	 */
	public void setAddress1(final String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		if (this.address2 == null) {
			return "";
		} else {
			return this.address2;
		}
	}
	
	

	/**
	 * @return the registered
	 */
	public String getRegistered() {
		return registered;
	}

	/**
	 * @param registered the registered to set
	 */
	public void setRegistered(String registered) {
		this.registered = registered;
	}

	/**
	 * @param address2
	 *            the address2 to set
	 */
	public void setAddress2(final String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		if (this.city == null) {
			return "";
		} else {
			return this.city;
		}
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(final String city) {
		this.city = city;
	}

	/**
	 * @return the packId
	 */
	public String getPackId() {
		return this.packId;
	}

	/**
	 * @param packId
	 *            the packId to set
	 */
	public void setPackId(final String packId) {
		this.packId = packId;
	}

	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		if (this.postcode == null) {
			return "";
		} else {
			return this.postcode;
		}
	}

	/**
	 * @param postcode
	 *            the postcode to set
	 */
	public void setPostcode(final String postcode) {
		this.postcode = postcode;
	}

	/**
	 * @return the registeredDate
	 */
	public Timestamp getRegisteredDate() {
		return this.registeredDate;
	}

	/**
	 * @param registeredDate
	 *            the registeredDate to set
	 */
	public void setRegisteredDate(final Timestamp registeredDate) {
		this.registeredDate = registeredDate;
	}

	/**
	 * @return the registeredDate
	 */
	public Timestamp getExpirationDate() {
		return this.expirationDate;
	}

	/**
	 * @param registeredDate
	 *            the registeredDate to set
	 */
	public void setExpirationDate(final Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		if (this.telephone == null) {
			return "";
		} else {
			return this.telephone;
		}
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(final String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the memberShipType
	 */
	public String getMemberShipType() {
		return this.memberShipType;
	}

	/**
	 * @param memberShipType
	 *            the memberShipType to set
	 */
	public void setMemberShipType(final String memberShipType) {
		if (memberShipType == "gold membership") {
			this.memberShipType = "Gold Members";
			this.setPackId("3");
		} else if (memberShipType == "silver membership") {
			this.memberShipType = "Silver Members";
			this.setPackId("2");
		} else if (memberShipType == "Joint Silver Membership") {
			this.memberShipType = "Joint Silver Members";
			this.setPackId("5");
		}
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		if (this.mobile == null) {
			return "";
		} else {
			return this.mobile;
		}
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the dob
	 */
	public String getDob() {
		if (this.dob == null) {
			return "";
		} else {
			return this.dob;
		}
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob2(final String dob2) {
		this.dob2 = dob2;
	}

	/**
	 * @return the dob
	 */
	public String getDob2() {
		if (this.dob2 == null) {
			return "";
		} else {
			return this.dob2;
		}
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(final String dob) {
		this.dob = dob;
	}
	
	
	
	

	/**
	 * @return the pin
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * @param pin the pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * @return the lastPayDate
	 */
	public String getLastPayDate() {
		return lastPayDate;
	}

	/**
	 * @param lastPayDate the lastPayDate to set
	 */
	public void setLastPayDate(String lastPayDate) {
		this.lastPayDate = lastPayDate;
	}

	/**
	 * @see com.openbravo.data.loader.SerializableRead#readValues(com.openbravo.data.loader.DataRead)
	 */
	@Override
	public void readValues(final DataRead dr) throws BasicException {
		int index = 0;
		this.id = dr.getInt(++index);
	}

	public Boolean requiredFields() {
		if ((this.firstName != null) && (this.lastName != null)
				&& (this.oldPin != null) && (this.expiry != null)) {
			return true;
		} else {
			return false;
		}
	}

	public Map<String, String> populateMap() {
		final Map<String, String> meta = new HashMap<String, String>();
		final String time = String.valueOf(this.getRegisteredDate());

		meta.put("ym_account_type_join_date", time);
		meta.put("ym_account_type", this.getMemberShipType());
		meta.put("wp_capabilities", "a:1:{s:10:\"subscriber\";s:1:\"1\";}");
		meta.put("wp_user_level", "0");
		meta.put("nickname", this.getNickname());
		meta.put("description", "");
		meta.put("rich_editing", "true");
		meta.put("comment_shortcuts", "false");
		meta.put("admin_color", "fresh");
		meta.put("use_ssl", "0");
		meta.put("show_admin_bar_front", "true");
		meta.put("show_admin_bar_front", "true");
		meta.put("ym_status", "Active");
		meta.put("oldPin", this.getOldPin());
		meta.put("ym_custom_fields", this.createCustomFields());
		LOGGER.info("Meta: " + meta.get("ym_custom_fields"));
		meta.put("ym_user", this.createUserFields());

		if (this.getFirstName() != null) {
			meta.put("first_name", this.getFirstName());
		}
		if (this.getLastName() != null) {
			meta.put("last_name", this.getLastName());
		}
		LOGGER.info("populate complete");
		return meta;
	}

	/**
	 * returns the nickname
	 */
	public String getNickname() {
		final String concactName = this.firstName + this.lastName;
		return concactName.replace(" ", "");
	}

	/*
	 * create the custom fields string
	 */
	private String createCustomFields() {
		this.setPin(String.format("%09d", this.getId()));
		// TODO generate pin
		final String customFields = "a:12:{i:17;s:12:\"" + this.getPin() + "\";"
				+ "i:12;s:" + String.valueOf(this.getFirstName().length())
				+ ":\"" + this.getFirstName() + "\";" + "i:13;s:"
				+ String.valueOf(this.getLastName().length()) + ":\""
				+ this.getLastName() + "\";" + "i:4;s:"
				+ String.valueOf(this.getDob().length()) + ":\""
				+ this.getDob() + "\";" + "i:15;s:2:\"No\";" + "i:7;s:"
				+ String.valueOf(this.getAddress1().length()) + ":\""
				+ this.getAddress1() + "\";" + "i:8;s:"
				+ String.valueOf(this.getAddress2().length()) + ":\""
				+ this.getAddress2() + "\";" + "i:9;s:"
				+ String.valueOf(this.getCity().length()) + ":\""
				+ this.getCity() + "\";" + "i:10;s:"
				+ String.valueOf(this.getPostcode().length()) + ":\""
				+ this.getPostcode() + "\";" + "i:11;s:"
				+ String.valueOf(this.getTelephone().length()) + ":\""
				+ this.getTelephone() + "\";" + "i:14;s:"
				+ String.valueOf(this.getMobile().length()) + ":\""
				+ this.getMobile() + "\";" + "i:16;s:1:\"0\";" + "i:19;s:1:\""
				+ this.getFreeTickets() + "\";" + "i:20;s:"
				+ String.valueOf(this.getAssociation().length()) + ":\""
				+ this.getAssociation() + "\";}";

		return customFields;
	}

	/*
	 * create the custom fields string
	 */
	private String createUserFields() {
		LOGGER.info("Userfields: " + this.getMemberShipType()
				+ this.getPackId());
		final String userMemberFields = "O:8:\"stdClass\":11:{"
				+ "s:6:\"scalar\";s:0:\"\";" + "s:8:\"duration\";s:1:\"1\";"
				+ "s:13:\"duration_type\";s:1:\"y\";"
				+ "s:6:\"amount\";s:1:\"0\";" + "s:8:\"currency\";s:3:\"GBP\";"
				+ "s:12:\"account_type\";s:"
				+ String.valueOf(this.getMemberShipType().length()) + ":\""
				+ this.getMemberShipType() + "\";"
				+ "s:12:\"gateway_used\";s:7:\"GiftSub\";"
				+ "s:10:\"status_str\";s:26:\"Gift Giving was Successful\";"
				+ "s:7:\"pack_id\";s:1:\"" + this.getPackId() + "\";"
				+ "s:13:\"last_pay_date\";s:10:\"" + this.getLastPayDate()
				+ "\";" + "s:11:\"expire_date\";s:16:\"" + this.getExpiry()
				+ "\";}";

		LOGGER.info("Userfields complete: " + userMemberFields);
		return userMemberFields;
	}

	/**
	 * @return the oldPin
	 */
	public String getOldPin() {
		return oldPin;
	}

	/**
	 * @param oldPin
	 *            the oldPin to set
	 */
	public void setOldPin(String oldPin) {
		this.oldPin = oldPin;
	}

	/**
	 * @return the oldPin2
	 */
	public String getOldPin2() {
		return oldPin2;
	}

	/**
	 * @param oldPin2
	 *            the oldPin2 to set
	 */
	public void setOldPin2(String oldPin2) {
		this.oldPin2 = oldPin2;
	}

	/**
	 * @return the expiry
	 */
	public String getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry
	 *            the expiry to set
	 */
	public void setExpiry(String expiry) {
		LOGGER.info("Expiry: " + expiry);
		// set registration date from a string
		String[] expiryTokens = expiry.split("/");
		String expiryDate = expiryTokens[2] + "-" + expiryTokens[1] + "-"
				+ expiryTokens[0] + " 00:00";
		this.expiry = expiryDate;
		LOGGER.info("Expiry: " + expiryDate);
		
		
		
		// minus a year
		int i = Integer.parseInt(expiryTokens[2]);
		i = i-1;
		expiryTokens[2] = Integer.toString(i);
		String registerDate = expiryTokens[2] + "-" + expiryTokens[1] + "-"
				+ expiryTokens[0] + " 00:00";
		this.setRegistered(registerDate);
		
		String lastPayDate = expiryTokens[2] + "-" + expiryTokens[1] + "-"
				+ expiryTokens[0];
		this.setLastPayDate(lastPayDate);

		LOGGER.info("Expiry: " + expiryDate);
		String str_date = expiry;
		DateFormat formatter;
		Date date;
		long time;
		formatter = new SimpleDateFormat("dd/MM/yy");
		Calendar c1 = Calendar.getInstance();

		try {
			c1.setTime(formatter.parse(str_date));
			date = (Date) formatter.parse(formatter.format(c1.getTime()));
			time = date.getTime();
			c1.add(Calendar.YEAR, -1);
			this.setRegisteredDate(new Timestamp(time));
			LOGGER.info("this is the registered date: " + this.getRegisteredDate());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
