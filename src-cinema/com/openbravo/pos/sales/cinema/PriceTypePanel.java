package com.openbravo.pos.sales.cinema;

import com.openbravo.pos.sales.cinema.listener.PriceTypeAl;
import com.openbravo.pos.sales.cinema.model.PriceType;

import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 */
public class PriceTypePanel extends JPanel {

    /**
     */
    private static final long serialVersionUID = -3062315528364624670L;

    /**
     */
    private final CinemaReservationMap panel;

    /**
     */
    private final JToggleButton[] buttons;

    /**
     */
    private final ButtonGroup group;

    /**
     * @param panel
     */
    public PriceTypePanel(final CinemaReservationMap panel) {
        super();

        this.panel = panel;

        final Insets insets = new Insets(6, 37, 6, 37);

        // DOUBLE_BILL

        final PriceTypeAl doubleBillListener =
            new PriceTypeAl(this.panel, PriceType.DOUBLE_BILL);

        final JToggleButton doubleBillButton = new JToggleButton("Double Bill");
        doubleBillButton.addActionListener(doubleBillListener);
        doubleBillButton.setMargin(insets);

        // GOLD

        final PriceTypeAl goldListener =
            new PriceTypeAl(this.panel, PriceType.GOLD);

        final JToggleButton goldButton = new JToggleButton("Gold");
        goldButton.addActionListener(goldListener);
        goldButton.setMargin(insets);

        // KINO_FRIENDS

        final PriceTypeAl kinoFriendsListener =
            new PriceTypeAl(this.panel, PriceType.KINO_FRIENDS);

        final JToggleButton kinoFriendsButton = new JToggleButton("Friend");
        kinoFriendsButton.addActionListener(kinoFriendsListener);
        kinoFriendsButton.setMargin(insets);

        // KINO_STAFF

        final PriceTypeAl kinoStaffListener =
            new PriceTypeAl(this.panel, PriceType.KINO_STAFF);

        final JToggleButton kinoStaffButton = new JToggleButton("Staff");
        kinoStaffButton.addActionListener(kinoStaffListener);
        kinoStaffButton.setMargin(insets);

        // SENIOR

        final PriceTypeAl seniorListener =
            new PriceTypeAl(this.panel, PriceType.SENIOR);

        final JToggleButton seniorButton = new JToggleButton("Senior");
        seniorButton.addActionListener(seniorListener);
        seniorButton.setMargin(insets);

        // SILVER

        final PriceTypeAl silverListener =
            new PriceTypeAl(this.panel, PriceType.SILVER);

        final JToggleButton silverButton = new JToggleButton("Silver");
        silverButton.addActionListener(silverListener);
        silverButton.setMargin(insets);

        // STUDENT

        final PriceTypeAl studentListener =
            new PriceTypeAl(this.panel, PriceType.STUDENT);

        final JToggleButton studentButton = new JToggleButton("Student");
        studentButton.addActionListener(studentListener);
        studentButton.setMargin(insets);

        // U16

        final PriceTypeAl u16Listener =
            new PriceTypeAl(this.panel, PriceType.U16);

        final JToggleButton u16Button = new JToggleButton("U16");
        u16Button.addActionListener(u16Listener);
        u16Button.setMargin(insets);

        this.buttons =
            new JToggleButton[] {
                goldButton, silverButton, seniorButton, u16Button,
                studentButton, kinoFriendsButton, kinoStaffButton,
                doubleBillButton
            };

        this.group = new ButtonGroup();

        // this.setSize(700, 100);

        for (final JToggleButton button : this.buttons) {
            this.group.add(button);
            this.add(button);
        }
    }

    /**
     * @param priceType
     */
    public void selectPriceType(final PriceType priceType) {
        switch (priceType) {
            case DOUBLE_BILL:
                this.buttons[7].doClick();
                break;
            case FIRST_FILM:
                this.group.clearSelection();
                break;
            case FULL_PRICE:
                this.group.clearSelection();
                break;
            case GOLD:
                this.buttons[0].doClick();
                break;
            case KINO_FRIENDS:
                this.buttons[5].doClick();
                break;
            case KINO_STAFF:
                this.buttons[6].doClick();
                break;
            case SENIOR:
                this.buttons[2].doClick();
                break;
            case SILVER:
                this.buttons[1].doClick();
                break;
            case STUDENT:
                this.buttons[4].doClick();
                break;
            case U16:
                this.buttons[3].doClick();
                break;
        }
    }
}
