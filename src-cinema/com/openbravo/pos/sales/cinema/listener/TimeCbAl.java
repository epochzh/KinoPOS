package com.openbravo.pos.sales.cinema.listener;

import com.openbravo.pos.sales.cinema.CinemaReservationMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
public class TimeCbAl implements ActionListener {

    /**
     */
    private static final Logger LOGGER = Logger.getLogger(TimeCbAl.class
        .getName());

    /**
     */
    private final CinemaReservationMap panel;

    /**
     * @param panel
     */
    public TimeCbAl(final CinemaReservationMap panel) {
        super();
        this.panel = panel;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("event: " + event.paramString());
        }

        this.panel.onTimeAction();
    }
}
