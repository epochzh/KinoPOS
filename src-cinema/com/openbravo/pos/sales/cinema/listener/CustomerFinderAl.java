package com.openbravo.pos.sales.cinema.listener;

import com.openbravo.pos.sales.cinema.CinemaReservationMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 */
public class CustomerFinderAl implements ActionListener {

    /**
     */
    private final CinemaReservationMap panel;

    /**
     * @param panel
     */
    public CustomerFinderAl(final CinemaReservationMap panel) {
        super();
        this.panel = panel;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        this.panel.doCustomerSearch();
    }
}
