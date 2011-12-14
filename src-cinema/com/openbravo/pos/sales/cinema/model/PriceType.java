package com.openbravo.pos.sales.cinema.model;

/**
 */
public enum PriceType {

    /**
     */
    FULL_PRICE("full_price"),

    /**
     */
    GOLD("gold"),

    /**
     */
    KINO_FRIENDS("kino_friends"),

    /**
     */
    KINO_STAFF("kino_staff"),

    /**
     */
    SENIOR("senior"),

    /**
     */
    SILVER("silver"),

    /**
     */
    STUDENT("student"),

    /**
     */
    U16("u16"),
    
    /**
     * 
     */
    DOUBLE_BILL("double_bill");
    


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
    private final String type;

    /**
     * @param type
     */
    private PriceType(final String type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }
}
