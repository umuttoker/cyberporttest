package de.cyberporttest.core.daos.impl;

import de.cyberporttest.core.daos.CustomerFeedbackReviewDAO;
import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.cyberporttest.core.util.AbstractCustomerFeedbackReviewTest;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

@IntegrationTest
public class DefaultCustomerFeedbackReviewDAOIntegrationTest extends AbstractCustomerFeedbackReviewTest {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCustomerFeedbackReviewDAOIntegrationTest.class);

    @Resource
    CustomerFeedbackReviewDAO<CustomerFeedbackReviewModel> customerFeedbackReviewDao;

    @Resource
    private UserService userService;

    private CustomerModel customer1;

    private ProductModel product1;

    @Before
    public void setUp() throws Exception{
        createCoreData();
        createDefaultUsers();
        createDefaultCatalog();

        UserModel user1 = userService.getUserForUID("ariel");
        assertNotNull("User", user1);
        assertTrue(user1 instanceof CustomerModel);
        customer1 = (CustomerModel)user1;

    }

    @Test
    public void findCustomerFeedbackReviews() throws Exception {
        Collection<CustomerFeedbackReviewModel> customerFeedbackReviews = createCustomerFeedbackReviews(customer1, 2);
        assertEquals(customerFeedbackReviews.size(), 2);

        long totalReviewCountOfCustomer = customerFeedbackReviewDao.getTotalReviewCountOfCustomer(customer1);
        assertEquals(totalReviewCountOfCustomer, customerFeedbackReviews.size());
    }
}
