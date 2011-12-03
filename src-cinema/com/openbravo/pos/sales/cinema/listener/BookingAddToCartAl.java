package com.openbravo.pos.sales.cinema.listener;

import com.openbravo.pos.sales.cinema.BookingPopup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 */
public class BookingAddToCartAl implements ActionListener {

    /**
	 */
    private final BookingPopup popup;

    /**
     * @param popup
     */
    public BookingAddToCartAl(final BookingPopup popup) {
        super();
        this.popup = popup;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        this.popup.addToCart();
    }
}
