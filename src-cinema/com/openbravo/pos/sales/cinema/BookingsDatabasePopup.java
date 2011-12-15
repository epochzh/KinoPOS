package com.openbravo.pos.sales.cinema;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.sales.cinema.model.Booking;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;

/**
 */
public class BookingsDatabasePopup extends JDialog {

    /**
     */
    private static final long serialVersionUID = -70158301859580949L;

    /**
     */
    private static final Logger LOGGER = Logger
        .getLogger(BookingsDatabasePopup.class.getName());

    /**
     * @param panel
     * @return a new BookingsDatabasePopup
     */
    public static BookingsDatabasePopup getPopup(
    final CinemaReservationMap panel) {
        final Window window = CinemaReservationMap.getWindow(panel);

        BookingsDatabasePopup myMsg;
        if (window instanceof Frame) {
            myMsg = new BookingsDatabasePopup(panel, (Frame) window, true);
        } else {
            myMsg = new BookingsDatabasePopup(panel, (Dialog) window, true);
        }
        myMsg.init();
        myMsg.applyComponentOrientation(panel.getComponentOrientation());

        return myMsg;
    }

    /**
     */
    private static class BookingsTableCellRenderer extends
    DefaultTableCellRenderer {

        /**
         */
        private static final Font FONT = new Font("Arial", Font.PLAIN, 18);

        /**
         */
        public BookingsTableCellRenderer() {
            super();
        }

        /**
         * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
         * java.lang.Object, boolean, boolean, int, int)
         */
        @Override
        public Component getTableCellRendererComponent(final JTable table,
        final Object value, final boolean isSelected, final boolean hasFocus,
        final int row, final int column) {
            final Component component =
                super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
            component.setFont(FONT);

            return component;
        }
    }

    /**
     */
    private static class BookingsTableModel extends AbstractTableModel {

        /**
         */
        private static final long serialVersionUID = -5452136159812312751L;

        /**
         */
        private static final String[] COLUMN_NAMES = {
            "Film", "Start date / time", "Barcode", "Customer", "State",
        };

        /**
         */
        private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
            "MMM");

        /**
         */
        private static final DateFormat TIME_FORMAT = new SimpleDateFormat(
            "HH:mm");

        /**
         */
        private final List<Booking> bookings;

        /**
         * @param bookings
         */
        public BookingsTableModel(final List<Booking> bookings) {
            this.bookings = bookings;
        }

        /**
         * @see javax.swing.table.TableModel#getRowCount()
         */
        @Override
        public int getRowCount() {
            return this.bookings.size();
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
        @SuppressWarnings("deprecation")
        public Object getValueAt(final int rowIndex, final int columnIndex) {
            final Object object;

            final Booking booking = this.bookings.get(rowIndex);
            if (columnIndex == 0) {
                object = booking.getEvent().getName();
            } else if (columnIndex == 1) {
                final Date dateBegin = booking.getEvent().getDateBegin();
                object =
                    CinemaReservationMap.getDayOfMonthSuffix(dateBegin
                        .getDate())
                        + " "
                        + DATE_FORMAT.format(dateBegin)
                        + " / "
                        + TIME_FORMAT.format(dateBegin);
            } else if (columnIndex == 2) {
                object = booking.getBarcode();
            } else if (columnIndex == 3) {
                object = booking.getCustomerName();
            } else if (columnIndex == 4) {
                object = booking.getState();
            } else {
                throw new IllegalArgumentException("columnIndex: "
                    + columnIndex);
            }

            return object;
        }

        /**
         * @return the bookings
         */
        public List<Booking> getBookings() {
            return this.bookings;
        }
    }

    /**
     */
    private final CinemaDaoImpl dao;

    /**
     */
    private final CinemaReservationMap panel;

    /**
     * @param panel
     * @param parent
     * @param modal
     */
    private BookingsDatabasePopup(final CinemaReservationMap panel,
    final Frame parent, final boolean modal) {
        super(parent, modal);
        this.dao = panel.getDao();
        this.panel = panel;
    }

