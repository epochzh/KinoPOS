package com.openbravo.pos.sales.cinema.model;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class Customer implements IKeyed, Serializable, SerializableRead {

    /**
     */
    private static final long serialVersionUID = 8141689385295512403L;

    /**
     */
    private String address1;

    /**
     */
    private String address2;

    /**
     */
    private Date birthdate;

    /**
     */
    private String city;

    /**
     */
    private String email;

    /**
     */
    private String firstName;

    /**
	 */
    private Long id;

    /**
     */
    private String lastName;

    /**
     */
    private Double msAmount;

    /**
     */
    private String msCurrency;

    /**
     */
    private Date msBegin;

    /**
     */
    private Date msEnd;

    /**
     */
    private MembershipType msType;

    /**
     */
    private Integer msDuration;

    /**
     */
    private Character msDurationType;

    /**
     */
    private String msGateway;

    /**
     */
    private Integer msPackId;

    /**
     */
    private String msStatusString;

    /**
     */
    private String phoneNumber;

    /**
     */
    private String pin;

    /**
     */
    private Boolean student;

    /**
	 */
    public Customer() {
        super();
    }

    /**
     * @param id
     */
    public Customer(final Long id) {
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
        this.email = dr.getString(++index);
        this.id = dr.getLong(++index);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        sb.append(" id=").append(this.id);
        sb.append(" firstName=").append(this.firstName);
        sb.append(" lastName=").append(this.lastName);
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * @return the address1
     */
    public String getAddress1() {
        return this.address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(final String address1) {
        this.address1 = address1;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return this.address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

    /**
     * @return the birthdate
     */
    public Date getBirthdate() {
        return this.birthdate;
    }

    /**
     * @param birthdate the birthdate to set
     */
    public void setBirthdate(final Date birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
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
     * @return the lastName
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the msAmount
     */
    public Double getMsAmount() {
        return this.msAmount;
    }

    /**
     * @param msAmount the msAmount to set
     */
    public void setMsAmount(final Double msAmount) {
        this.msAmount = msAmount;
    }

    /**
     * @return the msCurrency
     */
    public String getMsCurrency() {
        return this.msCurrency;
    }

    /**
     * @param msCurrency the msCurrency to set
     */
    public void setMsCurrency(final String msCurrency) {
        this.msCurrency = msCurrency;
    }

    /**
     * @return the msBegin
     */
    public Date getMsBegin() {
        return this.msBegin;
    }

    /**
     * @param msBegin the msBegin to set
     */
    public void setMsBegin(final Date msBegin) {
        this.msBegin = msBegin;
    }

    /**
     * @return the msEnd
     */
    public Date getMsEnd() {
        return this.msEnd;
    }

    /**
     * @param msEnd the msEnd to set
     */
    public void setMsEnd(final Date msEnd) {
        this.msEnd = msEnd;
    }

    /**
     * @return the msType
     */
    public MembershipType getMsType() {
        return this.msType;
    }

    /**
     * @param msType the msType to set
     */
    public void setMsType(final MembershipType msType) {
        this.msType = msType;
    }

    /**
     * @return the msDuration
     */
    public Integer getMsDuration() {
        return this.msDuration;
    }

    /**
     * @param msDuration the msDuration to set
     */
    public void setMsDuration(final Integer msDuration) {
        this.msDuration = msDuration;
    }

    /**
     * @return the msDurationType
     */
    public Character getMsDurationType() {
        return this.msDurationType;
    }

    /**
     * @param msDurationType the msDurationType to set
     */
    public void setMsDurationType(final Character msDurationType) {
        this.msDurationType = msDurationType;
    }

    /**
     * @return the msGateway
     */
    public String getMsGateway() {
        return this.msGateway;
    }

    /**
     * @param msGateway the msGateway to set
     */
    public void setMsGateway(final String msGateway) {
        this.msGateway = msGateway;
    }

    /**
     * @return the msPackId
     */
    public Integer getMsPackId() {
        return this.msPackId;
    }

    /**
     * @param msPackId the msPackId to set
     */
    public void setMsPackId(final Integer msPackId) {
        this.msPackId = msPackId;
    }

    /**
     * @return the msStatusString
     */
    public String getMsStatusString() {
        return this.msStatusString;
    }

    /**
     * @param msStatusString the msStatusString to set
     */
    public void setMsStatusString(final String msStatusString) {
        this.msStatusString = msStatusString;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the pin
     */
    public String getPin() {
        return this.pin;
    }

    /**
     * @param pin the pin to set
     */
    public void setPin(final String pin) {
        this.pin = pin;
    }

    /**
     * @return the student
     */
    public Boolean getStudent() {
        return this.student;
    }

    /**
     * @param student the student to set
     */
    public void setStudent(final Boolean student) {
        this.student = student;
    }
}
