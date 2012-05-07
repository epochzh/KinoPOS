package com.openbravo.pos.sales.cinema;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 */
public class CustomerPopupBookingTCM extends DefaultTableColumnModel {

    /**
     */
    private static final long serialVersionUID = -5843923090503522417L;

    /**
     */
    public CustomerPopupBookingTCM() {
        super();
    }

    /**
     * @see javax.swing.table.DefaultTableColumnModel#getColumn(int)
     */
    @Override
    public TableColumn getColumn(final int columnIndex) {
        final TableColumn column = super.getColumn(columnIndex);

        if (columnIndex == 0) {
            column.setWidth(140);
        } else if (columnIndex == 1) {
            column.setWidth(130);
        } else if (columnIndex == 2) {
            column.setWidth(40);
        } else if (columnIndex == 3) {
            column.setWidth(72);
        } else {
            throw new IllegalArgumentException("columnIndex: " + columnIndex);
        }

        return column;
    }
}
