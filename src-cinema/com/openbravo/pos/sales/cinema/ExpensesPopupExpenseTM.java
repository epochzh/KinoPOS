package com.openbravo.pos.sales.cinema;

import com.openbravo.pos.sales.cinema.model.Expense;

import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 */
public class ExpensesPopupExpenseTM extends AbstractTableModel {

    /**
     */
    private static final long serialVersionUID = 2346475775478631786L;

    /**
     */
    private static final String[] COLUMN_NAMES = {
        "Name", "Supplier", "Amount",
    };

    /**
     */
    private final List<Expense> expenses;

    /**
     * @param expenses
     */
    public ExpensesPopupExpenseTM(final List<Expense> expenses) {
        this.expenses = expenses;
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return this.expenses.size();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    /**
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(final int column) {
        return COLUMN_NAMES[column];
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final Object object;

        final Expense expense = this.expenses.get(rowIndex);
        if (columnIndex == 0) {
            object = " " + expense.getName();
        } else if (columnIndex == 1) {
            object = " " + expense.getSupplier();
        } else if (columnIndex == 2) {
            object = " " + expense.getAmount();
        } else {
            throw new IllegalArgumentException("columnIndex: " + columnIndex);
        }

        return object;
    }
}
