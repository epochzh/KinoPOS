package com.openbravo.pos.sales.cinema.listener;

import com.openbravo.pos.sales.cinema.CustomerNamePopup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 */
public class CustomerNameCancelAl implements ActionListener {

    /**
     */
    private final CustomerNamePopup popup;

    /**
     * @param popup
     */
    public CustomerNameCancelAl(final CustomerNamePopup popup) {
        super();
        this.popup = popup;
    }

    /**
     * @param event
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        this.popup.cancel();
    }
}
