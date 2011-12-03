package com.openbravo.pos.sales.cinema;

import com.openbravo.editor.JEditorKeys;
import com.openbravo.editor.JEditorString;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.sales.cinema.listener.CustomerNameCancelAl;
import com.openbravo.pos.sales.cinema.listener.CustomerNameOkAl;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

/**
 */
public class CustomerNamePopup extends JDialog {

    /**
     */
    private static final long serialVersionUID = -7342687898865209014L;

    /**
     */
    private JEditorString customerNameTF;

    /**
     */
    private JEditorString customerPhoneTF;

    /**
     */
    private JEditorKeys editorKeys;

    /**
     */
    private String customerName;

    /**
     */
    private String customerPhone;

    /**
     * @param owner
     */
    public CustomerNamePopup(final Dialog owner) {
        super(owner, true);
        this.initComponents();
    }

    /**
     * @param owner
     */
    public CustomerNamePopup(final Frame owner) {
        super(owner, true);
        this.initComponents();
    }

    /**
     */
    public void cancel() {
        this.customerName = null;
        this.customerPhone = null;

        this.dispose();
    }

    /**
     */
    public void ok() {
        this.customerName = this.customerNameTF.getText();
        this.customerPhone = this.customerPhoneTF.getText();

        this.dispose();
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return this.customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(final String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the customerPhone
     */
    public String getCustomerPhone() {
        return this.customerPhone;
    }

    /**
     * @param customerPhone the customerPhone to set
     */
    public void setCustomerPhone(final String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     */
    private void initComponents() {
        this.editorKeys = new JEditorKeys();

        this.customerNameTF = new JEditorString();
        this.customerNameTF.addEditorKeys(this.editorKeys);
        this.customerNameTF.reset();

        this.customerPhoneTF = new JEditorString();
        this.customerPhoneTF.addEditorKeys(this.editorKeys);
        this.customerPhoneTF.reset();

        final JButton cancel = new JButton();
        cancel.addActionListener(new CustomerNameCancelAl(this));
        cancel.setFocusPainted(false);
        cancel.setFocusable(false);
        cancel.setIcon(new ImageIcon(this.getClass().getResource(
            "/com/openbravo/images/button_cancel.png")));
        cancel.setMargin(new Insets(8, 16, 8, 16));
        cancel.setRequestFocusEnabled(false);
        cancel.setText(AppLocal.getIntString("Button.Cancel"));

        final JButton ok = new JButton();
        ok.addActionListener(new CustomerNameOkAl(this));
        ok.setFocusPainted(false);
        ok.setFocusable(false);
        ok.setIcon(new ImageIcon(this.getClass().getResource(
            "/com/openbravo/images/button_ok.png")));
        ok.setMargin(new Insets(8, 16, 8, 16));
        ok.setRequestFocusEnabled(false);
        ok.setText(AppLocal.getIntString("Button.OK"));

        final JPanel keysPanel = new JPanel();
        final JPanel jPanel3 = new JPanel();
        final JPanel jPanel5 = new JPanel();
        final JPanel jPanel7 = new JPanel();
        final JLabel nameLabel = new JLabel("Name");
        final JLabel phoneLabel = new JLabel("Phone");
        final JPanel jPanel6 = new JPanel();
        final JPanel jPanel4 = new JPanel();
        final JPanel jPanel8 = new JPanel();
        final JPanel buttonPanel = new JPanel();

        final GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(
            GroupLayout.Alignment.LEADING).addGroup(
            jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(
                jPanel7Layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(
                        jPanel7Layout.createSequentialGroup().addComponent(
                            nameLabel, GroupLayout.PREFERRED_SIZE, 100,
                            GroupLayout.PREFERRED_SIZE).addPreferredGap(
                            LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(this.customerNameTF,
                                GroupLayout.PREFERRED_SIZE, 165,
                                GroupLayout.PREFERRED_SIZE)).addGroup(
                        jPanel7Layout.createSequentialGroup().addComponent(
                            phoneLabel, GroupLayout.PREFERRED_SIZE, 100,
                            GroupLayout.PREFERRED_SIZE).addPreferredGap(
                            LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(this.customerPhoneTF,
                                GroupLayout.PREFERRED_SIZE, 165,
                                GroupLayout.PREFERRED_SIZE)))));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(
            GroupLayout.Alignment.LEADING).addGroup(
            jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(
                jPanel7Layout.createParallelGroup(
                    GroupLayout.Alignment.BASELINE).addComponent(nameLabel)
                    .addComponent(this.customerNameTF,
                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)).addGap(6, 6, 6).addGroup(
                jPanel7Layout.createParallelGroup(
                    GroupLayout.Alignment.BASELINE).addComponent(phoneLabel)
                    .addComponent(this.customerPhoneTF,
                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE))));

        keysPanel.setLayout(new BorderLayout());

        keysPanel.add(this.editorKeys, BorderLayout.NORTH);

        jPanel5.setLayout(new BorderLayout());

        jPanel5.add(jPanel7, BorderLayout.CENTER);
        jPanel5.add(jPanel6, BorderLayout.SOUTH);

        jPanel4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel4.setLayout(new BorderLayout());

        buttonPanel.add(ok);
        buttonPanel.add(cancel);

        jPanel8.setLayout(new BorderLayout());

        jPanel8.add(buttonPanel, BorderLayout.LINE_END);

        jPanel3.setLayout(new BorderLayout());

        jPanel3.add(jPanel5, BorderLayout.PAGE_START);
        jPanel3.add(jPanel4, BorderLayout.CENTER);
        jPanel3.add(jPanel8, BorderLayout.SOUTH);

        final Dimension screenSize =
            Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((screenSize.width - 560) / 2,
            (screenSize.height - 400) / 2, 560, 400);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Customer informations");

        this.getContentPane().add(keysPanel, BorderLayout.LINE_END);
        this.getContentPane().add(jPanel3, BorderLayout.CENTER);
        this.getRootPane().setDefaultButton(ok);
    }
}
