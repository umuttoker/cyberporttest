package de.cyberporttest.core.daos.impl;

import com.hybris.yprofile.dto.Order;
import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.couponservices.model.CouponRedemptionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCustomerPurchaseVerifierDAOUnitTest {

    @Mock
    FlexibleSearchService flexibleSearchService;

    @InjectMocks
    private DefaultCustomerPurchaseVerifierDAO customerPurchaseVerifierDAO = new DefaultCustomerPurchaseVerifierDAO();

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullProductTestIsCustomerPruchasedTheProduct()
    {
        customerPurchaseVerifierDAO.isCustomerPurchasedTheProduct(null, mock(CustomerModel.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullCustomerTestIsCustomerPruchasedTheProduct()
    {
        customerPurchaseVerifierDAO.isCustomerPurchasedTheProduct(mock(ProductModel.class), null);
    }

    @Test
    public void verifyPurchasedProduct(){
        boolean verificationWithResult = getVerificationWithResult(Arrays.asList(mock(OrderModel.class)));

        assertTrue(verificationWithResult);
    }

    @Test
    public void verifyNotPurchasedProduct(){
        boolean verificationWithResult = getVerificationWithResult(new ArrayList<>());

        assertFalse(verificationWithResult);
    }

    protected boolean getVerificationWithResult(List<OrderModel> resultList){
        final SearchResult<OrderModel> searchResult = mock(SearchResult.class);

        when(flexibleSearchService.<OrderModel> search(any(FlexibleSearchQuery.class))).thenReturn(searchResult);
        when(searchResult.getResult()).thenReturn(resultList);

        return customerPurchaseVerifierDAO.isCustomerPurchasedTheProduct(mock(ProductModel.class), mock(CustomerModel.class));
    }


}
