package com.openbravo.pos.sales.cinema;

import java.util.List;

import javax.swing.AbstractListModel;

/**
 */
class MyListData extends AbstractListModel {

    /**
     */
    private static final long serialVersionUID = -5224730163173970165L;

    /**
     */
    private final List<?> m_data;

    /**
     * @param data
     */
    public MyListData(final List<?> data) {
        this.m_data = data;
    }

    /**
     * @see javax.swing.ListModel#getElementAt(int)
     */
    @Override
    public Object getElementAt(final int index) {
        return this.m_data.get(index);
    }

    /**
     * @see javax.swing.ListModel#getSize()
     */
    @Override
    public int getSize() {
        return this.m_data.size();
    }
}