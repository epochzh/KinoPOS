package com.openbravo.pos.sales.cinema.model;

import com.openbravo.pos.sales.cinema.CinemaReservationMap;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
public enum MembershipType {

    /**
     */
    GOLD("Gold members"),

    /**
     */
    SILVER("Silver members");

    /**
     */
    private static final Logger LOGGER = Logger
        .getLogger(CinemaReservationMap.class.getName());

    /**
     * @param type
     * @return the MembershipType associated to the specified type
     */
    public static MembershipType fromType(final String type) {
        for (final MembershipType membershipType : values()) {
            if (membershipType.type.equals(type)) {
                return membershipType;
            }
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("type: " + type);
        }

        return null;
    }

    /**
     */
    private final String type;

    /**
     * @param type
     */
    private MembershipType(final String type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }
}
