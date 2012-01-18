package com.openbravo.pos.sales.cinema;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.sales.cinema.model.OldMember;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 */
public class OldMemberPopup extends JDialog {

    /**
	 */
    private static final long serialVersionUID = -8632989194945075029L;

    /**
     */
    private static final Logger LOGGER = Logger.getLogger(OldMemberPopup.class
        .getName());

    /**
     * @param dao
     * @param parent
     * @return a new OldMemberPopup
     */
    public static OldMemberPopup getOldMemberPopup(final CinemaDaoImpl dao,
    final Component parent) {
        final Window window = CinemaReservationMap.getWindow(parent);

        OldMemberPopup myMsg;
        if (window instanceof Frame) {
            myMsg = new OldMemberPopup(dao, (Frame) window, true);
        } else {
            myMsg = new OldMemberPopup(dao, (Dialog) window, true);
        }
        myMsg.init();
        myMsg.applyComponentOrientation(parent.getComponentOrientation());

        return myMsg;
    }

    /**
     * @param app
     * @param parent
     * @return a new OldMemberPopup
     */
    public static OldMemberPopup getOldMemberPopup(final AppView app,
    final Component parent) {
        final CinemaDaoImpl dao =
            (CinemaDaoImpl) app
                .getBean("com.openbravo.pos.sales.cinema.CinemaDaoImpl");

        return getOldMemberPopup(dao, parent);
    }

    /**
     */
    private java.awt.event.ActionListener membershipAction = new java.awt.event.ActionListener() {

        @Override
        public void
        actionPerformed(final java.awt.event.ActionEvent evt) {
            OldMemberPopup.this.search();
        }
    };
    
    /**
     * 
     */
    private java.awt.event.ActionListener jointMembershipAction = new java.awt.event.ActionListener() {

        @Override
        public void
        actionPerformed(final java.awt.event.ActionEvent evt) {
            OldMemberPopup.this.jointMembership();
        };
    };

    /**
     */
    private final CinemaDaoImpl dao;

    /**
     * @param dao
     * @param parent
     * @param modal
     */
    private OldMemberPopup(final CinemaDaoImpl dao, final Frame parent,
    final boolean modal) {
        super(parent, modal);
        this.dao = dao;
    }

    /**
     * @param dao
     * @param parent
     * @param modal
     */
    private OldMemberPopup(final CinemaDaoImpl dao, final Dialog parent,
    final boolean modal) {
        super(parent, modal);
        this.dao = dao;
    }

    /**
     */
    @SuppressWarnings("unchecked")
    private void init() {
        this.initComponents();

        this.jScrollPane1.getVerticalScrollBar().setPreferredSize(
            new Dimension(35, 35));

        this.firstNameTF.addEditorKeys(this.editorKeys);
        this.firstName2TF.addEditorKeys(this.editorKeys);
        this.lastNameTF.addEditorKeys(this.editorKeys);
        this.lastName2TF.addEditorKeys(this.editorKeys);
        this.oldPinTF.addEditorKeys(this.editorKeys);
        this.oldPin2TF.addEditorKeys(this.editorKeys);
        this.expiryTF.addEditorKeys(this.editorKeys);
       // this.membershipTF.addItem("gold membership");
        this.membershipTF.addItem("silver membership");
        this.membershipTF.addItem("joint membership");

        this.firstNameTF.reset();
        this.firstName2TF.reset();
        this.lastNameTF.reset();
        this.lastName2TF.reset();
        this.oldPinTF.reset();
        this.oldPin2TF.reset();
        this.expiryTF.reset();
        // this.membershipTF.reset();

        this.firstNameTF.activate();

        this.getRootPane().setDefaultButton(this.okButton);

        this.dao.setMember(null);
    }

