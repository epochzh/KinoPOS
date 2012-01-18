//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.panels.JProductFinder;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.pos.payment.JPaymentSelectReceipt;
import com.openbravo.pos.payment.JPaymentSelectRefund;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.cinema.CinemaReservationMap;
import com.openbravo.pos.scale.ScaleException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.ReportUtils;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.PrintService;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * 
 * @author adrianromero
 */
public abstract class JPanelTicket extends JPanel implements JPanelView,
BeanFactoryApp, TicketsEditor {

    // Variable numerica
    private final static int NUMBERZERO = 0;

    private final static int NUMBERVALID = 1;

    private final static int NUMBER_INPUTZERO = 0;

    private final static int NUMBER_INPUTZERODEC = 1;

    private final static int NUMBER_INPUTINT = 2;

    private final static int NUMBER_INPUTDEC = 3;

    private final static int NUMBER_PORZERO = 4;

    private final static int NUMBER_PORZERODEC = 5;

    private final static int NUMBER_PORINT = 6;

    private final static int NUMBER_PORDEC = 7;

    /**
     */
    private static final Logger LOGGER = Logger.getLogger(JPanelTicket.class
        .getName());

    protected JTicketLines m_ticketlines;

    // private Template m_tempLine;
    private TicketParser m_TTP;

    protected TicketInfo m_oTicket;

    protected Object m_oTicketExt;

    // Estas tres variables forman el estado...
    private int m_iNumberStatus;

    private int m_iNumberStatusInput;

    private int m_iNumberStatusPor;

    private StringBuffer m_sBarcode;

    private JTicketsBag m_ticketsbag;

    private SentenceList senttax;

    private ListKeyed taxcollection;

    // private ComboBoxValModel m_TaxModel;

    private SentenceList senttaxcategories;

    private ListKeyed taxcategoriescollection;

    private ComboBoxValModel taxcategoriesmodel;

    private TaxesLogic taxeslogic;

    // private ScriptObject scriptobjinst;
    protected JPanelButtons m_jbtnconfig;

    protected AppView m_App;

    protected DataLogicSystem dlSystem;

    protected DataLogicSales dlSales;

    protected DataLogicCustomers dlCustomers;

    private JPaymentSelect paymentdialogreceipt;

    private JPaymentSelect paymentdialogrefund;

    /** Creates new form JTicketView */
    public JPanelTicket() {

        this.initComponents();
    }

    @Override
    public void init(final AppView app) throws BeanFactoryException {

        this.m_App = app;
        this.dlSystem =
            (DataLogicSystem) this.m_App
                .getBean("com.openbravo.pos.forms.DataLogicSystem");
        this.dlSales =
            (DataLogicSales) this.m_App
                .getBean("com.openbravo.pos.forms.DataLogicSales");
        this.dlCustomers =
            (DataLogicCustomers) this.m_App
                .getBean("com.openbravo.pos.customers.DataLogicCustomers");

        // borramos el boton de bascula si no hay bascula conectada
        if (!this.m_App.getDeviceScale().existsScale()) {
            this.m_jbtnScale.setVisible(false);
        }

        this.m_ticketsbag = this.getJTicketsBag();
        this.m_jPanelBag.add(this.m_ticketsbag.getBagComponent(),
            BorderLayout.LINE_START);
        this.add(this.m_ticketsbag.getNullComponent(), "null");

        this.m_ticketlines =
            new JTicketLines(this.dlSystem.getResourceAsXML("Ticket.Line"));
        this.m_jPanelCentral.add(this.m_ticketlines,
            java.awt.BorderLayout.CENTER);

        this.m_TTP =
            new TicketParser(this.m_App.getDeviceTicket(), this.dlSystem);

        // Los botones configurables...
        this.m_jbtnconfig = new JPanelButtons("Ticket.Buttons", this);
        this.m_jButtonsExt.add(this.m_jbtnconfig);

        // El panel de los productos o de las lineas...
        this.catcontainer.add(this.getSouthComponent(), BorderLayout.CENTER);

        // El modelo de impuestos
        this.senttax = this.dlSales.getTaxList();
        this.senttaxcategories = this.dlSales.getTaxCategoriesList();

        this.taxcategoriesmodel = new ComboBoxValModel();

        // ponemos a cero el estado
        this.stateToZero();

        // inicializamos
        this.m_oTicket = null;
        this.m_oTicketExt = null;
    }

    @Override
    public Object getBean() {
        return this;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void activate() throws BasicException {

        this.paymentdialogreceipt = JPaymentSelectReceipt.getDialog(this);
        this.paymentdialogreceipt.init(this.m_App);
        this.paymentdialogrefund = JPaymentSelectRefund.getDialog(this);
        this.paymentdialogrefund.init(this.m_App);

        // impuestos incluidos seleccionado ?
        this.m_jaddtax.setSelected("true".equals(this.m_jbtnconfig
            .getProperty("taxesincluded")));

        // Inicializamos el combo de los impuestos.
        final java.util.List<TaxInfo> taxlist = this.senttax.list();
        this.taxcollection = new ListKeyed<TaxInfo>(taxlist);
        final java.util.List<TaxCategoryInfo> taxcategorieslist =
            this.senttaxcategories.list();
        this.taxcategoriescollection =
            new ListKeyed<TaxCategoryInfo>(taxcategorieslist);

        this.taxcategoriesmodel = new ComboBoxValModel(taxcategorieslist);
        this.m_jTax.setModel(this.taxcategoriesmodel);

        final String taxesid = this.m_jbtnconfig.getProperty("taxcategoryid");
        if (taxesid == null) {
            if (this.m_jTax.getItemCount() > 0) {
                this.m_jTax.setSelectedIndex(0);
            }
        } else {
            this.taxcategoriesmodel.setSelectedKey(taxesid);
        }

        this.taxeslogic = new TaxesLogic(taxlist);

        // Show taxes options
        if (this.m_App.getAppUserView().getUser().hasPermission(
            "sales.ChangeTaxOptions")) {
            this.m_jTax.setVisible(true);
            this.m_jaddtax.setVisible(true);
        } else {
            this.m_jTax.setVisible(false);
            this.m_jaddtax.setVisible(false);
        }

        // Authorization for buttons
        this.btnSplit.setEnabled(this.m_App.getAppUserView().getUser()
            .hasPermission("sales.Total"));
        this.m_jDelete.setEnabled(this.m_App.getAppUserView().getUser()
            .hasPermission("sales.EditLines"));
        this.m_jNumberKeys.setMinusEnabled(this.m_App.getAppUserView()
            .getUser().hasPermission("sales.EditLines"));
        this.m_jNumberKeys.setEqualsEnabled(this.m_App.getAppUserView()
            .getUser().hasPermission("sales.Total"));
        this.m_jbtnconfig.setPermissions(this.m_App.getAppUserView().getUser());

        this.m_ticketsbag.activate();
    }

    @Override
    public boolean deactivate() {

        return this.m_ticketsbag.deactivate();
    }

    protected abstract JTicketsBag getJTicketsBag();

    protected abstract Component getSouthComponent();

    protected abstract void resetSouthComponent();

    @Override
    public void setActiveTicket(final TicketInfo oTicket,
    final Object oTicketExt) {

        this.m_oTicket = oTicket;
        this.m_oTicketExt = oTicketExt;

        if (this.m_oTicket != null) {
            // Asign preeliminary properties to the receipt
            this.m_oTicket.setUser(this.m_App.getAppUserView().getUser()
                .getUserInfo());
            this.m_oTicket.setActiveCash(this.m_App.getActiveCashIndex());
            this.m_oTicket.setDate(new Date()); // Set the edition date.
        }

        this.executeEvent(this.m_oTicket, this.m_oTicketExt, "ticket.show");

        this.refreshTicket();
    }

    @Override
    public TicketInfo getActiveTicket() {
        return this.m_oTicket;
    }

    private void refreshTicket() {

        final CardLayout cl = (CardLayout) (this.getLayout());

        if (this.m_oTicket == null) {
            this.m_jTicketId.setText(null);
            this.m_ticketlines.clearTicketLines();

            this.m_jSubtotalEuros.setText(null);
            this.m_jTaxesEuros.setText(null);
            this.m_jTotalEuros.setText(null);

            this.stateToZero();

            // Muestro el panel de nulos.
            cl.show(this, "null");
            this.resetSouthComponent();

        } else {
            if (this.m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND) {
                // Make disable Search and Edit Buttons
                this.m_jEditLine.setVisible(false);
                this.m_jList.setVisible(false);
            }

            // Refresh ticket taxes
            for (final TicketLineInfo line : this.m_oTicket.getLines()) {
                line.setTaxInfo(this.taxeslogic.getTaxInfo(line
                    .getProductTaxCategoryID(), this.m_oTicket.getDate(),
                    this.m_oTicket.getCustomer()));
            }

            // The ticket name
            this.m_jTicketId.setText(this.m_oTicket.getName(this.m_oTicketExt));

            // Limpiamos todas las filas y anadimos las del ticket actual
            this.m_ticketlines.clearTicketLines();

            for (int i = 0; i < this.m_oTicket.getLinesCount(); i++) {
                this.m_ticketlines.addTicketLine(this.m_oTicket.getLine(i));
            }
            this.printPartialTotals();
            this.stateToZero();

            // Muestro el panel de tickets.
            cl.show(this, "ticket");
            this.resetSouthComponent();

            // activo el tecleador...
            this.m_jKeyFactory.setText(null);
            java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    JPanelTicket.this.m_jKeyFactory.requestFocus();
                }
            });
        }
    }

    private void printPartialTotals() {

        if (this.m_oTicket.getLinesCount() == 0) {
            this.m_jSubtotalEuros.setText(null);
            this.m_jTaxesEuros.setText(null);
            this.m_jTotalEuros.setText(null);
        } else {
            this.m_jSubtotalEuros.setText(this.m_oTicket.printSubTotal());
            this.m_jTaxesEuros.setText(this.m_oTicket.printTax());
            this.m_jTotalEuros.setText(this.m_oTicket.printTotal());
        }
    }

    private void paintTicketLine(final int index, final TicketLineInfo oLine) {

        if (this.executeEventAndRefresh("ticket.setline", new ScriptArg(
            "index", index), new ScriptArg("line", oLine)) == null) {

            this.m_oTicket.setLine(index, oLine);
            this.m_ticketlines.setTicketLine(index, oLine);
            this.m_ticketlines.setSelectedIndex(index);

            this.visorTicketLine(oLine); // Y al visor tambien...
            this.printPartialTotals();
            this.stateToZero();

            // event receipt
            this.executeEventAndRefresh("ticket.change");
        }
    }

    private void addTicketLine(final ProductInfoExt oProduct,
    final double dMul, final double dPrice) {

        final TaxInfo tax =
            this.taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(),
                this.m_oTicket.getDate(), this.m_oTicket.getCustomer());

        this.addTicketLine(new TicketLineInfo(oProduct, dMul, dPrice, tax,
            (java.util.Properties) (oProduct.getProperties().clone())));
    }

    protected void addTicketLine(final TicketLineInfo oLine) {

        if (this.executeEventAndRefresh("ticket.addline", new ScriptArg("line",
            oLine)) == null) {

            if (oLine.isProductCom()) {
                // Comentario entonces donde se pueda
                int i = this.m_ticketlines.getSelectedIndex();

                // me salto el primer producto normal...
                if ((i >= 0) && !this.m_oTicket.getLine(i).isProductCom()) {
                    i++;
                }

                // me salto todos los productos auxiliares...
                while ((i >= 0) && (i < this.m_oTicket.getLinesCount())
                    && this.m_oTicket.getLine(i).isProductCom()) {
                    i++;
                }

                if (i >= 0) {
                    this.m_oTicket.insertLine(i, oLine);
                    this.m_ticketlines.insertTicketLine(i, oLine); // Pintamos
                                                                   // la linea
                                                                   // en la
                                                                   // vista...
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            } else {
                // Producto normal, entonces al finalnewline.getMultiply()
                this.m_oTicket.addLine(oLine);
                this.m_ticketlines.addTicketLine(oLine); // Pintamos la linea en
                                                         // la vista...
            }

            this.visorTicketLine(oLine);
            this.printPartialTotals();
            this.stateToZero();

            // event receipt
            this.executeEventAndRefresh("ticket.change");
        }
    }

    private void removeTicketLine(final int i) {

        if (this.executeEventAndRefresh("ticket.removeline", new ScriptArg(
            "index", i)) == null) {

            if (this.m_oTicket.getLine(i).isProductCom()) {
                // Es un producto auxiliar, lo borro y santas pascuas.
                this.m_oTicket.removeLine(i);
                this.m_ticketlines.removeTicketLine(i);
            } else {
                // Es un producto normal, lo borro.
                this.m_oTicket.removeLine(i);
                this.m_ticketlines.removeTicketLine(i);
                // Y todos lo auxiliaries que hubiera debajo.
                while ((i < this.m_oTicket.getLinesCount())
                    && this.m_oTicket.getLine(i).isProductCom()) {
                    this.m_oTicket.removeLine(i);
                    this.m_ticketlines.removeTicketLine(i);
                }
            }

            this.visorTicketLine(null); // borro el visor
            this.printPartialTotals(); // pinto los totales parciales...
            this.stateToZero(); // Pongo a cero

            // event receipt
            this.executeEventAndRefresh("ticket.change");
        }
    }

    private ProductInfoExt getInputProduct() {
        final ProductInfoExt oProduct = new ProductInfoExt(); // Es un ticket
        oProduct.setReference(null);
        oProduct.setCode(null);
        oProduct.setName("");
        oProduct.setTaxCategoryID(((TaxCategoryInfo) this.taxcategoriesmodel
            .getSelectedItem()).getID());

        oProduct.setPriceSell(this.includeTaxes(oProduct.getTaxCategoryID(),
            this.getInputValue()));

        return oProduct;
    }

    private double includeTaxes(final String tcid, final double dValue) {
        if (this.m_jaddtax.isSelected()) {
            final TaxInfo tax =
                this.taxeslogic.getTaxInfo(tcid, this.m_oTicket.getDate(),
                    this.m_oTicket.getCustomer());
            final double dTaxRate = tax == null ? 0.0 : tax.getRate();
            return dValue / (1.0 + dTaxRate);
        } else {
            return dValue;
        }
    }

    private double getInputValue() {
        try {
            return Double.parseDouble(this.m_jPrice.getText());
        } catch (final NumberFormatException e) {
            return 0.0;
        }
    }

    private double getPorValue() {
        try {
            return Double.parseDouble(this.m_jPor.getText().substring(1));
        } catch (final NumberFormatException e) {
            return 1.0;
        } catch (final StringIndexOutOfBoundsException e) {
            return 1.0;
        }
    }

    private void stateToZero() {
        this.m_jPor.setText("");
        this.m_jPrice.setText("");
        this.m_sBarcode = new StringBuffer();

        this.m_iNumberStatus = NUMBER_INPUTZERO;
        this.m_iNumberStatusInput = NUMBERZERO;
        this.m_iNumberStatusPor = NUMBERZERO;
    }

    private void incProductByCode(final String sCode) {
        // precondicion: sCode != null

        try {
            final ProductInfoExt oProduct =
                this.dlSales.getProductInfoByCode(sCode);
            if (oProduct == null) {
                Toolkit.getDefaultToolkit().beep();
                new MessageInf(MessageInf.SGN_WARNING, AppLocal
                    .getIntString("message.noproduct")).show(this);
                this.stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                this.incProduct(oProduct);
            }
        } catch (final BasicException eData) {
            this.stateToZero();
            new MessageInf(eData).show(this);
        }
    }

    private void incProductByCodePrice(final String sCode,
    final double dPriceSell) {
        // precondicion: sCode != null

        try {
            final ProductInfoExt oProduct =
                this.dlSales.getProductInfoByCode(sCode);
            if (oProduct == null) {
                Toolkit.getDefaultToolkit().beep();
                new MessageInf(MessageInf.SGN_WARNING, AppLocal
                    .getIntString("message.noproduct")).show(this);
                this.stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                if (this.m_jaddtax.isSelected()) {
                    // debemos quitarle los impuestos ya que el precio es con
                    // iva incluido...
                    final TaxInfo tax =
                        this.taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(),
                            this.m_oTicket.getDate(), this.m_oTicket
                                .getCustomer());
                    this.addTicketLine(oProduct, 1.0, dPriceSell
                        / (1.0 + tax.getRate()));
                } else {
                    this.addTicketLine(oProduct, 1.0, dPriceSell);
                }
            }
        } catch (final BasicException eData) {
            this.stateToZero();
            new MessageInf(eData).show(this);
        }
    }

    private void incProduct(final ProductInfoExt prod) {

        if (prod.isScale() && this.m_App.getDeviceScale().existsScale()) {
            try {
                final Double value = this.m_App.getDeviceScale().readWeight();
                if (value != null) {
                    this.incProduct(value.doubleValue(), prod);
                }
            } catch (final ScaleException e) {
                Toolkit.getDefaultToolkit().beep();
                new MessageInf(MessageInf.SGN_WARNING, AppLocal
                    .getIntString("message.noweight"), e).show(this);
                this.stateToZero();
            }
        } else {
            // No es un producto que se pese o no hay balanza
            this.incProduct(1.0, prod);
        }
    }

    private void incProduct(final double dPor, final ProductInfoExt prod) {
        // precondicion: prod != null
        this.addTicketLine(prod, dPor, prod.getPriceSell());
    }

    protected void buttonTransition(final ProductInfoExt prod) {
        // precondicion: prod != null

        if ((this.m_iNumberStatusInput == NUMBERZERO)
            && (this.m_iNumberStatusPor == NUMBERZERO)) {
            this.incProduct(prod);
        } else if ((this.m_iNumberStatusInput == NUMBERVALID)
            && (this.m_iNumberStatusPor == NUMBERZERO)) {
            this.incProduct(this.getInputValue(), prod);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void stateTransition(final char cTrans) {

        if (cTrans == '\n') {
            // Codigo de barras introducido
            if (this.m_sBarcode.length() > 0) {
                final String sCode = this.m_sBarcode.toString();
                if (sCode.startsWith("c")) {
                    // barcode of a customers card
                    try {
                        final CustomerInfoExt newcustomer =
                            this.dlSales.findCustomerExt(sCode);
                        if (newcustomer == null) {
                            Toolkit.getDefaultToolkit().beep();
                            new MessageInf(MessageInf.SGN_WARNING, AppLocal
                                .getIntString("message.nocustomer")).show(this);
                        } else {
                            this.m_oTicket.setCustomer(newcustomer);
                            this.m_jTicketId.setText(this.m_oTicket
                                .getName(this.m_oTicketExt));
                        }
                    } catch (final BasicException e) {
                        Toolkit.getDefaultToolkit().beep();
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal
                            .getIntString("message.nocustomer"), e).show(this);
                    }
                    this.stateToZero();
                } else if ((sCode.length() == 13) && sCode.startsWith("250")) {
                    // barcode of the other machine
                    final ProductInfoExt oProduct = new ProductInfoExt(); // Es
                                                                          // un
                                                                          // ticket
                    oProduct.setReference(null); // para que no se grabe
                    oProduct.setCode(sCode);
                    oProduct.setName("Ticket " + sCode.substring(3, 7));
                    oProduct.setPriceSell(Double.parseDouble(sCode.substring(7,
                        12)) / 100);
                    oProduct
                        .setTaxCategoryID(((TaxCategoryInfo) this.taxcategoriesmodel
                            .getSelectedItem()).getID());
                    // Se anade directamente una unidad con el precio y todo
                    this.addTicketLine(oProduct, 1.0, this.includeTaxes(
                        oProduct.getTaxCategoryID(), oProduct.getPriceSell()));
                } else if ((sCode.length() == 13) && sCode.startsWith("210")) {
                    // barcode of a weigth product
                    this.incProductByCodePrice(sCode.substring(0, 7), Double
                        .parseDouble(sCode.substring(7, 12)) / 100);
                } else {
                    this.incProductByCode(sCode);
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        } else {
            // otro caracter
            // Esto es para el codigo de barras...
            this.m_sBarcode.append(cTrans);

            // Esto es para el los productos normales...
            if (cTrans == '\u007f') {
                this.stateToZero();

            } else if ((cTrans == '0')
                && (this.m_iNumberStatus == NUMBER_INPUTZERO)) {
                this.m_jPrice.setText("0");
            } else if (((cTrans == '1') || (cTrans == '2') || (cTrans == '3')
                || (cTrans == '4') || (cTrans == '5') || (cTrans == '6')
                || (cTrans == '7') || (cTrans == '8') || (cTrans == '9'))
                && (this.m_iNumberStatus == NUMBER_INPUTZERO)) {
                // Un numero entero
                this.m_jPrice.setText(Character.toString(cTrans));
                this.m_iNumberStatus = NUMBER_INPUTINT;
                this.m_iNumberStatusInput = NUMBERVALID;
            } else if (((cTrans == '0') || (cTrans == '1') || (cTrans == '2')
                || (cTrans == '3') || (cTrans == '4') || (cTrans == '5')
                || (cTrans == '6') || (cTrans == '7') || (cTrans == '8') || (cTrans == '9'))
                && (this.m_iNumberStatus == NUMBER_INPUTINT)) {
                // Un numero entero
                this.m_jPrice.setText(this.m_jPrice.getText() + cTrans);

            } else if ((cTrans == '.')
                && (this.m_iNumberStatus == NUMBER_INPUTZERO)) {
                this.m_jPrice.setText("0.");
                this.m_iNumberStatus = NUMBER_INPUTZERODEC;
            } else if ((cTrans == '.')
                && (this.m_iNumberStatus == NUMBER_INPUTINT)) {
                this.m_jPrice.setText(this.m_jPrice.getText() + ".");
                this.m_iNumberStatus = NUMBER_INPUTDEC;

            } else if ((cTrans == '0')
                && ((this.m_iNumberStatus == NUMBER_INPUTZERODEC) || (this.m_iNumberStatus == NUMBER_INPUTDEC))) {
                // Un numero decimal
                this.m_jPrice.setText(this.m_jPrice.getText() + cTrans);
            } else if (((cTrans == '1') || (cTrans == '2') || (cTrans == '3')
                || (cTrans == '4') || (cTrans == '5') || (cTrans == '6')
                || (cTrans == '7') || (cTrans == '8') || (cTrans == '9'))
                && ((this.m_iNumberStatus == NUMBER_INPUTZERODEC) || (this.m_iNumberStatus == NUMBER_INPUTDEC))) {
                // Un numero decimal
                this.m_jPrice.setText(this.m_jPrice.getText() + cTrans);
                this.m_iNumberStatus = NUMBER_INPUTDEC;
                this.m_iNumberStatusInput = NUMBERVALID;

            } else if ((cTrans == '*')
                && ((this.m_iNumberStatus == NUMBER_INPUTINT) || (this.m_iNumberStatus == NUMBER_INPUTDEC))) {
                this.m_jPor.setText("x");
                this.m_iNumberStatus = NUMBER_PORZERO;
            } else if ((cTrans == '*')
                && ((this.m_iNumberStatus == NUMBER_INPUTZERO) || (this.m_iNumberStatus == NUMBER_INPUTZERODEC))) {
                this.m_jPrice.setText("0");
                this.m_jPor.setText("x");
                this.m_iNumberStatus = NUMBER_PORZERO;

            } else if ((cTrans == '0')
                && (this.m_iNumberStatus == NUMBER_PORZERO)) {
                this.m_jPor.setText("x0");
            } else if (((cTrans == '1') || (cTrans == '2') || (cTrans == '3')
                || (cTrans == '4') || (cTrans == '5') || (cTrans == '6')
                || (cTrans == '7') || (cTrans == '8') || (cTrans == '9'))
                && (this.m_iNumberStatus == NUMBER_PORZERO)) {
                // Un numero entero
                this.m_jPor.setText("x" + Character.toString(cTrans));
                this.m_iNumberStatus = NUMBER_PORINT;
                this.m_iNumberStatusPor = NUMBERVALID;
            } else if (((cTrans == '0') || (cTrans == '1') || (cTrans == '2')
                || (cTrans == '3') || (cTrans == '4') || (cTrans == '5')
                || (cTrans == '6') || (cTrans == '7') || (cTrans == '8') || (cTrans == '9'))
                && (this.m_iNumberStatus == NUMBER_PORINT)) {
                // Un numero entero
                this.m_jPor.setText(this.m_jPor.getText() + cTrans);

            } else if ((cTrans == '.')
                && (this.m_iNumberStatus == NUMBER_PORZERO)) {
                this.m_jPor.setText("x0.");
                this.m_iNumberStatus = NUMBER_PORZERODEC;
            } else if ((cTrans == '.')
                && (this.m_iNumberStatus == NUMBER_PORINT)) {
                this.m_jPor.setText(this.m_jPor.getText() + ".");
                this.m_iNumberStatus = NUMBER_PORDEC;

            } else if ((cTrans == '0')
                && ((this.m_iNumberStatus == NUMBER_PORZERODEC) || (this.m_iNumberStatus == NUMBER_PORDEC))) {
                // Un numero decimal
                this.m_jPor.setText(this.m_jPor.getText() + cTrans);
            } else if (((cTrans == '1') || (cTrans == '2') || (cTrans == '3')
                || (cTrans == '4') || (cTrans == '5') || (cTrans == '6')
                || (cTrans == '7') || (cTrans == '8') || (cTrans == '9'))
                && ((this.m_iNumberStatus == NUMBER_PORZERODEC) || (this.m_iNumberStatus == NUMBER_PORDEC))) {
                // Un numero decimal
                this.m_jPor.setText(this.m_jPor.getText() + cTrans);
                this.m_iNumberStatus = NUMBER_PORDEC;
                this.m_iNumberStatusPor = NUMBERVALID;

            } else if ((cTrans == '\u00a7')
                && (this.m_iNumberStatusInput == NUMBERVALID)
                && (this.m_iNumberStatusPor == NUMBERZERO)) {
                // Scale button pressed and a number typed as a price
                if (this.m_App.getDeviceScale().existsScale()
                    && this.m_App.getAppUserView().getUser().hasPermission(
                        "sales.EditLines")) {
                    try {
                        final Double value =
                            this.m_App.getDeviceScale().readWeight();
                        if (value != null) {
                            final ProductInfoExt product =
                                this.getInputProduct();
                            this.addTicketLine(product, value.doubleValue(),
                                product.getPriceSell());
                        }
                    } catch (final ScaleException e) {
                        Toolkit.getDefaultToolkit().beep();
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal
                            .getIntString("message.noweight"), e).show(this);
                        this.stateToZero();
                    }
                } else {
                    // No existe la balanza;
                    Toolkit.getDefaultToolkit().beep();
                }
            } else if ((cTrans == '\u00a7')
                && (this.m_iNumberStatusInput == NUMBERZERO)
                && (this.m_iNumberStatusPor == NUMBERZERO)) {
                // Scale button pressed and no number typed.
                final int i = this.m_ticketlines.getSelectedIndex();
                if (i < 0) {
                    Toolkit.getDefaultToolkit().beep();
                } else if (this.m_App.getDeviceScale().existsScale()) {
                    try {
                        final Double value =
                            this.m_App.getDeviceScale().readWeight();
                        if (value != null) {
                            final TicketLineInfo newline =
                                new TicketLineInfo(this.m_oTicket.getLine(i));
                            newline.setMultiply(value.doubleValue());
                            newline.setPrice(Math.abs(newline.getPrice()));
                            this.paintTicketLine(i, newline);
                        }
                    } catch (final ScaleException e) {
                        // Error de pesada.
                        Toolkit.getDefaultToolkit().beep();
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal
                            .getIntString("message.noweight"), e).show(this);
                        this.stateToZero();
                    }
                } else {
                    // No existe la balanza;
                    Toolkit.getDefaultToolkit().beep();
                }

                // Add one product more to the selected line
            } else if ((cTrans == '+')
                && (this.m_iNumberStatusInput == NUMBERZERO)
                && (this.m_iNumberStatusPor == NUMBERZERO)) {
                final int i = this.m_ticketlines.getSelectedIndex();
                if (i < 0) {
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    final TicketLineInfo newline =
                        new TicketLineInfo(this.m_oTicket.getLine(i));
                    // If it's a refund + button means one unit less
                    if (this.m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND) {
                        newline.setMultiply(newline.getMultiply() - 1.0);
                        this.paintTicketLine(i, newline);
                    } else {
                        // add one unit to the selected line
                        newline.setMultiply(newline.getMultiply() + 1.0);
                        this.paintTicketLine(i, newline);
                    }
                }

                // Delete one product of the selected line
            } else if ((cTrans == '-')
                && (this.m_iNumberStatusInput == NUMBERZERO)
                && (this.m_iNumberStatusPor == NUMBERZERO)
                && this.m_App.getAppUserView().getUser().hasPermission(
                    "sales.EditLines")) {

                final int i = this.m_ticketlines.getSelectedIndex();
                if (i < 0) {
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    final TicketLineInfo newline =
                        new TicketLineInfo(this.m_oTicket.getLine(i));
                    // If it's a refund - button means one unit more
                    if (this.m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND) {
                        newline.setMultiply(newline.getMultiply() + 1.0);
                        if (newline.getMultiply() >= 0) {
                            this.removeTicketLine(i);
                        } else {
                            this.paintTicketLine(i, newline);
                        }
                    } else {
                        // substract one unit to the selected line
                        newline.setMultiply(newline.getMultiply() - 1.0);
                        if (newline.getMultiply() <= 0.0) {
                            this.removeTicketLine(i); // elimino la linea
                        } else {
                            this.paintTicketLine(i, newline);
                        }
                    }
                }

                // Set n products to the selected line
            } else if ((cTrans == '+')
                && (this.m_iNumberStatusInput == NUMBERZERO)
                && (this.m_iNumberStatusPor == NUMBERVALID)) {
                final int i = this.m_ticketlines.getSelectedIndex();
                if (i < 0) {
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    final double dPor = this.getPorValue();
                    final TicketLineInfo newline =
                        new TicketLineInfo(this.m_oTicket.getLine(i));
                    if (this.m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND) {
                        newline.setMultiply(-dPor);
                        newline.setPrice(Math.abs(newline.getPrice()));
                        this.paintTicketLine(i, newline);
                    } else {
                        newline.setMultiply(dPor);
                        newline.setPrice(Math.abs(newline.getPrice()));
                        this.paintTicketLine(i, newline);
                    }
                }

                // Set n negative products to the selected line
            } else if ((cTrans == '-')
                && (this.m_iNumberStatusInput == NUMBERZERO)
                && (this.m_iNumberStatusPor == NUMBERVALID)
                && this.m_App.getAppUserView().getUser().hasPermission(
                    "sales.EditLines")) {

                final int i = this.m_ticketlines.getSelectedIndex();
                if (i < 0) {
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    final double dPor = this.getPorValue();
                    final TicketLineInfo newline =
                        new TicketLineInfo(this.m_oTicket.getLine(i));
                    if (this.m_oTicket.getTicketType() == TicketInfo.RECEIPT_NORMAL) {
                        newline.setMultiply(dPor);
                        newline.setPrice(-Math.abs(newline.getPrice()));
                        this.paintTicketLine(i, newline);
                    }
                }

                // Anadimos 1 producto
            } else if ((cTrans == '+')
                && (this.m_iNumberStatusInput == NUMBERVALID)
                && (this.m_iNumberStatusPor == NUMBERZERO)
                && this.m_App.getAppUserView().getUser().hasPermission(
                    "sales.EditLines")) {
                final ProductInfoExt product = this.getInputProduct();
                this.addTicketLine(product, 1.0, product.getPriceSell());

                // Anadimos 1 producto con precio negativo
            } else if ((cTrans == '-')
                && (this.m_iNumberStatusInput == NUMBERVALID)
                && (this.m_iNumberStatusPor == NUMBERZERO)
                && this.m_App.getAppUserView().getUser().hasPermission(
                    "sales.EditLines")) {
                final ProductInfoExt product = this.getInputProduct();
                this.addTicketLine(product, 1.0, -product.getPriceSell());

                // Anadimos n productos
            } else if ((cTrans == '+')
                && (this.m_iNumberStatusInput == NUMBERVALID)
                && (this.m_iNumberStatusPor == NUMBERVALID)
                && this.m_App.getAppUserView().getUser().hasPermission(
                    "sales.EditLines")) {
                final ProductInfoExt product = this.getInputProduct();
                this.addTicketLine(product, this.getPorValue(), product
                    .getPriceSell());

                // Anadimos n productos con precio negativo ?
            } else if ((cTrans == '-')
                && (this.m_iNumberStatusInput == NUMBERVALID)
                && (this.m_iNumberStatusPor == NUMBERVALID)
                && this.m_App.getAppUserView().getUser().hasPermission(
                    "sales.EditLines")) {
                final ProductInfoExt product = this.getInputProduct();
                this.addTicketLine(product, this.getPorValue(), -product
                    .getPriceSell());

                // Totals() Igual;
            } else if ((cTrans == ' ') || (cTrans == '=')) {
                if (this.m_oTicket.getLinesCount() > 0) {

                    if (this.closeTicket(this.m_oTicket, this.m_oTicketExt)) {
                        // Ends edition of current receipt
                        this.m_ticketsbag.deleteTicket();
                    } else {
                        // repaint current ticket
                        this.refreshTicket();
                    }
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
    }

    private boolean
    closeTicket(final TicketInfo ticket, final Object ticketext) {

        boolean resultok = false;

        if (this.m_App.getAppUserView().getUser().hasPermission("sales.Total")) {

            try {
                // reset the payment info
                this.taxeslogic.calculateTaxes(ticket);
                if (ticket.getTotal() >= 0.0) {
                    ticket.resetPayments(); // Only reset if is sale
                }

                if (this.executeEvent(ticket, ticketext, "ticket.total") == null) {

                    // Muestro el total
                    this.printTicket("Printer.TicketTotal", ticket, ticketext);

                    // Select the Payments information
                    final JPaymentSelect paymentdialog =
                        ticket.getTicketType() == TicketInfo.RECEIPT_NORMAL
                            ? this.paymentdialogreceipt
                            : this.paymentdialogrefund;
                    paymentdialog.setPrintSelected("true"
                        .equals(this.m_jbtnconfig.getProperty("printselected",
                            "true")));

                    paymentdialog.setTransactionID(ticket.getTransactionID());

                    if (paymentdialog.showDialog(ticket.getTotal(), ticket
                        .getCustomer())) {

                        // assign the payments selected and calculate taxes.
                        ticket.setPayments(paymentdialog.getSelectedPayments());

                        // Asigno los valores definitivos del ticket...
                        ticket.setUser(this.m_App.getAppUserView().getUser()
                            .getUserInfo()); // El usuario que lo cobra
                        ticket.setActiveCash(this.m_App.getActiveCashIndex());
                        ticket.setDate(new Date()); // Le pongo la fecha de
                                                    // cobro

                        if (this.executeEvent(ticket, ticketext, "ticket.save") == null) {
                            // Save the receipt and assign a receipt number
                            try {
                                this.dlSales.saveTicket(ticket, this.m_App
                                    .getInventoryLocation());
                            } catch (final BasicException eData) {
                                final MessageInf msg =
                                    new MessageInf(
                                        MessageInf.SGN_NOTICE,
                                        AppLocal
                                            .getIntString("message.nosaveticket"),
                                        eData);
                                msg.show(this);
                            }

                            this.executeEvent(ticket, ticketext,
                                "ticket.close", new ScriptArg("print",
                                    paymentdialog.isPrintSelected()));

                            // Print receipt.
                            this.printTicket(paymentdialog.isPrintSelected()
                                ? "Printer.Ticket" : "Printer.Ticket2", ticket,
                                ticketext);
                            resultok = true;
                        }
                    }
                }
            } catch (final TaxesException e) {
                final MessageInf msg =
                    new MessageInf(MessageInf.SGN_WARNING, AppLocal
                        .getIntString("message.cannotcalculatetaxes"));
                msg.show(this);
                resultok = false;
            }

            // reset the payment info
            this.m_oTicket.resetTaxes();
            this.m_oTicket.resetPayments();
        }

        // cancelled the ticket.total script
        // or canceled the payment dialog
        // or canceled the ticket.close script
        return resultok;
    }

    private void printTicket(final String sresourcename,
    final TicketInfo ticket, final Object ticketext) {

        final String sresource = this.dlSystem.getResourceAsXML(sresourcename);
        if (sresource == null) {
            final MessageInf msg =
                new MessageInf(MessageInf.SGN_WARNING, AppLocal
                    .getIntString("message.cannotprintticket"));
            msg.show(JPanelTicket.this);
        } else {
            try {
                final ScriptEngine script =
                    ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", this.taxcollection);
                script.put("taxeslogic", this.taxeslogic);
                script.put("ticket", ticket);
                script.put("place", ticketext);
                this.m_TTP.printTicket(script.eval(sresource).toString());
            } catch (final ScriptException e) {
                final MessageInf msg =
                    new MessageInf(MessageInf.SGN_WARNING, AppLocal
                        .getIntString("message.cannotprintticket"), e);
                msg.show(JPanelTicket.this);
            } catch (final TicketPrinterException e) {
                final MessageInf msg =
                    new MessageInf(MessageInf.SGN_WARNING, AppLocal
                        .getIntString("message.cannotprintticket"), e);
                msg.show(JPanelTicket.this);
            }
        }
    }

    private void printReport(final String resourcefile,
    final TicketInfo ticket, final Object ticketext) {

        try {

            JasperReport jr;

            final InputStream in =
                this.getClass().getResourceAsStream(resourcefile + ".ser");
            if (in == null) {
                // read and compile the report
                final JasperDesign jd =
                    JRXmlLoader.load(this.getClass().getResourceAsStream(
                        resourcefile + ".jrxml"));
                jr = JasperCompileManager.compileReport(jd);
            } else {
                // read the compiled reporte
                final ObjectInputStream oin = new ObjectInputStream(in);
                jr = (JasperReport) oin.readObject();
                oin.close();
            }

            // Construyo el mapa de los parametros.
            final Map reportparams = new HashMap();
            // reportparams.put("ARG", params);
            try {
                reportparams.put("REPORT_RESOURCE_BUNDLE", ResourceBundle
                    .getBundle(resourcefile + ".properties"));
            } catch (final MissingResourceException e) {
            }
            reportparams.put("TAXESLOGIC", this.taxeslogic);

            final Map reportfields = new HashMap();
            reportfields.put("TICKET", ticket);
            reportfields.put("PLACE", ticketext);

            final JasperPrint jp =
                JasperFillManager.fillReport(jr, reportparams,
                    new JRMapArrayDataSource(new Object[] {
                        reportfields
                    }));

            final PrintService service =
                ReportUtils.getPrintService(this.m_App.getProperties()
                    .getProperty("machine.printername"));

            JRPrinterAWT300
                .printPages(jp, 0, jp.getPages().size() - 1, service);

        } catch (final Exception e) {
            final MessageInf msg =
                new MessageInf(MessageInf.SGN_WARNING, AppLocal
                    .getIntString("message.cannotloadreport"), e);
            msg.show(this);
        }
    }

    private void visorTicketLine(final TicketLineInfo oLine) {
        if (oLine == null) {
            this.m_App.getDeviceTicket().getDeviceDisplay().clearVisor();
        } else {
            try {
                final ScriptEngine script =
                    ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticketline", oLine);
                this.m_TTP.printTicket(script.eval(
                    this.dlSystem.getResourceAsXML("Printer.TicketLine"))
                    .toString());
            } catch (final ScriptException e) {
                final MessageInf msg =
                    new MessageInf(MessageInf.SGN_WARNING, AppLocal
                        .getIntString("message.cannotprintline"), e);
                msg.show(JPanelTicket.this);
            } catch (final TicketPrinterException e) {
                final MessageInf msg =
                    new MessageInf(MessageInf.SGN_WARNING, AppLocal
                        .getIntString("message.cannotprintline"), e);
                msg.show(JPanelTicket.this);
            }
        }
    }

    private Object evalScript(final ScriptObject scr, final String resource,
    final ScriptArg... args) {

        // resource here is guaratied to be not null
        try {
            scr.setSelectedIndex(this.m_ticketlines.getSelectedIndex());
            return scr.evalScript(this.dlSystem.getResourceAsXML(resource),
                args);
        } catch (final ScriptException e) {
            final MessageInf msg =
                new MessageInf(MessageInf.SGN_WARNING, AppLocal
                    .getIntString("message.cannotexecute"), e);
            msg.show(this);
            return msg;
        }
    }

    public void evalScriptAndRefresh(final String resource,
    final ScriptArg... args) {

        if (resource == null) {
            final MessageInf msg =
                new MessageInf(MessageInf.SGN_WARNING, AppLocal
                    .getIntString("message.cannotexecute"));
            msg.show(this);
        } else {
            final ScriptObject scr =
                new ScriptObject(this.m_oTicket, this.m_oTicketExt);
            scr.setSelectedIndex(this.m_ticketlines.getSelectedIndex());
            this.evalScript(scr, resource, args);
            this.refreshTicket();
            this.setSelectedIndex(scr.getSelectedIndex());
        }
    }

    public void printTicket(final String resource) {
        this.printTicket(resource, this.m_oTicket, this.m_oTicketExt);
    }

    private Object executeEventAndRefresh(final String eventkey,
    final ScriptArg... args) {

        final String resource = this.m_jbtnconfig.getEvent(eventkey);
        if (resource == null) {
            return null;
        } else {
            final ScriptObject scr =
                new ScriptObject(this.m_oTicket, this.m_oTicketExt);
            scr.setSelectedIndex(this.m_ticketlines.getSelectedIndex());
            final Object result = this.evalScript(scr, resource, args);
            this.refreshTicket();
            this.setSelectedIndex(scr.getSelectedIndex());
            return result;
        }
    }

    private Object executeEvent(final TicketInfo ticket,
    final Object ticketext, final String eventkey, final ScriptArg... args) {

        final String resource = this.m_jbtnconfig.getEvent(eventkey);
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("eventkey: " + eventkey + ", resource: " + resource);
        }
        if (resource == null) {
            return null;
        } else {
            final ScriptObject scr = new ScriptObject(ticket, ticketext);
            return this.evalScript(scr, resource, args);
        }
    }

    public String getResourceAsXML(final String sresourcename) {
        return this.dlSystem.getResourceAsXML(sresourcename);
    }

    public BufferedImage getResourceAsImage(final String sresourcename) {
        return this.dlSystem.getResourceAsImage(sresourcename);
    }

    private void setSelectedIndex(final int i) {

        if ((i >= 0) && (i < this.m_oTicket.getLinesCount())) {
            this.m_ticketlines.setSelectedIndex(i);
        } else if (this.m_oTicket.getLinesCount() > 0) {
            this.m_ticketlines
                .setSelectedIndex(this.m_oTicket.getLinesCount() - 1);
        }
    }

    public static class ScriptArg {

        private final String key;

        private final Object value;

        public ScriptArg(final String key, final Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }
    }

    public class ScriptObject {

        private final TicketInfo ticket;

        private final Object ticketext;

        private int selectedindex;

        private ScriptObject(final TicketInfo ticket, final Object ticketext) {
            this.ticket = ticket;
            this.ticketext = ticketext;
        }

        public double getInputValue() {
            if ((JPanelTicket.this.m_iNumberStatusInput == NUMBERVALID)
                && (JPanelTicket.this.m_iNumberStatusPor == NUMBERZERO)) {
                return JPanelTicket.this.getInputValue();
            } else {
                return 0.0;
            }
        }

        public int getSelectedIndex() {
            return this.selectedindex;
        }

        public void setSelectedIndex(final int i) {
            this.selectedindex = i;
        }

        public void printReport(final String resourcefile) {
            JPanelTicket.this.printReport(resourcefile, this.ticket,
                this.ticketext);
        }

        public void printTicket(final String sresourcename) {
            JPanelTicket.this.printTicket(sresourcename, this.ticket,
                this.ticketext);
        }

        public Object evalScript(final String code, final ScriptArg... args)
        throws ScriptException {

            final ScriptEngine script =
                ScriptFactory.getScriptEngine(ScriptFactory.BEANSHELL);
            script.put("ticket", this.ticket);
            script.put("place", this.ticketext);
            script.put("taxes", JPanelTicket.this.taxcollection);
            script.put("taxeslogic", JPanelTicket.this.taxeslogic);
            script.put("user", JPanelTicket.this.m_App.getAppUserView()
                .getUser());
            script.put("sales", this);

            // more arguments
            for (final ScriptArg arg : args) {
                script.put(arg.getKey(), arg.getValue());
            }

            return script.eval(code);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed"
    // desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        this.m_jPanContainer = new javax.swing.JPanel();
        this.m_jOptions = new javax.swing.JPanel();
        this.m_jButtons = new javax.swing.JPanel();
        this.m_jTicketId = new javax.swing.JLabel();
        this.btnCustomer = new javax.swing.JButton();
        this.btnMembership = new javax.swing.JButton();
        this.btnOldMembership = new javax.swing.JButton();
        this.btnSplit = new javax.swing.JButton();
        this.m_jPanelScripts = new javax.swing.JPanel();
        this.m_jButtonsExt = new javax.swing.JPanel();
        this.jPanel1 = new javax.swing.JPanel();
        this.m_jbtnScale = new javax.swing.JButton();
        this.m_jPanelBag = new javax.swing.JPanel();
        this.m_jPanTicket = new javax.swing.JPanel();
        this.jPanel5 = new javax.swing.JPanel();
        this.jPanel2 = new javax.swing.JPanel();
        this.m_jUp = new javax.swing.JButton();
        this.m_jDown = new javax.swing.JButton();
        this.m_jDelete = new javax.swing.JButton();
        this.m_jList = new javax.swing.JButton();
        this.m_jEditLine = new javax.swing.JButton();
        this.jEditAttributes = new javax.swing.JButton();
        this.m_jPanelCentral = new javax.swing.JPanel();
        this.jPanel4 = new javax.swing.JPanel();
        this.m_jPanTotals = new javax.swing.JPanel();
        this.m_jTotalEuros = new javax.swing.JLabel();
        this.m_jLblTotalEuros1 = new javax.swing.JLabel();
        this.m_jSubtotalEuros = new javax.swing.JLabel();
        this.m_jTaxesEuros = new javax.swing.JLabel();
        this.m_jLblTotalEuros2 = new javax.swing.JLabel();
        this.m_jLblTotalEuros3 = new javax.swing.JLabel();
        this.m_jContEntries = new javax.swing.JPanel();
        this.m_jPanEntries = new javax.swing.JPanel();
        this.m_jNumberKeys = new com.openbravo.beans.JNumberKeys();
        this.jPanel9 = new javax.swing.JPanel();
        this.m_jPrice = new javax.swing.JLabel();
        this.m_jPor = new javax.swing.JLabel();
        this.m_jEnter = new javax.swing.JButton();
        this.m_jTax = new javax.swing.JComboBox();
        this.m_jaddtax = new javax.swing.JToggleButton();
        this.m_jKeyFactory = new javax.swing.JTextField();
        this.catcontainer = new javax.swing.JPanel();

        this.setBackground(new java.awt.Color(255, 204, 153));
        this.setLayout(new java.awt.CardLayout());

        this.m_jPanContainer.setLayout(new java.awt.BorderLayout());

        this.m_jOptions.setLayout(new java.awt.BorderLayout());

        this.m_jTicketId.setBackground(java.awt.Color.white);
        this.m_jTicketId
            .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        this.m_jTicketId.setBorder(javax.swing.BorderFactory
            .createCompoundBorder(javax.swing.BorderFactory
                .createLineBorder(javax.swing.UIManager.getDefaults().getColor(
                    "Button.darkShadow")), javax.swing.BorderFactory
                .createEmptyBorder(1, 4, 1, 4)));
        this.m_jTicketId.setOpaque(true);
        // XXX: CINEMA
        this.m_jTicketId.setPreferredSize(new java.awt.Dimension(140, 25));
        // this.m_jTicketId.setPreferredSize(new java.awt.Dimension(160, 25));
        this.m_jTicketId.setRequestFocusEnabled(false);
        this.m_jButtons.add(this.m_jTicketId);

        this.btnCustomer.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/kuser.png"))); // NOI18N
        this.btnCustomer.setFocusPainted(false);
        this.btnCustomer.setFocusable(false);
        this.btnCustomer.setMargin(new java.awt.Insets(20, 26, 20, 26));
        this.btnCustomer.setRequestFocusEnabled(false);
        this.btnCustomer.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                JPanelTicket.this.btnCustomerActionPerformed(evt);
            }
        });
        this.jPanel2.add(this.btnCustomer);

        this.btnMembership.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/colorize.png"))); // NOI18N
        this.btnMembership.setFocusPainted(false);
        this.btnMembership.setFocusable(false);
        this.btnMembership.setMargin(new java.awt.Insets(8, 14, 8, 14));
        this.btnMembership.setRequestFocusEnabled(false);
        this.btnMembership
            .addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void
                actionPerformed(final java.awt.event.ActionEvent evt) {
                    JPanelTicket.this.btnMembershipActionPerformed(evt);
                }
            });
        this.jPanel2.add(this.btnMembership);
        
        // old membership button

        this.btnOldMembership.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/contents.png"))); // NOI18N
        this.btnOldMembership.setFocusPainted(false);
        this.btnOldMembership.setFocusable(false);
        this.btnOldMembership.setMargin(new java.awt.Insets(8, 14, 8, 14));
        this.btnOldMembership.setRequestFocusEnabled(false);
        this.btnOldMembership
            .addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void
                actionPerformed(final java.awt.event.ActionEvent evt) {
                    JPanelTicket.this.btnOldMembershipActionPerformed(evt);
                }
            });
        this.jPanel2.add(this.btnOldMembership);


        this.btnSplit.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/editcut.png"))); // NOI18N
        this.btnSplit.setFocusPainted(false);
        this.btnSplit.setFocusable(false);
        this.btnSplit.setMargin(new java.awt.Insets(8, 14, 8, 14));
        this.btnSplit.setRequestFocusEnabled(false);
        this.btnSplit.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                JPanelTicket.this.btnSplitActionPerformed(evt);
            }
        });
        // XXX: CINEMA
        // m_jButtons.add(btnSplit);

        this.m_jOptions.add(this.m_jButtons, java.awt.BorderLayout.LINE_START);

        this.m_jPanelScripts.setLayout(new java.awt.BorderLayout());

        this.m_jButtonsExt.setLayout(new javax.swing.BoxLayout(
            this.m_jButtonsExt, javax.swing.BoxLayout.LINE_AXIS));

        this.m_jbtnScale.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/ark216.png"))); // NOI18N
        this.m_jbtnScale.setText(AppLocal.getIntString("button.scale")); // NOI18N
        this.m_jbtnScale.setFocusPainted(false);
        this.m_jbtnScale.setFocusable(false);
        this.m_jbtnScale.setMargin(new java.awt.Insets(8, 14, 8, 14));
        this.m_jbtnScale.setRequestFocusEnabled(false);
        this.m_jbtnScale.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                JPanelTicket.this.m_jbtnScaleActionPerformed(evt);
            }
        });
        this.jPanel1.add(this.m_jbtnScale);

        this.m_jButtonsExt.add(this.jPanel1);

        this.m_jPanelScripts.add(this.m_jButtonsExt,
            java.awt.BorderLayout.LINE_END);

        this.m_jOptions.add(this.m_jPanelScripts,
            java.awt.BorderLayout.LINE_END);

        this.m_jPanelBag.setLayout(new java.awt.BorderLayout());
        this.m_jOptions.add(this.m_jPanelBag, java.awt.BorderLayout.CENTER);

        this.m_jPanContainer.add(this.m_jOptions, java.awt.BorderLayout.NORTH);

        this.m_jPanTicket.setBorder(javax.swing.BorderFactory
            .createEmptyBorder(5, 5, 5, 5));
        this.m_jPanTicket.setLayout(new java.awt.BorderLayout());

        this.jPanel5.setLayout(new java.awt.BorderLayout());

        this.jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,
            5, 0, 5));
        this.jPanel2.setLayout(new java.awt.GridLayout(0, 1, 5, 5));

        this.m_jUp.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/1uparrow22.png"))); // NOI18N
        this.m_jUp.setFocusPainted(false);
        this.m_jUp.setFocusable(false);
        this.m_jUp.setMargin(new java.awt.Insets(8, 14, 8, 14));
        this.m_jUp.setRequestFocusEnabled(false);
        this.m_jUp.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                JPanelTicket.this.m_jUpActionPerformed(evt);
            }
        });
        this.jPanel2.add(this.m_jUp);

        this.m_jDown.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/1downarrow22.png"))); // NOI18N
        this.m_jDown.setFocusPainted(false);
        this.m_jDown.setFocusable(false);
        this.m_jDown.setMargin(new java.awt.Insets(8, 14, 8, 14));
        this.m_jDown.setRequestFocusEnabled(false);
        this.m_jDown.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                JPanelTicket.this.m_jDownActionPerformed(evt);
            }
        });
        this.jPanel2.add(this.m_jDown);

        this.m_jDelete.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
        this.m_jDelete.setFocusPainted(false);
        this.m_jDelete.setFocusable(false);
        this.m_jDelete.setMargin(new java.awt.Insets(8, 14, 8, 14));
        this.m_jDelete.setRequestFocusEnabled(false);
        this.m_jDelete.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                JPanelTicket.this.m_jDeleteActionPerformed(evt);
            }
        });
        this.jPanel2.add(this.m_jDelete);

        this.m_jList.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/search22.png"))); // NOI18N
        this.m_jList.setFocusPainted(false);
        this.m_jList.setFocusable(false);
        this.m_jList.setMargin(new java.awt.Insets(8, 14, 8, 14));
        this.m_jList.setRequestFocusEnabled(false);
        this.m_jList.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                JPanelTicket.this.m_jListActionPerformed(evt);
            }
        });
        // this.jPanel2.add(this.m_jList);

        this.m_jEditLine.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/color_line.png"))); // NOI18N
        this.m_jEditLine.setFocusPainted(false);
        this.m_jEditLine.setFocusable(false);
        this.m_jEditLine.setMargin(new java.awt.Insets(8, 14, 8, 14));
        this.m_jEditLine.setRequestFocusEnabled(false);
        this.m_jEditLine.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                JPanelTicket.this.m_jEditLineActionPerformed(evt);
            }
        });
        // this.jPanel2.add(this.m_jEditLine);

        this.jEditAttributes.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/colorize.png"))); // NOI18N
        this.jEditAttributes.setFocusPainted(false);
        this.jEditAttributes.setFocusable(false);
        this.jEditAttributes.setMargin(new java.awt.Insets(8, 14, 8, 14));
        this.jEditAttributes.setRequestFocusEnabled(false);
        this.jEditAttributes
            .addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void
                actionPerformed(final java.awt.event.ActionEvent evt) {
                    JPanelTicket.this.jEditAttributesActionPerformed(evt);
                }
            });
        // this.jPanel2.add(this.jEditAttributes);

        this.jPanel5.add(this.jPanel2, java.awt.BorderLayout.NORTH);

        this.m_jPanTicket.add(this.jPanel5, java.awt.BorderLayout.LINE_END);

        this.m_jPanelCentral.setLayout(new java.awt.BorderLayout());

        this.jPanel4.setLayout(new java.awt.BorderLayout());

        this.m_jPanTotals.setLayout(new java.awt.GridBagLayout());

        this.m_jTotalEuros.setBackground(java.awt.Color.white);
        this.m_jTotalEuros.setFont(new java.awt.Font("Dialog", 1, 14));
        this.m_jTotalEuros
            .setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        this.m_jTotalEuros.setBorder(javax.swing.BorderFactory
            .createCompoundBorder(javax.swing.BorderFactory
                .createLineBorder(javax.swing.UIManager.getDefaults().getColor(
                    "Button.darkShadow")), javax.swing.BorderFactory
                .createEmptyBorder(1, 4, 1, 4)));
        this.m_jTotalEuros.setOpaque(true);
        this.m_jTotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        this.m_jTotalEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor =
            java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.m_jPanTotals.add(this.m_jTotalEuros, gridBagConstraints);

        this.m_jLblTotalEuros1
            .setText(AppLocal.getIntString("label.totalcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor =
            java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.m_jPanTotals.add(this.m_jLblTotalEuros1, gridBagConstraints);

        this.m_jSubtotalEuros.setBackground(java.awt.Color.white);
        this.m_jSubtotalEuros
            .setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        this.m_jSubtotalEuros.setBorder(javax.swing.BorderFactory
            .createCompoundBorder(javax.swing.BorderFactory
                .createLineBorder(javax.swing.UIManager.getDefaults().getColor(
                    "Button.darkShadow")), javax.swing.BorderFactory
                .createEmptyBorder(1, 4, 1, 4)));
        this.m_jSubtotalEuros.setOpaque(true);
        this.m_jSubtotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        this.m_jSubtotalEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor =
            java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.m_jPanTotals.add(this.m_jSubtotalEuros, gridBagConstraints);

        this.m_jTaxesEuros.setBackground(java.awt.Color.white);
        this.m_jTaxesEuros
            .setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        this.m_jTaxesEuros.setBorder(javax.swing.BorderFactory
            .createCompoundBorder(javax.swing.BorderFactory
                .createLineBorder(javax.swing.UIManager.getDefaults().getColor(
                    "Button.darkShadow")), javax.swing.BorderFactory
                .createEmptyBorder(1, 4, 1, 4)));
        this.m_jTaxesEuros.setOpaque(true);
        this.m_jTaxesEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        this.m_jTaxesEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor =
            java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        this.m_jPanTotals.add(this.m_jTaxesEuros, gridBagConstraints);

        this.m_jLblTotalEuros2.setText(AppLocal.getIntString("label.taxcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor =
            java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        this.m_jPanTotals.add(this.m_jLblTotalEuros2, gridBagConstraints);

        this.m_jLblTotalEuros3.setText(AppLocal
            .getIntString("label.subtotalcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor =
            java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.m_jPanTotals.add(this.m_jLblTotalEuros3, gridBagConstraints);

        this.jPanel4.add(this.m_jPanTotals, java.awt.BorderLayout.LINE_END);

        this.m_jPanelCentral.add(this.jPanel4, java.awt.BorderLayout.SOUTH);

        this.m_jPanTicket.add(this.m_jPanelCentral,
            java.awt.BorderLayout.CENTER);

        this.m_jPanContainer.add(this.m_jPanTicket,
            java.awt.BorderLayout.CENTER);

        this.m_jContEntries.setLayout(new java.awt.BorderLayout());

        this.m_jPanEntries.setLayout(new javax.swing.BoxLayout(
            this.m_jPanEntries, javax.swing.BoxLayout.Y_AXIS));

        this.m_jNumberKeys
            .addJNumberEventListener(new com.openbravo.beans.JNumberEventListener() {

                @Override
                public void keyPerformed(
                final com.openbravo.beans.JNumberEvent evt) {
                    JPanelTicket.this.m_jNumberKeysKeyPerformed(evt);
                }
            });
        this.m_jPanEntries.add(this.m_jNumberKeys);

        this.jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,
            5, 5, 5));
        this.jPanel9.setLayout(new java.awt.GridBagLayout());

        this.m_jPrice.setBackground(java.awt.Color.white);
        this.m_jPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        this.m_jPrice.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager
                .getDefaults().getColor("Button.darkShadow")),
            javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        this.m_jPrice.setOpaque(true);
        this.m_jPrice.setPreferredSize(new java.awt.Dimension(100, 22));
        this.m_jPrice.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        // this.jPanel9.add(this.m_jPrice, gridBagConstraints);

        this.m_jPor.setBackground(java.awt.Color.white);
        this.m_jPor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        this.m_jPor.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager
                .getDefaults().getColor("Button.darkShadow")),
            javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        this.m_jPor.setOpaque(true);
        this.m_jPor.setPreferredSize(new java.awt.Dimension(22, 22));
        this.m_jPor.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        // this.jPanel9.add(this.m_jPor, gridBagConstraints);

        this.m_jEnter.setIcon(new javax.swing.ImageIcon(this.getClass()
            .getResource("/com/openbravo/images/barcode.png"))); // NOI18N
        this.m_jEnter.setFocusPainted(false);
        this.m_jEnter.setFocusable(false);
        this.m_jEnter.setRequestFocusEnabled(false);
        this.m_jEnter.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                JPanelTicket.this.m_jEnterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        // this.jPanel9.add(this.m_jEnter, gridBagConstraints);

        this.m_jTax.setFocusable(false);
        this.m_jTax.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        this.jPanel9.add(this.m_jTax, gridBagConstraints);

        this.m_jaddtax.setText("+");
        this.m_jaddtax.setFocusPainted(false);
        this.m_jaddtax.setFocusable(false);
        this.m_jaddtax.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        this.jPanel9.add(this.m_jaddtax, gridBagConstraints);

        this.m_jPanEntries.add(this.jPanel9);

        this.m_jKeyFactory.setBackground(javax.swing.UIManager.getDefaults()
            .getColor("Panel.background"));
        this.m_jKeyFactory.setForeground(javax.swing.UIManager.getDefaults()
            .getColor("Panel.background"));
        this.m_jKeyFactory.setBorder(null);
        this.m_jKeyFactory.setCaretColor(javax.swing.UIManager.getDefaults()
            .getColor("Panel.background"));
        this.m_jKeyFactory.setPreferredSize(new java.awt.Dimension(1, 1));
        this.m_jKeyFactory.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyTyped(final java.awt.event.KeyEvent evt) {
                JPanelTicket.this.m_jKeyFactoryKeyTyped(evt);
            }
        });
        this.m_jPanEntries.add(this.m_jKeyFactory);

        this.m_jContEntries
            .add(this.m_jPanEntries, java.awt.BorderLayout.NORTH);

        this.m_jPanContainer.add(this.m_jContEntries,
            java.awt.BorderLayout.LINE_END);

        this.catcontainer.setBorder(javax.swing.BorderFactory
            .createEmptyBorder(5, 5, 5, 5));
        this.catcontainer.setLayout(new java.awt.BorderLayout());
        this.m_jPanContainer
            .add(this.catcontainer, java.awt.BorderLayout.SOUTH);

        this.add(this.m_jPanContainer, "ticket");
    }// </editor-fold>//GEN-END:initComponents

    private void
    m_jbtnScaleActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jbtnScaleActionPerformed

        this.stateTransition('\u00a7');

    }// GEN-LAST:event_m_jbtnScaleActionPerformed

    private void
    m_jEditLineActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jEditLineActionPerformed

        final int i = this.m_ticketlines.getSelectedIndex();
        if (i < 0) {
            Toolkit.getDefaultToolkit().beep(); // no line selected
        } else {
            try {
                final TicketLineInfo newline =
                    JProductLineEdit.showMessage(this, this.m_App,
                        this.m_oTicket.getLine(i));
                if (newline != null) {
                    // line has been modified
                    this.paintTicketLine(i, newline);
                }
            } catch (final BasicException e) {
                new MessageInf(e).show(this);
            }
        }

    }// GEN-LAST:event_m_jEditLineActionPerformed

    private void m_jEnterActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jEnterActionPerformed

        this.stateTransition('\n');

    }// GEN-LAST:event_m_jEnterActionPerformed

    private void m_jNumberKeysKeyPerformed(
    final com.openbravo.beans.JNumberEvent evt) {// GEN-FIRST:event_m_jNumberKeysKeyPerformed

        this.stateTransition(evt.getKey());

    }// GEN-LAST:event_m_jNumberKeysKeyPerformed

    private void m_jKeyFactoryKeyTyped(final java.awt.event.KeyEvent evt) {// GEN-FIRST:event_m_jKeyFactoryKeyTyped

        this.m_jKeyFactory.setText(null);
        this.stateTransition(evt.getKeyChar());

    }// GEN-LAST:event_m_jKeyFactoryKeyTyped

    private void m_jDeleteActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jDeleteActionPerformed

        final int i = this.m_ticketlines.getSelectedIndex();
        if (i < 0) {
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {
            this.removeTicketLine(i); // elimino la linea
        }

    }// GEN-LAST:event_m_jDeleteActionPerformed

    private void m_jUpActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jUpActionPerformed

        this.m_ticketlines.selectionUp();

    }// GEN-LAST:event_m_jUpActionPerformed

    private void m_jDownActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jDownActionPerformed

        this.m_ticketlines.selectionDown();

    }// GEN-LAST:event_m_jDownActionPerformed

    private void m_jListActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_m_jListActionPerformed

        final ProductInfoExt prod =
            JProductFinder.showMessage(JPanelTicket.this, this.dlSales);
        if (prod != null) {
            this.buttonTransition(prod);
        }

    }// GEN-LAST:event_m_jListActionPerformed

    private void
    btnCustomerActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCustomerActionPerformed
        // XXX: CINEMA
        final String ticketsbag =
            this.m_App.getProperties().getProperty("machine.ticketsbag");
        if ("cinema".equals(ticketsbag)) {
            ((CinemaReservationMap) this.m_ticketsbag).doCustomerSearch();

            return;
        }

        final JCustomerFinder finder =
            JCustomerFinder.getCustomerFinder(this, this.dlCustomers);
        finder.search(this.m_oTicket.getCustomer());
        finder.setVisible(true);

        try {
            this.m_oTicket.setCustomer(finder.getSelectedCustomer() == null
                ? null : this.dlSales.loadCustomerExt(finder
                    .getSelectedCustomer().getId()));
        } catch (final BasicException e) {
            final MessageInf msg =
                new MessageInf(MessageInf.SGN_WARNING, AppLocal
                    .getIntString("message.cannotfindcustomer"), e);
            msg.show(this);
        }

        this.refreshTicket();

    }// GEN-LAST:event_btnCustomerActionPerformed

    private void btnMembershipActionPerformed(
    final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCustomerActionPerformed
        // XXX: CINEMA
        // XXX: CINEMA
        final String ticketsbag =
            this.m_App.getProperties().getProperty("machine.ticketsbag");
        if ("cinema".equals(ticketsbag)) {
            ((CinemaReservationMap) this.m_ticketsbag).doMembershipAdd();

            return;
        }

        final JCustomerFinder finder =
            JCustomerFinder.getCustomerFinder(this, this.dlCustomers);
        finder.search(this.m_oTicket.getCustomer());
        finder.setVisible(true);

        try {
            this.m_oTicket.setCustomer(finder.getSelectedCustomer() == null
                ? null : this.dlSales.loadCustomerExt(finder
                    .getSelectedCustomer().getId()));
        } catch (final BasicException e) {
            final MessageInf msg =
                new MessageInf(MessageInf.SGN_WARNING, AppLocal
                    .getIntString("message.cannotfindcustomer"), e);
            msg.show(this);
        }

        this.refreshTicket();

    }// GEN-LAST:event_btnCustomerActionPerformed
    
    
    // add old memberships
    private void btnOldMembershipActionPerformed(
    	    final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCustomerActionPerformed
    	        // XXX: CINEMA
    	        // XXX: CINEMA
    	        final String ticketsbag =
    	            this.m_App.getProperties().getProperty("machine.ticketsbag");
    	        if ("cinema".equals(ticketsbag)) {
    	            ((CinemaReservationMap) this.m_ticketsbag).doOldMembershipAdd();

    	            return;
    	        }

    	        final JCustomerFinder finder =
    	            JCustomerFinder.getCustomerFinder(this, this.dlCustomers);
    	        finder.search(this.m_oTicket.getCustomer());
    	        finder.setVisible(true);

    	        try {
    	            this.m_oTicket.setCustomer(finder.getSelectedCustomer() == null
    	                ? null : this.dlSales.loadCustomerExt(finder
    	                    .getSelectedCustomer().getId()));
    	        } catch (final BasicException e) {
    	            final MessageInf msg =
    	                new MessageInf(MessageInf.SGN_WARNING, AppLocal
    	                    .getIntString("message.cannotfindcustomer"), e);
    	            msg.show(this);
    	        }

    	        this.refreshTicket();

    	    }// GEN-LAST:event_btnCustomerActionPerformed

    private void btnSplitActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSplitActionPerformed

        if (this.m_oTicket.getLinesCount() > 0) {
            final ReceiptSplit splitdialog =
                ReceiptSplit.getDialog(this, this.dlSystem
                    .getResourceAsXML("Ticket.Line"), this.dlSales,
                    this.dlCustomers, this.taxeslogic);

            final TicketInfo ticket1 = this.m_oTicket.copyTicket();
            final TicketInfo ticket2 = new TicketInfo();
            ticket2.setCustomer(this.m_oTicket.getCustomer());

            if (splitdialog.showDialog(ticket1, ticket2, this.m_oTicketExt)) {
                if (this.closeTicket(ticket2, this.m_oTicketExt)) { // already
                                                                    // checked
                                                                    // that
                                                                    // number of
                                                                    // lines > 0
                    this.setActiveTicket(ticket1, this.m_oTicketExt);// set
                                                                     // result
                                                                     // ticket
                }
            }
        }

    }// GEN-LAST:event_btnSplitActionPerformed

    private void jEditAttributesActionPerformed(
    final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jEditAttributesActionPerformed

        final int i = this.m_ticketlines.getSelectedIndex();
        if (i < 0) {
            Toolkit.getDefaultToolkit().beep(); // no line selected
        } else {
            try {
                final TicketLineInfo line = this.m_oTicket.getLine(i);
                final JProductAttEdit attedit =
                    JProductAttEdit.getAttributesEditor(this, this.m_App
                        .getSession());
                attedit.editAttributes(line.getProductAttSetId(), line
                    .getProductAttSetInstId());
                attedit.setVisible(true);
                if (attedit.isOK()) {
                    // The user pressed OK
                    line.setProductAttSetInstId(attedit.getAttributeSetInst());
                    line.setProductAttSetInstDesc(attedit
                        .getAttributeSetInstDescription());
                    this.paintTicketLine(i, line);
                }
            } catch (final BasicException ex) {
                final MessageInf msg =
                    new MessageInf(MessageInf.SGN_WARNING, AppLocal
                        .getIntString("message.cannotfindattributes"), ex);
                msg.show(this);
            }
        }

    }// GEN-LAST:event_jEditAttributesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCustomer;

    private javax.swing.JButton btnSplit;

    private javax.swing.JButton btnMembership;
    
    private javax.swing.JButton btnOldMembership;

    private javax.swing.JPanel catcontainer;

    private javax.swing.JButton jEditAttributes;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel9;

    private javax.swing.JPanel m_jButtons;

    private javax.swing.JPanel m_jButtonsExt;

    private javax.swing.JPanel m_jContEntries;

    private javax.swing.JButton m_jDelete;

    private javax.swing.JButton m_jDown;

    private javax.swing.JButton m_jEditLine;

    private javax.swing.JButton m_jEnter;

    private javax.swing.JTextField m_jKeyFactory;

    private javax.swing.JLabel m_jLblTotalEuros1;

    private javax.swing.JLabel m_jLblTotalEuros2;

    private javax.swing.JLabel m_jLblTotalEuros3;

    private javax.swing.JButton m_jList;

    private com.openbravo.beans.JNumberKeys m_jNumberKeys;

    private javax.swing.JPanel m_jOptions;

    private javax.swing.JPanel m_jPanContainer;

    private javax.swing.JPanel m_jPanEntries;

    private javax.swing.JPanel m_jPanTicket;

    private javax.swing.JPanel m_jPanTotals;

    private javax.swing.JPanel m_jPanelBag;

    private javax.swing.JPanel m_jPanelCentral;

    private javax.swing.JPanel m_jPanelScripts;

    private javax.swing.JLabel m_jPor;

    private javax.swing.JLabel m_jPrice;

    private javax.swing.JLabel m_jSubtotalEuros;

    private javax.swing.JComboBox m_jTax;

    private javax.swing.JLabel m_jTaxesEuros;

    private javax.swing.JLabel m_jTicketId;

    private javax.swing.JLabel m_jTotalEuros;

    private javax.swing.JButton m_jUp;

    private javax.swing.JToggleButton m_jaddtax;

    private javax.swing.JButton m_jbtnScale;
    // End of variables declaration//GEN-END:variables

}