    /**
     * @param panel
     * @param parent
     * @param modal
     */
    private BookingsDatabasePopup(final CinemaReservationMap panel,
    final Dialog parent, final boolean modal) {
        super(parent, modal);
        this.dao = panel.getDao();
        this.panel = panel;
    }

    /**
     */
    private void init() {
        this.initComponents();

        this.jScrollPane1.getVerticalScrollBar().setPreferredSize(
            new Dimension(35, 35));

        this.barcodeTF.addEditorKeys(this.editorKeys);
        this.customerNameTF.addEditorKeys(this.editorKeys);

        this.barcodeTF.reset();
        this.customerNameTF.reset();

        this.barcodeTF.activate();

        this.getRootPane().setDefaultButton(this.okButton);
    }

    /**
     */
    private void cleanSearch() {
        this.bookingsTable.setModel(new BookingsTableModel(
            new ArrayList<Booking>()));
    }

    /**
     */
    public void executeSearch() {
        String barcode = this.barcodeTF.getText();
        String customerName = this.customerNameTF.getText();

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("barcode: " + barcode);
            LOGGER.info("customerName: " + customerName);
        }

        final List<Booking> bookings;
        try {
            if (StringUtils.isNotEmpty(barcode)) {
                barcode = "%" + barcode + "%";
                bookings = this.dao.listBookingByBarcode(barcode);
            } else if (StringUtils.isNotEmpty(customerName)) {
                customerName = "%" + customerName + "%";
                bookings = this.dao.listBookingByCustomerName(customerName);
            } else {
                bookings = this.dao.listBookingByToday();
            }
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
            return;
        }

        this.bookingsTable.setModel(new BookingsTableModel(bookings));

        this.jScrollPane1.setViewportView(this.bookingsTable);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed"
    // desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        this.jPanel2 = new javax.swing.JPanel();
        this.editorKeys = new com.openbravo.editor.JEditorKeys();
        this.jPanel3 = new javax.swing.JPanel();
        this.jPanel5 = new javax.swing.JPanel();
        this.jPanel7 = new javax.swing.JPanel();
        this.customerNameLabel = new javax.swing.JLabel();
        this.customerNameTF = new com.openbravo.editor.JEditorString();
        this.barcodeLabel = new javax.swing.JLabel();
        this.barcodeTF = new com.openbravo.editor.JEditorString();
        this.jPanel6 = new javax.swing.JPanel();
        this.cleanButton = new javax.swing.JButton();
        this.searchButton = new javax.swing.JButton();
        this.jPanel4 = new javax.swing.JPanel();
        this.jScrollPane1 = new javax.swing.JScrollPane();
        this.bookingsTable = new javax.swing.JTable();
        this.jPanel8 = new javax.swing.JPanel();
        this.jPanel1 = new javax.swing.JPanel();
        this.okButton = new javax.swing.JButton();

        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Bookings list");

        this.jPanel2.setLayout(new java.awt.BorderLayout());
        this.jPanel2.add(this.editorKeys, java.awt.BorderLayout.NORTH);

        this.getContentPane().add(this.jPanel2, java.awt.BorderLayout.LINE_END);

        this.jPanel3.setLayout(new java.awt.BorderLayout());

        this.jPanel5.setLayout(new java.awt.BorderLayout());

        this.customerNameLabel.setText("Customer name");

        this.barcodeLabel.setText("Barcode");

