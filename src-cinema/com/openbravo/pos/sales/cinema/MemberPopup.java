package com.openbravo.pos.sales.cinema;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.sales.cinema.model.Member;

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
public class MemberPopup extends JDialog {

    /**
	 */
    private static final long serialVersionUID = -8632989194945075029L;

    /**
     */
    private static final Logger LOGGER = Logger.getLogger(MemberPopup.class
        .getName());

    

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


    /**
     */
    private final CinemaDaoImpl dao;


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
     */
    @SuppressWarnings("unchecked")
	private void init() {
        this.initComponents();

        this.jScrollPane1.getVerticalScrollBar().setPreferredSize(
            new Dimension(35, 35));

        this.pinTF.addEditorKeys(this.editorKeys);
        this.firstNameTF.addEditorKeys(this.editorKeys);
        this.lastNameTF.addEditorKeys(this.editorKeys);
        this.address1TF.addEditorKeys(this.editorKeys);
        this.address2TF.addEditorKeys(this.editorKeys);
        this.cityTF.addEditorKeys(this.editorKeys);
        this.postcodeTF.addEditorKeys(this.editorKeys);
        this.telephoneTF.addEditorKeys(this.editorKeys);
        this.mobileTF.addEditorKeys(this.editorKeys);
        this.dobTF.addEditorKeys(this.editorKeys);
        this.membershipTF.addItem("gold membership");
        this.membershipTF.addItem("silver membership");

        this.pinTF.reset();
        this.firstNameTF.reset();
        this.lastNameTF.reset();
        this.address1TF.reset();
        this.address2TF.reset();
        this.cityTF.reset();
        this.postcodeTF.reset();
        this.telephoneTF.reset();
        this.mobileTF.reset();
        this.dobTF.reset();
       // this.membershipTF.reset();

        this.firstNameTF.activate();


        this.getRootPane().setDefaultButton(this.okButton);

        this.dao.setMember(null);
    }

  

