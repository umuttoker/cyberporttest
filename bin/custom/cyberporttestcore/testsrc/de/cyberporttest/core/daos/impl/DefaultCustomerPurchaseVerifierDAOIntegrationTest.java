package de.cyberporttest.core.daos.impl;

import de.cyberporttest.core.daos.CustomerPurchaseVerifierDAO;
import de.cyberporttest.core.util.AbstractCustomerFeedbackReviewTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.daos.OrderDao;
import de.hybris.platform.order.daos.OrderDaoTest;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import java.util.Collection;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class DefaultCustomerPurchaseVerifierDAOIntegrationTest extends AbstractCustomerFeedbackReviewTest {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultCustomerPurchaseVerifierDAOIntegrationTest.class);

    @Resource
    private CustomerPurchaseVerifierDAO customerPurchaseVerifierDAO;
    @Resource
    private ProductService productService;
    @Resource
    private UserService userService;

    private CustomerModel customer1;
    private CustomerModel customer2;

    private ProductModel product1;
    private ProductModel product2;

    @Before
    public void setUp() throws Exception
    {
        createCoreData();
        createDefaultUsers();
        createDefaultCatalog();

        UserModel user1 = userService.getUserForUID("ariel");
        assertNotNull("User", user1);
        assertTrue(user1 instanceof CustomerModel);
        customer1 = (CustomerModel)user1;

        UserModel user2 = userService.getUserForUID("hpneumann");
        assertNotNull("User", user2);
        assertTrue(user2 instanceof CustomerModel);
        customer2 = (CustomerModel)user2;

        product1 = productService.getProduct("testProduct0");
        assertNotNull("Product", product1);

        product2 = productService.getProduct("testProduct1");
        assertNotNull("Product", product2);
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testFindOrdersByProductPurchased() throws InvalidCartException
    {
        placeOrderWith(customer1, product1);
        assertTrue(customerPurchaseVerifierDAO.isCustomerPurchasedTheProduct(product1, customer1));
    }

    @Test
    public void testFindOrdersByProductNotPurchased() throws InvalidCartException
    {
        assertFalse(customerPurchaseVerifierDAO.isCustomerPurchasedTheProduct(product1, customer2));
    }
}
