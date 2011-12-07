package com.openbravo.pos.sales.cinema;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.sales.cinema.model.Booking;
import com.openbravo.pos.sales.cinema.model.Customer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

/**
 */
public class MemberPopup extends JDialog {

    /**
	 */
    private static final long serialVersionUID = -8632989194945075029L;

    /**
     */
    private static final Logger LOGGER = Logger.getLogger(MemberPopup.class
        .getName());

    /**
     */
    private static final DateFormat DATE_FORMAT_MS = new SimpleDateFormat(
        "dd-MM-yyyy");

    /**
     * @param dao
     * @param parent
     * @return a new MemberPopup
     */
    public static MemberPopup getMemberPopup(final CinemaDaoImpl dao,
    final Component parent) {
        final Window window = CinemaReservationMap.getWindow(parent);

        MemberPopup myMsg;
        if (window instanceof Frame) {
            myMsg = new MemberPopup(dao, (Frame) window, true);
        } else {
            myMsg = new MemberPopup(dao, (Dialog) window, true);
        }
        myMsg.init();
        myMsg.applyComponentOrientation(parent.getComponentOrientation());

        return myMsg;
    }

    /**
     * @param app
     * @param parent
     * @return a new MemberPopup
     */
    public static MemberPopup getMemberPopup(final AppView app,
    final Component parent) {
        final CinemaDaoImpl dao =
            (CinemaDaoImpl) app
                .getBean("com.openbravo.pos.sales.cinema.CinemaDaoImpl");

        return getMemberPopup(dao, parent);
    }

    /**
     */
    private static class MyListData extends AbstractListModel {

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

    /**
     */
    private final CinemaDaoImpl dao;

    /**
     */
    private Customer selectedCustomer;

    /**
     * @param dao
     * @param parent
     * @param modal
     */
    private MemberPopup(final CinemaDaoImpl dao, final Frame parent,
    final boolean modal) {
        super(parent, modal);
        this.dao = dao;
    }

    /**
     * @param dao
     * @param parent
     * @param modal
     */
    private MemberPopup(final CinemaDaoImpl dao, final Dialog parent,
    final boolean modal) {
        super(parent, modal);
        this.dao = dao;
    }

    /**
     * @return the selected customer
     */
    public Customer getSelectedCustomer() {
        return this.selectedCustomer;
    }

    /**
     */
    private void init() {
        this.initComponents();

        this.jScrollPane1.getVerticalScrollBar().setPreferredSize(
            new Dimension(35, 35));

        this.pinTF.addEditorKeys(this.editorKeys);
        this.nameTF.addEditorKeys(this.editorKeys);

        this.pinTF.reset();
        this.nameTF.reset();

        this.pinTF.activate();

        this.customersList.setCellRenderer(new CustomerRenderer());

        this.getRootPane().setDefaultButton(this.okButton);

        this.selectedCustomer = null;
    }

    /**
     */
    private void cleanSearch() {
        this.customersList.setModel(new MyListData(new ArrayList<Object>()));
        this.selectedCustomer = null;

        this.customersList.setVisible(true);
        this.jScrollPane1.setViewportView(this.customersList);
    }

