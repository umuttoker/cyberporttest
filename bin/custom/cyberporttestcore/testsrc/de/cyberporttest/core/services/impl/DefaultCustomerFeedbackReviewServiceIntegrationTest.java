package de.cyberporttest.core.services.impl;

import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.cyberporttest.core.services.CustomerFeedbackReviewService;
import de.cyberporttest.core.util.AbstractCustomerFeedbackReviewTest;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@IntegrationTest
public class DefaultCustomerFeedbackReviewServiceIntegrationTest extends AbstractCustomerFeedbackReviewTest {

    @Resource
    CustomerFeedbackReviewService customerFeedbackReviewService;
    @Resource
    private UserService userService;

    private CustomerModel customer1;
    private CustomerModel customer2;


    @Before
    public void setUp() throws Exception {
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
    }

    @Test
    public void testPagedDataOfReviews() throws Exception {
        createCustomerFeedbackReviews(customer1, 3);
        SearchPageData searchPageData = getSearchPageData(0, 1, true, "rate", "asc");
        SearchPageData<CustomerFeedbackReviewModel> reviewsOfCustomer = customerFeedbackReviewService.getReviewsOfCustomer(customer1, searchPageData);

        assertTrue(reviewsOfCustomer.getPagination().getTotalNumberOfResults() == 3);
        assertTrue(reviewsOfCustomer.getResults().size() == 1);
        assertTrue(reviewsOfCustomer.getResults().get(0).getRate() == 1);
        assertTrue(reviewsOfCustomer.getResults().get(0).getCustomer().equals(customer1));
    }

    @Test
    public void testEmptyResult(){
        SearchPageData searchPageData = getSearchPageData(0, 1, true, "rate", "asc");
        SearchPageData<CustomerFeedbackReviewModel> reviewsOfCustomer = customerFeedbackReviewService.getReviewsOfCustomer(customer2, searchPageData);

        assertTrue(reviewsOfCustomer.getPagination().getTotalNumberOfResults() == 0);
    }
}
