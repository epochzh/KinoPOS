package com.openbravo.pos.sales.cinema;

import com.openbravo.pos.sales.cinema.model.Customer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;

/**
 */
public class CustomerRenderer extends DefaultListCellRenderer {

    /**
     */
    private static final long serialVersionUID = 2206093327434701315L;

    /**
	 */
    private static Icon ICON_CUSTOMER = new ImageIcon(CustomerRenderer.class
        .getClassLoader().getResource("com/openbravo/images/kdmconfig.png"));

    /**
     */
    public CustomerRenderer() {
    }

    /**
     * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList,
     * java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(final JList list,
    final Object value, final int index, final boolean isSelected,
    final boolean cellHasFocus) {
        final Customer customer = (Customer) value;

        super.getListCellRendererComponent(list, null, index, isSelected,
            cellHasFocus);

        this.setText(customer.getFirstName() + " " + customer.getLastName());
        this.setIcon(ICON_CUSTOMER);

        return this;
    }
}
