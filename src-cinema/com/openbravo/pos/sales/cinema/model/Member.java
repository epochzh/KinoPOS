package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class Member implements IKeyed, Serializable, SerializableRead {

    /**
     */
    private static final long serialVersionUID = -881663203933608241L;

    /**
     */
    private Integer id;

    /**
     */
    private String firstName;

    /**
     */
    private String lastName;

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
    private String memberShipType;

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
    private Timestamp registeredDate;


    /**
	 */
    public Member() {
        super();
    }

    /**
     * @param id
     */
    public Member(final Integer id) {
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
        final Member other = (Member) obj;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		if(firstName == null){
			return "";
		}else{
			return firstName;
		}
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		if(lastName == null){
			return "";
		}else{
			return lastName;
		}
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		if(address1 == null){
			return "";
		}else{
			return address1;
		}
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		if(address2 == null){
			return "";
		}else{
			return address2;
		}
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		if(city == null){
			return "";
		}else{
			return city;
		}
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		if(postcode == null){
			return "";
		}else{
			return postcode;
		}
	}

	/**
	 * @param postcode the postcode to set
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	

	/**
	 * @return the registeredDate
	 */
	public Timestamp getRegisteredDate() {
		return registeredDate;
	}

	/**
	 * @param registeredDate the registeredDate to set
	 */
	public void setRegisteredDate(Timestamp registeredDate) {
		this.registeredDate = registeredDate;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		if(telephone == null){
			return "";
		}else{
			return telephone;
		}
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the memberShipType
	 */
	public String getMemberShipType() {
		return memberShipType;
	}

	/**
	 * @param memberShipType the memberShipType to set
	 */
	public void setMemberShipType(String memberShipType) {
		this.memberShipType = memberShipType;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		if(mobile == null){
			return "";
		}else{
			return mobile;
		}
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the dob
	 */
	public String getDob() {
		if(dob == null){
			return "";
		}else{
			return dob;
		}
	}

	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}

	 /**
     * @see com.openbravo.data.loader.SerializableRead#readValues(com.openbravo.data.loader.DataRead)
     */
    @Override
    public void readValues(final DataRead dr) throws BasicException {
        int index = 0;
        this.id = dr.getInt(++index);
    }
	
	public Boolean requiredFields()
	{
		if(this.firstName != null && this.lastName != null && this.memberShipType != null
			&& this.address1 != null && this.city != null && this.postcode != null && this.telephone != null && this.dob != null){
			return true;
		}else{
			return false;
		}
	}
	
	public Map<String, String> populateMap()
	{
		Map<String, String> meta = new HashMap<String, String>();
		long timestamp = System.currentTimeMillis()/1000;
		String time = String.valueOf(timestamp);
				
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
		meta.put("ym_account_type_join_date", time);
		meta.put("ym_custom_fields", this.createCustomFields());
		meta.put("ym_user", this.createUserFields());
		
		
		
		
		if(this.getFirstName() != null)
		{
			meta.put("first_name", this.getFirstName());
		}
		if(this.getLastName() != null)
		{
			meta.put("last_name", this.getLastName());
		}
		return meta;
	}
	
	
	/**
	 * returns the nickname
	 */
	public String getNickname()
	{
		return this.firstName+this.lastName;
	}
	
	/*
	 * create the custom fields string
	 */
	private String createCustomFields()
	{
		String pin = String.format("%012d", this.getId());
		//TODO generate pin
		String customFields = "a:12:{i:17;s:9:\""+ pin +"\"; " +
							  "i:12;s:3:\""+ this.getFirstName() +"\";" +
							  "i:13;s:3:\""+ this.getLastName() +"\";" +
							  "i:4;s:10:\""+ this.getDob() +"\";" +
							  "i:15;s:2:\"No\";" +
							  "i:7;s:19:\"" + this.getAddress1() + "\";" +
							  "i:8;s:12:\"" + this.getAddress2() + "\";" +
							  "i:9;s:7:\"" + this.getCity() + "\";" +
							  "i:10;s:7:\"" + this.getPostcode() + "\";" +
							  "i:11;s:10:\"" + this.getTelephone() + "\";" +
							  "i:14;s:11:\"" + this.getMobile() + "\";" +
							  "i:16;s:1:\"0\";}";
		
		return customFields;
	}
	
	
	/*
	 * create the custom fields string
	 */
	private String createUserFields()
	{
		
		//TODO generate pin
		String userFields = "O:15:\"YourMember_User\":11:{" +
							"s:8:\"duration\";s:1:\"1\";" +
							"s:13:\"duration_type\";s:1:\"y\";" +
							"s:6:\"amount\";s:4:\"0.10\";" +
							"s:8:\"currency\";s:3:\"GBP\";" +
							"s:13:\"last_pay_date\";s:10:\"" + this.getLastPayDate() + "\";" +
							"s:11:\"expire_date\";s:16:\"2012-11-23 16:04\";" +
							"s:12:\"account_type\";s:12:\"Gold Members\";" +
							"s:10:\"status_str\";s:27:\"Last payment was successful\";" +
							"s:7:\"pack_id\";s:1:\"3\";" +
							"s:12:\"gateway_used\";" +
							"s:9:\"ym_paypal\";}";
		
		return userFields;
	}
	
	private String getLastPayDate()
	{
		java.text.SimpleDateFormat sdf = 
		      new java.text.SimpleDateFormat("yyyy-mm-dd");
		Calendar c1 = Calendar.getInstance(); 
		c1.add(Calendar.YEAR,1);
		
		return sdf.format(c1.getTime());
	}
    
    
    
    
    

    
}
