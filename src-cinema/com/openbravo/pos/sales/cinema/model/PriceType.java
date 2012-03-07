package com.openbravo.pos.sales.cinema.model;

/**
 */
public enum PriceType {

    /**
     */
    FIRST_FILM("FF", "first_film"),

    /**
     */
    FREE("FR", "free"),

    /**
     */
    FULL_PRICE("AD", "full_price"),

    /**
     */
    GOLD("ME", "gold"),

    /**
     */
    KINO_FRIENDS("", "kino_friends"),

    /**
     */
    KINO_STAFF("", "kino_staff"),

    /**
     */
    SENIOR("SE", "senior"),

    /**
     */
    SILVER("ME", "silver"),

    /**
     */
    STUDENT("ST", "student"),

    /**
     */
    U16("CH", "u16"),

    /**
     */
    DOUBLE_BILL("DB", "double_bill");

    /**
     * @param type
     * @return the PriceType associated to the specified type
     */
    public static PriceType fromType(final String type) {
        for (final PriceType priceType : values()) {
            // if (priceType.type.equals(type)) {
            /*
             * TODO: This replace hack is there only because of data
             * inconsistency in the test data.
             */
            if (priceType.type.equals(type.replace(' ', '_'))) {
                return priceType;
            }
        }

        throw new IllegalArgumentException("type: " + type);
    }

    /**
     */
    private final String abbreviation;

    /**
     */
    private final String type;

    /**
     * @param abbreviation
     * @param type
     */
    private PriceType(final String abbreviation, final String type) {
        this.abbreviation = abbreviation;
        this.type = type;
    }

    /**
     * @return the abbreviation
     */
    public String getAbbreviation() {
        return this.abbreviation;
    }

    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }
}
