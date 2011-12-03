package com.openbravo.pos.sales.cinema;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppViewConnection;
import com.openbravo.pos.sales.cinema.model.Day;
import com.openbravo.pos.sales.cinema.model.Event;
import com.openbravo.pos.sales.cinema.model.PriceMatrix;
import com.openbravo.pos.sales.cinema.model.Venue;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 */
public class PriceTest {

    /**
     */
    private static CinemaDaoImpl dao;

    /**
     * @throws BasicException
     */
    @BeforeClass
    public static void setUpClass() throws BasicException {
        final AppConfig config = new AppConfig(new String[] {});
        config.load();

        final Session session = AppViewConnection.createSession(config);

        dao = new CinemaDaoImpl();
        dao.init(session);
    }

    /**
     * @throws BasicException
     */
    @SuppressWarnings("static-method")
    @Test
    public void testIsFirstFilmApplicable() throws BasicException {
        final Venue venue = new Venue(26L);

        final Event event14 = dao.getEvent(14L);
        final Event event15 = dao.getEvent(15L);
        final Event event16 = dao.getEvent(16L);
        final Event event17 = dao.getEvent(17L);

        boolean isFirstFilmApplicable =
            dao.isFirstFilmApplicable(venue, event14);
        Assert.assertFalse(isFirstFilmApplicable);

        isFirstFilmApplicable = dao.isFirstFilmApplicable(venue, event15);
        Assert.assertTrue(isFirstFilmApplicable);

        isFirstFilmApplicable = dao.isFirstFilmApplicable(venue, event16);
        Assert.assertFalse(isFirstFilmApplicable);

        isFirstFilmApplicable = dao.isFirstFilmApplicable(venue, event17);
        Assert.assertFalse(isFirstFilmApplicable);
    }

    /**
     * @throws BasicException
     */
    @SuppressWarnings("static-method")
    @Test
    public void testList() throws BasicException {
        final List<PriceMatrix> prices = dao.listPrice(new Venue(26L));
        Assert.assertEquals(20, prices.size());
    }

    /**
     * @throws BasicException
     */
    @SuppressWarnings("static-method")
    @Test
    public void testListByDate() throws BasicException {
        @SuppressWarnings("deprecation")
        final List<PriceMatrix> prices =
            dao.listPrice(new Venue(26L), Day.TUESDAY, new Date(0, 0, 0, 8, 15,
                0));
        Assert.assertEquals(2, prices.size());
    }
}
