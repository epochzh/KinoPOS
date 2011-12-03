package com.openbravo.pos.sales.cinema.listener;

import com.openbravo.pos.sales.cinema.BookingPopup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 */
public class BookedTicketDetailsAl implements ActionListener {

    /**
	 */
    private final BookingPopup popup;

    /**
     * @param popup
     */
    public BookedTicketDetailsAl(final BookingPopup popup) {
        this.popup = popup;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        this.popup.print();
    }
}
