package com.openbravo.pos.sales.cinema;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.RepaintManager;

// http://www.roseindia.net/java/example/java/swing/Print.shtml

@SuppressWarnings("all")
public class PrintableDocument implements Printable {

    private final Component compent;

    public static void printComponent(final Component c) {
        new PrintableDocument(c).print();
    }

    public PrintableDocument(final Component compent) {
        this.compent = compent;
    }

    public void print() {
        final PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (final PrinterException pe) {
                System.out.println("Error printing: " + pe);
            }
        }
    }

    @Override
    public int print(final Graphics g, final PageFormat pageFormat,
    final int pageIndex) {
        if (pageIndex > 0) {
            return (NO_SUCH_PAGE);
        }

        final Graphics2D graph = (Graphics2D) g;
        graph.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        disableBuffering(this.compent);
        this.compent.paint(graph);
        enableBuffering(this.compent);

        return (PAGE_EXISTS);
    }

    public static void disableBuffering(final Component c) {
        final RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    public static void enableBuffering(final Component c) {
        final RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }
}