    /**
     */
    private void executeSearch() {
        final OldMember newMember = new OldMember();
        newMember.setFirstName(this.firstNameTF.getText());
        newMember.setLastName(this.lastNameTF.getText());
        newMember.setOldPin(this.oldPinTF.getText());
        newMember.setExpiry(this.expiryTF.getText());
        newMember.setMemberShipType((String) this.membershipTF
            .getSelectedItem());
     //   newMember.setExpiry(this.expiryTF.getText());

        LOGGER.info("New member: " + newMember.getFirstName()
            + newMember.getLastName());
        if (!newMember.requiredFields()) {
            // TODO data validation
            JOptionPane.showMessageDialog(null,
                "Please fill in all required fields marked by '*'");
        } else {
            // try {
            try {
			this.dao.createOldWpUser(newMember);
            this.okButton.setEnabled(true);
            this.firstNameTF.setEnabled(false);
            this.firstName2TF.setEnabled(false);
            this.lastNameTF.setEnabled(false);
            this.lastName2TF.setEnabled(false);
            this.oldPinTF.setEnabled(false);
            this.oldPin2TF.setEnabled(false);
            this.expiryTF.setEnabled(false);

             } catch (BasicException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
             }
        }
    }
    
    
    /**
     */
    private void executeJointMembership(){
    	final OldMember newMember = new OldMember();
        newMember.setFirstName(this.firstNameTF.getText());
        newMember.setFirstName2(this.firstName2TF.getText());
        newMember.setLastName(this.lastNameTF.getText());
        newMember.setLastName2(this.lastName2TF.getText());
        newMember.setOldPin(this.oldPinTF.getText());
        newMember.setOldPin2(this.oldPin2TF.getText());
       newMember.setExpiry(this.expiryTF.getText());

        LOGGER.info("New member: " + newMember.getFirstName()
            + newMember.getLastName());
        if (!newMember.requiredFields()) {
            // TODO data validation
            JOptionPane.showMessageDialog(null,
                "Please fill in all required fields marked by '*'");
        } else {
             try {
            this.dao.createWpJointUsers(newMember);
            this.okButton.setEnabled(true);
            this.firstNameTF.setEnabled(false);
            this.firstName2TF.setEnabled(false);
            this.lastNameTF.setEnabled(false);
            this.oldPinTF.setEnabled(false);
            this.expiryTF.setEnabled(false);

            } catch (BasicException e) {
            // TODO Auto-generated catch block
            	e.printStackTrace();
            }
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
        this.firstNameLabel = new javax.swing.JLabel();
        this.firstName2Label = new javax.swing.JLabel();
        this.firstNameTF = new com.openbravo.editor.JEditorString();
        this.firstName2TF = new com.openbravo.editor.JEditorString();
        this.lastNameLabel = new javax.swing.JLabel();
        this.lastNameTF = new com.openbravo.editor.JEditorString();
        this.lastName2Label = new javax.swing.JLabel();
        this.lastName2TF = new com.openbravo.editor.JEditorString();
        this.oldPinLabel = new javax.swing.JLabel();
        this.oldPin2Label = new javax.swing.JLabel();
        this.oldPinTF = new com.openbravo.editor.JEditorString();
        this.oldPin2TF = new com.openbravo.editor.JEditorString();
        this.expiryLabel = new javax.swing.JLabel();
        this.expiryTF = new com.openbravo.editor.JEditorString();
        this.membershipLabel = new javax.swing.JLabel();
        this.membershipTF = new javax.swing.JComboBox();
        this.jPanel6 = new javax.swing.JPanel();
        this.cleanButton = new javax.swing.JButton();
        this.searchButton = new javax.swing.JButton();
        this.jPanel4 = new javax.swing.JPanel();
        this.jScrollPane1 = new javax.swing.JScrollPane();
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

        this.firstNameLabel.setText("First Name *");
        this.firstName2Label.setText("2nd Person First Name *");
        this.lastNameLabel.setText("Last Name *");
        this.lastName2Label.setText("2nd Person Last Name 2 *");
        this.oldPinLabel.setText("Old Pin *");
        this.oldPin2Label.setText("2nd person Old Pin *");
        this.membershipLabel.setText("Membership Type");
        this.expiryLabel.setText("Expiry Date (mm-dd-yyyy) *");
        
        this.firstName2TF.setEnabled(false);
        this.lastName2TF.setEnabled(false);
        this.oldPin2TF.setEnabled(false);
        
        this.membershipTF.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent event) {
            	OldMemberPopup.this.addExtraFields();
            }

        });
        
       

        final javax.swing.GroupLayout jPanel7Layout =
            new javax.swing.GroupLayout(this.jPanel7);
        this.jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(
            javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            jPanel7Layout.createSequentialGroup().addComponent(
                    this.membershipLabel,
                    javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(
                    		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(this.membershipTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE, 190,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                    jPanel7Layout.createSequentialGroup().addComponent(
                        this.firstNameLabel,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.firstNameTF,
                            javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                            javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                    jPanel7Layout.createSequentialGroup().addComponent(
                        this.lastNameLabel,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.lastNameTF,
                            javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                            javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                     jPanel7Layout.createSequentialGroup().addComponent(
                         this.oldPinLabel,
                         javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                         javax.swing.GroupLayout.PREFERRED_SIZE)
                         .addPreferredGap(
                         javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                         .addComponent(this.oldPinTF,
                         javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                         javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                    jPanel7Layout.createSequentialGroup().addComponent(
                        this.firstName2Label,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.firstName2TF,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                        javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                     jPanel7Layout.createSequentialGroup().addComponent(
                        this.lastName2Label,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                        		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.lastName2TF,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                        javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                      jPanel7Layout.createSequentialGroup().addComponent(
                        this.oldPin2Label,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.oldPin2TF,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                        javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                    jPanel7Layout.createSequentialGroup().addComponent(
                        this.expiryLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                        140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.expiryTF,
                            javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                            javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(
            javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.membershipLabel).addComponent(this.membershipTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.firstNameLabel).addComponent(this.firstNameTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.lastNameLabel).addComponent(this.lastNameTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                 jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.oldPinLabel).addComponent(this.oldPinTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.firstName2Label).addComponent(this.firstName2TF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.lastName2Label).addComponent(this.lastName2TF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                 jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.oldPin2Label).addComponent(this.oldPin2TF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.expiryLabel).addComponent(this.expiryTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                javax.swing.LayoutStyle.ComponentPlacement.RELATED).addContainerGap(
                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        this.jPanel5.add(this.jPanel7, java.awt.BorderLayout.CENTER);

        this.cleanButton.setText(AppLocal.getIntString("button.clean")); // NOI18N
        this.cleanButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                OldMemberPopup.this.clean();
            }
        });
        this.jPanel6.add(this.cleanButton);

        // this.searchButton.setIcon(new javax.swing.ImageIcon(this.getClass()
        // .getResource("/com/openbravo/images/launch.png"))); // NOI18N
        this.searchButton.setText("Create Membership"); // NOI18N
        this.searchButton.setFocusPainted(false);
        this.searchButton.setFocusable(false);
        this.searchButton.setRequestFocusEnabled(false);
        this.searchButton
            .addActionListener(this.membershipAction);
        this.jPanel6.add(this.searchButton);

        this.jPanel5.add(this.jPanel6, java.awt.BorderLayout.SOUTH);

        this.jPanel3.add(this.jPanel5, java.awt.BorderLayout.PAGE_START);

        this.jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,
            5, 5, 5));
        this.jPanel4.setLayout(new java.awt.BorderLayout());

        this.jPanel3.add(this.jPanel4, java.awt.BorderLayout.CENTER);

        this.jPanel8.setLayout(new java.awt.BorderLayout());

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
                    OldMemberPopup.this.cancel();
                }
            });
        this.jPanel1.add(this.cancelButton);

        this.okButton.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        this.okButton.setText("print"); // NOI18N
        this.okButton.setEnabled(false);
        this.okButton.setFocusPainted(false);
        this.okButton.setFocusable(false);
        this.okButton.setMargin(new java.awt.Insets(8, 30, 8, 30));
        this.okButton.setRequestFocusEnabled(false);
        this.okButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent event) {
                OldMemberPopup.this.print();
            }
        });
        this.jPanel1.add(this.okButton);

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
        this.dao.setMember(null);

        this.dispose();
    }
    
    /**
     * if the drop down is equal to joint membership add the extra fields and change the function 
     * else revert back to normal
     */
    public void addExtraFields(){
    	if(this.membershipTF.getSelectedItem().equals("joint membership"))
    	{
    		this.firstName2TF.setEnabled(true);
    		this.lastName2TF.setEnabled(true);
    		this.oldPin2TF.setEnabled(true);
    		this.searchButton.removeActionListener(this.membershipAction);
    		this.searchButton.removeActionListener(this.jointMembershipAction);
    		this.searchButton.addActionListener(this.jointMembershipAction);
    	}else if(this.membershipTF.getSelectedItem().equals("silver membership")){
    		this.searchButton.removeActionListener(this.jointMembershipAction);
    		this.searchButton.removeActionListener(this.membershipAction);
    		this.searchButton.addActionListener(this.membershipAction);
    		this.firstName2TF.setEnabled(false);
    		this.lastName2TF.setEnabled(false);
    		this.oldPin2TF.setEnabled(false);
    	}
    }

    /**
     */
    private void print() {
        // TODO: print the membership ticket

        this.dispose();
    }

    /**
     */
    private void search() {
        this.executeSearch();
    }
    
    /**
     */
    private void jointMembership() {
        this.executeJointMembership();
    }

    private void clean() {
        this.firstNameTF.reset();
        this.firstName2TF.reset();
        this.lastNameTF.reset();
        this.lastName2TF.reset();
        this.oldPinTF.reset();
        this.oldPin2TF.reset();
        this.expiryTF.reset();
        // this.membershipTF.reset();
        this.okButton.setEnabled(false);
        this.dao.setMember(null);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cleanButton;

    private javax.swing.JButton searchButton;

    private javax.swing.JLabel firstNameLabel;
    
    private javax.swing.JLabel firstName2Label;

    private javax.swing.JLabel lastNameLabel;
    
    private javax.swing.JLabel lastName2Label;
    private javax.swing.JLabel oldPinLabel;
    private javax.swing.JLabel oldPin2Label;


    private javax.swing.JLabel membershipLabel;

    private javax.swing.JLabel expiryLabel;
    

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

    private com.openbravo.editor.JEditorString firstNameTF;
    
    private com.openbravo.editor.JEditorString firstName2TF;

    private com.openbravo.editor.JEditorString lastNameTF;
    
    private com.openbravo.editor.JEditorString lastName2TF;
    
    private com.openbravo.editor.JEditorString oldPinTF;
    private com.openbravo.editor.JEditorString oldPin2TF;

    private com.openbravo.editor.JEditorString expiryTF;
    
    private com.openbravo.editor.JEditorString expiry2TF;

    private javax.swing.JComboBox membershipTF;

    // End of variables declaration//GEN-END:variables

}