        final javax.swing.GroupLayout jPanel7Layout =
            new javax.swing.GroupLayout(this.jPanel7);
        this.jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(
            javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                    jPanel7Layout.createSequentialGroup().addComponent(
                        this.customerNameLabel,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.customerNameTF,
                            javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                            javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                    jPanel7Layout.createSequentialGroup().addComponent(
                        this.barcodeLabel,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.barcodeTF,
                            javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                            javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(
            javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.barcodeLabel).addComponent(this.barcodeTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.customerNameLabel).addComponent(this.customerNameTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(
                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        this.jPanel5.add(this.jPanel7, java.awt.BorderLayout.CENTER);

        this.cleanButton.setText(AppLocal.getIntString("button.clean")); // NOI18N
        this.cleanButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                BookingsDatabasePopup.this.clean();
            }
        });
        this.jPanel6.add(this.cleanButton);

        this.searchButton.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/launch.png"))); // NOI18N
        this.searchButton
            .setText(AppLocal.getIntString("button.executefilter")); // NOI18N
        this.searchButton.setFocusPainted(false);
        this.searchButton.setFocusable(false);
        this.searchButton.setRequestFocusEnabled(false);
        this.searchButton
            .addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void
                actionPerformed(final java.awt.event.ActionEvent evt) {
                    BookingsDatabasePopup.this.search();
                }
            });
        this.jPanel6.add(this.searchButton);

        this.jPanel5.add(this.jPanel6, java.awt.BorderLayout.SOUTH);

        this.jPanel3.add(this.jPanel5, java.awt.BorderLayout.PAGE_START);

        this.jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,
            5, 5, 5));
        this.jPanel4.setLayout(new java.awt.BorderLayout());

        // final TableCellRenderer renderer = new BookingsTableCellRenderer();
        // this.bookingsTable.setDefaultRenderer(Object.class, renderer);
        this.bookingsTable.setFocusable(false);
        this.bookingsTable.setRequestFocusEnabled(false);
        this.bookingsTable.setRowHeight(32);
        this.bookingsTable.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
                BookingsDatabasePopup.this.bookingsTableMouseClicked();
            }
        });
        this.jScrollPane1.setViewportView(this.bookingsTable);

        this.jPanel4.add(this.jScrollPane1, java.awt.BorderLayout.CENTER);

        this.jPanel3.add(this.jPanel4, java.awt.BorderLayout.CENTER);

        this.jPanel8.setLayout(new java.awt.BorderLayout());

        this.okButton.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        this.okButton.setText(AppLocal.getIntString("Button.OK")); // NOI18N
        this.okButton.setFocusPainted(false);
        this.okButton.setFocusable(false);
        this.okButton.setMargin(new java.awt.Insets(8, 16, 8, 16));
        this.okButton.setRequestFocusEnabled(false);
        this.okButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent event) {
                BookingsDatabasePopup.this.ok();
            }
        });
        this.jPanel1.add(this.okButton);

        this.jPanel8.add(this.jPanel1, java.awt.BorderLayout.LINE_END);

        this.jPanel3.add(this.jPanel8, java.awt.BorderLayout.SOUTH);

        this.getContentPane().add(this.jPanel3, java.awt.BorderLayout.CENTER);

        final java.awt.Dimension screenSize =
            java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((screenSize.width - 900) / 2,
            (screenSize.height - 610) / 2, 900, 610);
    }// </editor-fold>//GEN-END:initComponents

    /**
     */
    private void ok() {
        this.dispose();
    }

    /**
     */
    private void search() {
        this.executeSearch();
    }

    /**
     */
    private void bookingsTableMouseClicked() {
        final int selectedRow = this.bookingsTable.getSelectedRow();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("selectedRow: " + selectedRow);
        }
        if (selectedRow == -1) {
            return;
        }
        final BookingsTableModel model =
            (BookingsTableModel) this.bookingsTable.getModel();
        final Booking booking = model.getBookings().get(selectedRow);
        this.panel.showBookingPopup(true, booking);
    }

    /**
     */
    private void clean() {
        this.barcodeTF.reset();
        this.customerNameTF.reset();

        this.cleanSearch();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cleanButton;

    private javax.swing.JButton searchButton;

    private javax.swing.JLabel customerNameLabel;

    private javax.swing.JLabel barcodeLabel;

    private javax.swing.JTable bookingsTable;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JButton okButton;

    private com.openbravo.editor.JEditorKeys editorKeys;

    private com.openbravo.editor.JEditorString customerNameTF;

    private com.openbravo.editor.JEditorString barcodeTF;

    // End of variables declaration//GEN-END:variables
}
