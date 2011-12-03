package com.openbravo.pos.sales.cinema.listener;

import com.openbravo.pos.sales.cinema.CustomerNamePopup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 */
public class CustomerNameOkAl implements ActionListener {

    /**
     */
    private final CustomerNamePopup popup;

    /**
     * @param popup
     */
    public CustomerNameOkAl(final CustomerNamePopup popup) {
        super();
        this.popup = popup;
    }

    /**
     * @param event
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        this.popup.ok();
    }
}
