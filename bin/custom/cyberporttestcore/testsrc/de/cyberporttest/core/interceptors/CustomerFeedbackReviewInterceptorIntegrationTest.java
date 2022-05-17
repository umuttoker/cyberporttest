package de.cyberporttest.core.interceptors;

import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.cyberporttest.core.util.AbstractCustomerFeedbackReviewTest;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@IntegrationTest
public class CustomerFeedbackReviewInterceptorIntegrationTest extends AbstractCustomerFeedbackReviewTest {

    @Resource
    private UserService userService;

    @Resource
    private ProductService productService;

    @Resource
    ModelService modelService;

    private CustomerModel customer1;
    private CustomerModel customer2;

    private ProductModel product1;
    private ProductModel product2;

    @Before
    public void setUp() throws Exception{
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

    @Test
    public void testMaxAllowedReviewCount(){
        String defaultValue = Config.getParameter(CustomerFeedbackReviewInterceptor.MAX_ALLOWED_REVIEW_COUNT_CONFIG_KEY);
        try
        {
            Config.setParameter(CustomerFeedbackReviewInterceptor.MAX_ALLOWED_REVIEW_COUNT_CONFIG_KEY, "2");
            createCustomerFeedbackReviews(customer1, 3);
            fail("expected ModelSavingException");

        }
        catch (final Exception e)
        {
            Config.setParameter(CustomerFeedbackReviewInterceptor.MAX_ALLOWED_REVIEW_COUNT_CONFIG_KEY, defaultValue);
            assertTrue(e instanceof ModelSavingException);
            assertTrue(e.getCause() instanceof InterceptorException);
        }

    }

    @Test
    public void testVerifiedPurchase() throws Exception {
        placeOrderWith(customer2, product1);
        CustomerFeedbackReviewModel customerFeedbackReview = createCustomerFeedbackReview(customer2, product1, 0);
        modelService.refresh(customerFeedbackReview);
        assertTrue(customerFeedbackReview.getVerifiedPurchase());
    }

    @Test
    public void testUnVerifiedPurchase() throws Exception {
        CustomerFeedbackReviewModel customerFeedbackReview = createCustomerFeedbackReview(customer2, product2, 0);
        modelService.refresh(customerFeedbackReview);
        assertFalse(customerFeedbackReview.getVerifiedPurchase());
    }



}
