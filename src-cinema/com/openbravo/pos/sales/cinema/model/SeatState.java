package com.openbravo.pos.sales.cinema.model;

/**
 */
public enum SeatState {

    /**
     * A non-bookable seat representing an exit.
     */
    EXIT((byte) 3),

    /**
     * A non-bookable seat.
     */
    NO_SEAT((byte) 0),

    /**
     * A bookable seat.
     */
    STANDARD((byte) 1),

    /**
     * A bookable seat which can be removed for a wheelchair.
     */
    WHEELCHAIR((byte) 2),
    
    /**
     * A bookable seat which can be removed for a wheelchair.
     */
    GAP((byte) 4);

    /**
     * @param state
     * @return the SeatState associated to the specified sate
     */
    public static SeatState fromState(final byte state) {
        for (final SeatState seatState : values()) {
            if (seatState.state == state) {
                return seatState;
            }
        }

        throw new IllegalArgumentException("state: " + state);
    }

    /**
     */
    private final byte state;

    /**
     * @param state
     */
    private SeatState(final byte state) {
        this.state = state;
    }

    /**
     * @return the state
     */
    public byte getState() {
        return this.state;
    }
}
