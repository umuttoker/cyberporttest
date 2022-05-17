package de.cyberporttest.facades.customerfeedbackreview.impl;

import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.cyberporttest.core.services.CustomerFeedbackReviewService;
import de.cyberporttest.core.util.AbstractCustomerFeedbackReviewTest;
import de.cyberporttest.facades.customerfeedbackreview.CustomerFeedbackReviewFacade;
import de.cyberporttest.facades.customerfeedbackreview.data.CustomerFeedbackReviewData;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCustomerFeedbackReviewFacadeUnitTest extends AbstractCustomerFeedbackReviewTest {

    @InjectMocks
    CustomerFeedbackReviewFacade customerFeedbackReviewFacade;

    @Mock
    CustomerFeedbackReviewService customerFeedbackReviewService;
    @Mock
    UserService userService;
    @Mock
    Converter<CustomerFeedbackReviewModel, CustomerFeedbackReviewData> customerFeedbackReviewConverter;

    @Mock
    CustomerModel sampleUser;
    @Mock
    EmployeeModel sampleEmployee;

    private static final String customerUid = "sampleCustomer";
    private static final String employeeUid = "sampleEmployee";
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sampleUser.setUid(customerUid);
        sampleEmployee.setUid(employeeUid);

        when(userService.getUserForUID(customerUid)).thenReturn(sampleUser);
        when(userService.getUserForUID(employeeUid)).thenReturn(sampleEmployee);

        final SearchPageData<CustomerFeedbackReviewModel> searchPageModelData = mock(SearchPageData.class);
        when(customerFeedbackReviewService.getReviewsOfCustomer(sampleUser, any(SearchPageData.class))).thenReturn(searchPageModelData);

        final List<CustomerFeedbackReviewData> convertedCustomerReview = Arrays.asList(mock(CustomerFeedbackReviewData.class));
        when(customerFeedbackReviewConverter.convertAll(searchPageModelData.getResults())).thenReturn(convertedCustomerReview);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullGetReviewsOfCustomer() {
        customerFeedbackReviewFacade.getReviewsOfCustomer(null,null);
    }

    @Test(expected = UnknownIdentifierException.class)
    public void testWithEmployeeGetReviewsOfCustomer() {
        customerFeedbackReviewFacade.getReviewsOfCustomer(employeeUid,new SearchPageData<>());
    }

    @Test
    public void testWithCustomerGetReviewsOfCustomer() {
        SearchPageData<CustomerFeedbackReviewData> reviewsOfCustomer = customerFeedbackReviewFacade.getReviewsOfCustomer(customerUid, new SearchPageData<>());
        assertTrue(!reviewsOfCustomer.getResults().isEmpty());
    }

}
