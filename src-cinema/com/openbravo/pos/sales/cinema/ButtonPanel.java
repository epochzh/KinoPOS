package com.openbravo.pos.sales.cinema;

import com.openbravo.pos.sales.cinema.listener.CancelTicketAl;
import com.openbravo.pos.sales.cinema.listener.CinemaReservationMapAl;
import com.openbravo.pos.sales.cinema.listener.PriceTypeAl;
import com.openbravo.pos.sales.cinema.model.PriceType;

import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 */
public class ButtonPanel extends JPanel {

    /**
	 */
    private static final long serialVersionUID = 4455753454080990706L;

    /**
     */
    private static final Icon ICON_DELETE = new ImageIcon(ButtonPanel.class
        .getResource("/com/openbravo/images/editdelete.png"));

    /**
     */
    private static final Icon ICON_MAP = new ImageIcon(ButtonPanel.class
        .getResource("/com/openbravo/images/atlantikdesignersmall.png"));

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
    public ButtonPanel(final CinemaReservationMap panel) {
        super();

        this.panel = panel;

        final Insets insets = new Insets(8, 14, 8, 14);

        // MAP

        final CinemaReservationMapAl mapListener =
            new CinemaReservationMapAl(this.panel);

        final JButton map = new JButton();
        map.addActionListener(mapListener);
        map.setFocusPainted(false);
        map.setFocusable(false);
        map.setIcon(ICON_MAP);
        map.setMargin(new Insets(8, 14, 8, 14));
        map.setRequestFocusEnabled(false);

        final JButton delete = new JButton();
        delete.addActionListener(new CancelTicketAl(panel));
        delete.setFocusPainted(false);
        delete.setFocusable(false);
        delete.setIcon(ICON_DELETE);
        delete.setMargin(new Insets(8, 14, 8, 14));
        delete.setRequestFocusEnabled(false);

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
                studentButton, kinoFriendsButton, kinoStaffButton
            };

        this.group = new ButtonGroup();

        this.setSize(700, 100);

        this.add(map);
        this.add(delete);
        for (final JToggleButton button : this.buttons) {
            this.group.add(button);
            this.add(button);
        }
    }

    /**
     */
    public void disablePriceType() {
        for (final JToggleButton button : this.buttons) {
            button.setEnabled(false);
        }
    }

    /**
     */
    public void enablePriceType() {
        for (final JToggleButton button : this.buttons) {
            button.setEnabled(true);
        }
        this.group.clearSelection();
    }
}
