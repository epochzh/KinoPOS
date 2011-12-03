package com.openbravo.pos.sales.cinema.model;

/**
 */
public enum BookingState {

    /**
     * Indicates that a seat is locked because currently purchased on a POS.
     */
    LOCKED("locked"),

    /**
     * Indicates that a seat is reserved but has not been purchased yet.
     */
    RESERVED("reserved"),

    /**
     * Indicates that a seat has been purchased.
     */
    TAKEN("taken");

    /**
     * @param state
     * @return the {@link BookingState} associated to the specified state
     */
    public static BookingState fromState(final String state) {
        for (final BookingState bookingState : values()) {
            if (bookingState.state.equals(state)) {
                return bookingState;
            }
        }

        throw new IllegalArgumentException("state: " + state);
    }

    /**
     */
    private final String state;

    /**
     * @param state
     */
    private BookingState(final String state) {
        this.state = state;
    }

    /**
     * @return the state
     */
    public String getState() {
        return this.state;
    }
}