    /**
     */
    private void executeSearch() {
    	final Member newMember = new Member();
        newMember.setFirstName(this.firstNameTF.getText());
        newMember.setLastName(this.lastNameTF.getText());
        newMember.setAddress1(this.address1TF.getText());
        newMember.setAddress2(this.address2TF.getText());
        newMember.setCity(this.cityTF.getText());
        newMember.setMemberShipType((String) this.membershipTF.getSelectedItem());
        newMember.setPostcode(this.postcodeTF.getText());
        newMember.setTelephone(this.telephoneTF.getText());
        newMember.setMobile(this.mobileTF.getText());
        newMember.setDob(this.dobTF.getText());

        LOGGER.info("New member: " + newMember.getFirstName() + newMember.getLastName());
        if(!newMember.requiredFields())
        {
        	JOptionPane.showMessageDialog(null, "Please fill in all required fields marked by '*'");
        }else{
        	try {
			this.dao.createWpUser(newMember);
        	this.okButton.setEnabled(true);
				
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
        this.firstNameTF = new com.openbravo.editor.JEditorString();
        this.lastNameLabel = new javax.swing.JLabel();
        this.lastNameTF = new com.openbravo.editor.JEditorString();
        this.address1Label = new javax.swing.JLabel();
        this.address1TF = new com.openbravo.editor.JEditorString();
        this.address2Label = new javax.swing.JLabel();
        this.address2TF = new com.openbravo.editor.JEditorString();
        this.cityLabel = new javax.swing.JLabel();
        this.cityTF = new com.openbravo.editor.JEditorString();
        this.postcodeLabel = new javax.swing.JLabel();
        this.postcodeTF = new com.openbravo.editor.JEditorString();
        this.telephoneLabel = new javax.swing.JLabel();
        this.telephoneTF = new com.openbravo.editor.JEditorString();
        this.mobileLabel = new javax.swing.JLabel();
        this.mobileTF = new com.openbravo.editor.JEditorString();
        this.dobLabel = new javax.swing.JLabel();
        this.dobTF = new com.openbravo.editor.JEditorString();
        this.membershipLabel = new javax.swing.JLabel();
        this.membershipTF = new javax.swing.JComboBox();
        this.pinLabel = new javax.swing.JLabel();
        this.pinTF = new com.openbravo.editor.JEditorString();
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
        this.lastNameLabel.setText("Last Name *");
        this.address1Label.setText("Address Line 1 *");
        this.address2Label.setText("Address Line 2");
        this.cityLabel.setText("Town/City *");
        this.postcodeLabel.setText("Postcode *");
        this.membershipLabel.setText("Membership Type");
        this.telephoneLabel.setText("Main Telephone *");
        this.mobileLabel.setText("Mobile No.");
        this.dobLabel.setText("DOB (yyyy/mm/dd) *");

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
                        this.firstNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                        140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.firstNameTF,
                            javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                            javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                    jPanel7Layout.createSequentialGroup().addComponent(
                        this.membershipLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                        140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.membershipTF,
                            javax.swing.GroupLayout.PREFERRED_SIZE, 190,
                            javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                     jPanel7Layout.createSequentialGroup().addComponent(
                         this.lastNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                         140, javax.swing.GroupLayout.PREFERRED_SIZE)
                         .addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                         .addComponent(this.lastNameTF,
                            javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                            javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                     jPanel7Layout.createSequentialGroup().addComponent(
                         this.address1Label, javax.swing.GroupLayout.PREFERRED_SIZE,
                         140, javax.swing.GroupLayout.PREFERRED_SIZE)
                         .addPreferredGap(
                             javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                             .addComponent(this.address1TF,
                             javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                             javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                      jPanel7Layout.createSequentialGroup().addComponent(
                         this.address2Label, javax.swing.GroupLayout.PREFERRED_SIZE,
                         140, javax.swing.GroupLayout.PREFERRED_SIZE)
                         .addPreferredGap(
                             javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                             .addComponent(this.address2TF,
                             javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                             javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                      jPanel7Layout.createSequentialGroup().addComponent(
                             this.cityLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                             140, javax.swing.GroupLayout.PREFERRED_SIZE)
                             .addPreferredGap(
                             javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                             .addComponent(this.cityTF,
                             javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                             javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                      jPanel7Layout.createSequentialGroup().addComponent(
                             this.postcodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                             140, javax.swing.GroupLayout.PREFERRED_SIZE)
                             .addPreferredGap(
                             javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                             .addComponent(this.postcodeTF,
                             javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                             javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                      jPanel7Layout.createSequentialGroup().addComponent(
                             this.pinLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                             140, javax.swing.GroupLayout.PREFERRED_SIZE)
                             .addPreferredGap(
                             javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                             .addComponent(this.pinTF,
                             javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                             javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                     jPanel7Layout.createSequentialGroup().addComponent(
                             this.telephoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                             140, javax.swing.GroupLayout.PREFERRED_SIZE)
                             .addPreferredGap(
                              javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                              .addComponent(this.telephoneTF,
                              javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                              javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                   jPanel7Layout.createSequentialGroup().addComponent(
                              this.mobileLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                              140, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addPreferredGap(
                              javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                              .addComponent(this.mobileTF,
                              javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                              javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                   jPanel7Layout.createSequentialGroup().addComponent(
                              this.dobLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                              140, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addPreferredGap(
                              javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                              .addComponent(this.dobTF,
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
                    this.address1Label).addComponent(this.address1TF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.address2Label).addComponent(this.address2TF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
               javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
               jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.cityLabel).addComponent(this.cityTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
               javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
               jPanel7Layout.createParallelGroup(
            		javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.postcodeLabel).addComponent(this.postcodeTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
              javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
              jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.pinLabel).addComponent(this.pinTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
              javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                    jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.telephoneLabel).addComponent(this.telephoneTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
             javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                    jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.mobileLabel).addComponent(this.mobileTF,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
             javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                    jPanel7Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                    this.dobLabel).addComponent(this.dobTF,
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

      //  this.searchButton.setIcon(new javax.swing.ImageIcon(this.getClass()
           // .getResource("/com/openbravo/images/launch.png"))); // NOI18N
        this.searchButton
            .setText("Create Membership"); // NOI18N
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
                	MemberPopup.this.cancel();
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
                	MemberPopup.this.print();
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


    private void clean() {
        this.pinTF.reset();
        this.firstNameTF.reset();
        this.lastNameTF.reset();
        this.address1TF.reset();
        this.address2TF.reset();
        this.cityTF.reset();
        this.postcodeTF.reset();
        this.telephoneTF.reset();
        this.mobileTF.reset();
        this.dobTF.reset();
       // this.membershipTF.reset();

        this.dao.setMember(null);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cleanButton;

    private javax.swing.JButton searchButton;

    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JLabel address1Label;
    private javax.swing.JLabel address2Label;
    private javax.swing.JLabel cityLabel;
    private javax.swing.JLabel postcodeLabel;
    private javax.swing.JLabel membershipLabel;
    private javax.swing.JLabel telephoneLabel;
    private javax.swing.JLabel mobileLabel;
    private javax.swing.JLabel dobLabel;

    private javax.swing.JLabel pinLabel;

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
    private com.openbravo.editor.JEditorString lastNameTF;
    private com.openbravo.editor.JEditorString address1TF;
    private com.openbravo.editor.JEditorString address2TF;
    private com.openbravo.editor.JEditorString cityTF;
    private com.openbravo.editor.JEditorString postcodeTF;
    private com.openbravo.editor.JEditorString telephoneTF;
    private com.openbravo.editor.JEditorString mobileTF;
    private com.openbravo.editor.JEditorString dobTF;
    private javax.swing.JComboBox membershipTF;

    private com.openbravo.editor.JEditorString pinTF;

    // End of variables declaration//GEN-END:variables

 
}