    /**
     */
    private void executeSearch() {
        final String name = this.nameTF.getText();
        final String pin = this.pinTF.getText();

        final List<Customer> customers;
        try {
            if (StringUtils.isNotEmpty(name)) {
                customers = this.dao.searchCustomer(name);
            } else if (StringUtils.isNotEmpty(pin)) {
                customers = Arrays.asList(this.dao.getCustomerByPin(pin));
            } else {
                customers = Collections.emptyList();
            }
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
            return;
        }

        this.customersList.setVisible(customers.size() != 1);
        this.selectedCustomer = null;

        if (customers.size() == 1) {
            final JPanel customerPanel = this.toPanel(customers.get(0));

            this.okButton.setEnabled(true);

            this.jScrollPane1.setViewportView(customerPanel);
        } else {
            this.customersList.setModel(new MyListData(customers));
            if (this.customersList.getModel().getSize() > 0) {
                this.customersList.setSelectedIndex(0);
            }

            this.jScrollPane1.setViewportView(this.customersList);
        }
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
        this.nameLabel = new javax.swing.JLabel();
        this.nameTF = new com.openbravo.editor.JEditorString();
        this.pinLabel = new javax.swing.JLabel();
        this.pinTF = new com.openbravo.editor.JEditorString();
        this.jPanel6 = new javax.swing.JPanel();
        this.cleanButton = new javax.swing.JButton();
        this.searchButton = new javax.swing.JButton();
        this.jPanel4 = new javax.swing.JPanel();
        this.jScrollPane1 = new javax.swing.JScrollPane();
        this.customersList = new javax.swing.JList();
        this.jPanel8 = new javax.swing.JPanel();
        this.jPanel1 = new javax.swing.JPanel();
        this.okButton = new javax.swing.JButton();
        this.cancelButton = new javax.swing.JButton();

        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Create Memberships"); // NOI18N

        this.jPanel2.setLayout(new java.awt.BorderLayout());
        this.jPanel2.add(this.editorKeys, java.awt.BorderLayout.NORTH);

        this.getContentPane().add(this.jPanel2, java.awt.BorderLayout.LINE_END);

        this.jPanel3.setLayout(new java.awt.BorderLayout());

        this.jPanel5.setLayout(new java.awt.BorderLayout());

        this.nameLabel.setText("Name");

        this.pinLabel.setText("PIN");

        final javax.swing.GroupLayout jPanel7Layout =
            new javax.swing.GroupLayout(this.jPanel7);
        this.jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(
            javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                    jPanel7Layout.createSequentialGroup().addComponent(
                        this.nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                        140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.nameTF,
                            javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                            javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                    jPanel7Layout.createSequentialGroup().addComponent(
                        this.pinLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                        140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.pinTF,
                            javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                            javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(
            javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.pinLabel).addComponent(this.pinTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.nameLabel).addComponent(this.nameTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(
                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        this.jPanel5.add(this.jPanel7, java.awt.BorderLayout.CENTER);

        this.cleanButton.setText(AppLocal.getIntString("button.clean")); // NOI18N
        this.cleanButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
            	MemberPopup.this.clean();
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
                	MemberPopup.this.search();
                }
            });
        this.jPanel6.add(this.searchButton);

        this.jPanel5.add(this.jPanel6, java.awt.BorderLayout.SOUTH);

        this.jPanel3.add(this.jPanel5, java.awt.BorderLayout.PAGE_START);

        this.jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,
            5, 5, 5));
        this.jPanel4.setLayout(new java.awt.BorderLayout());

        this.customersList.setFocusable(false);
        this.customersList.setRequestFocusEnabled(false);
        this.customersList.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
            	MemberPopup.this.jListCustomersMouseClicked(evt);
            }
        });
        this.customersList
            .addListSelectionListener(new javax.swing.event.ListSelectionListener() {

                @Override
                public void valueChanged(
                final javax.swing.event.ListSelectionEvent evt) {
                	MemberPopup.this.jListCustomersValueChanged(evt);
                }
            });
        this.jScrollPane1.setViewportView(this.customersList);

        this.jPanel4.add(this.jScrollPane1, java.awt.BorderLayout.CENTER);

        this.jPanel3.add(this.jPanel4, java.awt.BorderLayout.CENTER);

        this.jPanel8.setLayout(new java.awt.BorderLayout());

        this.okButton.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        this.okButton.setText(AppLocal.getIntString("Button.OK")); // NOI18N
        this.okButton.setEnabled(false);
        this.okButton.setFocusPainted(false);
        this.okButton.setFocusable(false);
        this.okButton.setMargin(new java.awt.Insets(8, 16, 8, 16));
        this.okButton.setRequestFocusEnabled(false);
        this.okButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent event) {
            	MemberPopup.this.ok();
            }
        });
        this.jPanel1.add(this.okButton);

        this.cancelButton.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        this.cancelButton.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
        this.cancelButton.setFocusPainted(false);
        this.cancelButton.setFocusable(false);
        this.cancelButton.setMargin(new java.awt.Insets(8, 16, 8, 16));
        this.cancelButton.setRequestFocusEnabled(false);
        this.cancelButton
            .addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent event) {
                	MemberPopup.this.cancel();
                }
            });
        this.jPanel1.add(this.cancelButton);

        this.jPanel8.add(this.jPanel1, java.awt.BorderLayout.LINE_END);

        this.jPanel3.add(this.jPanel8, java.awt.BorderLayout.SOUTH);

        this.getContentPane().add(this.jPanel3, java.awt.BorderLayout.CENTER);

        final java.awt.Dimension screenSize =
            java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((screenSize.width - 613) / 2,
            (screenSize.height - 610) / 2, 613, 610);
    }// </editor-fold>//GEN-END:initComponents

    /**
     */
    private void cancel() {
        this.selectedCustomer = null;

        this.dispose();
    }

    /**
     */
    private void ok() {
        if (this.selectedCustomer == null) {
            this.selectedCustomer =
                (Customer) this.customersList.getSelectedValue();
        }

        this.dispose();
    }

    /**
     */
    private void search() {
        this.executeSearch();
    }

    private void jListCustomersValueChanged(
    final javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListCustomersValueChanged

        this.okButton.setEnabled(this.customersList.getSelectedValue() != null);

    }// GEN-LAST:event_jListCustomersValueChanged

    private void
    jListCustomersMouseClicked(final java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jListCustomersMouseClicked

        if (evt.getClickCount() == 2) {
            this.selectedCustomer =
                (Customer) this.customersList.getSelectedValue();
            this.dispose();
        }

    }// GEN-LAST:event_jListCustomersMouseClicked

    private void clean() {
        this.pinTF.reset();
        this.nameTF.reset();

        this.cleanSearch();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cleanButton;

    private javax.swing.JButton searchButton;

    private javax.swing.JLabel nameLabel;

    private javax.swing.JLabel pinLabel;

    private javax.swing.JList customersList;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JButton cancelButton;

    private javax.swing.JButton okButton;

    private com.openbravo.editor.JEditorKeys editorKeys;

    private com.openbravo.editor.JEditorString nameTF;

    private com.openbravo.editor.JEditorString pinTF;

    // End of variables declaration//GEN-END:variables

    /**
     * @param customer
     * @return
     */
    private JPanel toPanel(final Customer customer) {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("customer: " + customer);
        }

        final List<Booking> bookings;
        try {
            bookings = this.dao.listBookingByCustomer(customer);
        } catch (final BasicException ex) {
            new MessageInf(ex).show(this);
            return new JPanel();
        }

        final JPanel customerPanel = new JPanel();
        customerPanel.setBackground(Color.WHITE);
        customerPanel.setLayout(new GridLayout2(7, 2));

        final JLabel idLabel = new JLabel("ID");
        idLabel.setSize(80, 20);

        customerPanel.add(idLabel);
        customerPanel.add(new JLabel(customer.getPin()));

        customerPanel.add(new JLabel("Name"));
        customerPanel.add(new JLabel(customer.getFirstName() + " "
            + customer.getLastName()));

        customerPanel.add(new JLabel("D.O.B."));
        if (customer.getBirthdate() == null) {
            customerPanel.add(new JLabel());
        } else {
            customerPanel.add(new JLabel(DATE_FORMAT_MS.format(customer
                .getBirthdate())));
        }

        customerPanel.add(new JLabel("Email"));
        customerPanel.add(new JLabel(customer.getEmail()));

        customerPanel.add(new JLabel("Membership"));
        if (customer.getMsType() == null) {
            customerPanel.add(new JLabel());
        } else {
            customerPanel.add(new JLabel(customer.getMsType().name()));
        }

        customerPanel.add(new JLabel("Valid from"));
        if (customer.getMsBegin() == null) {
            customerPanel.add(new JLabel());
        } else {
            customerPanel.add(new JLabel(DATE_FORMAT_MS.format(customer
                .getMsBegin())));
        }

        customerPanel.add(new JLabel("Valid to"));
        if (customer.getMsEnd() == null) {
            customerPanel.add(new JLabel());
        } else {
            customerPanel.add(new JLabel(DATE_FORMAT_MS.format(customer
                .getMsEnd())));
        }

        final JPanel bookingPanel = new JPanel();
        bookingPanel.setBackground(Color.WHITE);
        bookingPanel.setLayout(new GridLayout(bookings.size() + 1, 4));

        if (!bookings.isEmpty()) {
            bookingPanel.add(new JLabel("Film"));
            bookingPanel.add(new JLabel("Date"));
            bookingPanel.add(new JLabel("Seat"));
            bookingPanel.add(new JLabel("State"));

            for (final Booking booking : bookings) {
                bookingPanel.add(new JLabel(booking.getEvent().getName()));
                bookingPanel.add(new JLabel(DATE_FORMAT_MS.format(booking
                    .getEvent().getDateBegin())));
                bookingPanel.add(new JLabel(booking.getSeatCoordinates()));
                bookingPanel.add(new JLabel(booking.getState().name()));
            }
        }

        this.selectedCustomer = customer;

        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(customerPanel);
        panel.add(bookingPanel);

        return panel;
    }
}
