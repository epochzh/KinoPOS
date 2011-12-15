package com.openbravo.pos.sales.cinema.model;

import com.openbravo.pos.sales.cinema.CinemaReservationMap;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
public enum EventType {

    /**
     */
    FILM("film"),

    /**
     */
    MAINTENANCE("maintenance"),

    /**
     */
    EVENT1("event1"),

    /**
     */
    EVENT2("event2"),

    /**
     */
    EVENT3("event3"),

    /**
     */
    UNKNOWN(null);

    /**
     */
    private static final Logger LOGGER = Logger
        .getLogger(CinemaReservationMap.class.getName());

    /**
     * @param type
     * @return the EventType associated to the specified type
     */
    public static EventType fromType(final String type) {
        for (final EventType eventType : values()) {
            if (eventType.type.equals(type)) {
                return eventType;
            }
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("type: " + type);
        }

        return UNKNOWN;
    }

    /**
     */
    private final String type;

    /**
     * @param type
     */
    private EventType(final String type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }
}
