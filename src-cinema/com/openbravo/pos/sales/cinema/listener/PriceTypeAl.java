package com.openbravo.pos.sales.cinema.listener;

import com.openbravo.pos.sales.cinema.CinemaReservationMap;
import com.openbravo.pos.sales.cinema.model.PriceType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

/**
 */
public class PriceTypeAl implements ActionListener {

    /**
	 */
    private final CinemaReservationMap panel;

    /**
	 */
    private final PriceType priceType;

    /**
     * @param panel
     * @param priceType
     */
    public PriceTypeAl(final CinemaReservationMap panel,
    final PriceType priceType) {
        this.panel = panel;
        this.priceType = priceType;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        final JToggleButton button = (JToggleButton) event.getSource();
        if (button.isSelected()) {
            this.panel.onPriceTypeAction(this.priceType);
        } else {
            this.panel.onPriceTypeAction(null);
        }
    }
}
