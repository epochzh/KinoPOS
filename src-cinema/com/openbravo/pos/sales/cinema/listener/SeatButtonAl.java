package com.openbravo.pos.sales.cinema.listener;

import com.openbravo.pos.sales.cinema.CinemaReservationMap;
import com.openbravo.pos.sales.cinema.model.Seat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 */
public class SeatButtonAl implements ActionListener {

    /**
     */
    private final CinemaReservationMap panel;

    /**
     */
    private final Seat seat;

    /**
     * @param panel
     * @param seat
     */
    public SeatButtonAl(final CinemaReservationMap panel, final Seat seat) {
        super();
        this.panel = panel;
        this.seat = seat;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        this.panel.onSeatAction(this.seat);
    }
}
