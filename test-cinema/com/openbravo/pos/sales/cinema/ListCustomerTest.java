package com.openbravo.pos.sales.cinema;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppViewConnection;
import com.openbravo.pos.sales.cinema.model.Customer;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 */
public class ListCustomerTest {

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
    public void test() throws BasicException {
        final List<Customer> customers = dao.listCustomer();
        Assert.assertEquals(2, customers.size());
    }
}
