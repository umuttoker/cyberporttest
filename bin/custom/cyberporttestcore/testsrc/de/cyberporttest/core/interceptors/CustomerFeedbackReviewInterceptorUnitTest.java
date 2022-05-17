package de.cyberporttest.core.interceptors;

import de.cyberporttest.core.daos.CustomerFeedbackReviewDAO;
import de.cyberporttest.core.daos.CustomerPurchaseVerifierDAO;
import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.cyberporttest.core.util.AbstractCustomerFeedbackReviewTest;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Resource;

import static de.cyberporttest.core.interceptors.CustomerFeedbackReviewInterceptor.MAX_ALLOWED_REVIEW_COUNT_CONFIG_KEY;
import static de.cyberporttest.core.interceptors.CustomerFeedbackReviewInterceptor.MAX_ALLOWED_REVIEW_COUNT_REACHED;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomerFeedbackReviewInterceptorUnitTest{

    @Mock
    private ModelService modelService;
    @Mock
    CustomerFeedbackReviewDAO<CustomerFeedbackReviewModel> customerFeedbackReviewDao;
    @Mock
    CustomerPurchaseVerifierDAO customerPurchaseVerifierDAO;
    @Mock
    private L10NService l10nService;

    @Mock
    CustomerFeedbackReviewModel customerFeedbackReviewWithVerifiedPruchase;
    @Mock
    CustomerFeedbackReviewModel customerFeedbackReviewWithUnVerifiedPruchase;

    @InjectMocks
    CustomerFeedbackReviewInterceptor customerFeedbackReviewInterceptor;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);

        when(modelService.isNew(customerFeedbackReviewWithVerifiedPruchase)).thenReturn(true);
        when(l10nService.getLocalizedString(MAX_ALLOWED_REVIEW_COUNT_REACHED)).thenReturn("Customer has already reached max allowed review count!");

        when(customerFeedbackReviewWithVerifiedPruchase.getCustomer()).thenReturn(mock(CustomerModel.class));
        when(customerFeedbackReviewWithVerifiedPruchase.getProduct()).thenReturn(getProductForPurchasedFlag(true));

        when(customerFeedbackReviewWithUnVerifiedPruchase.getCustomer()).thenReturn(mock(CustomerModel.class));
        when(customerFeedbackReviewWithUnVerifiedPruchase.getProduct()).thenReturn(getProductForPurchasedFlag(false));

        when(customerPurchaseVerifierDAO.isCustomerPurchasedTheProduct(customerFeedbackReviewWithVerifiedPruchase.getProduct()
                , customerFeedbackReviewWithVerifiedPruchase.getCustomer())).thenReturn(true);

        when(customerPurchaseVerifierDAO.isCustomerPurchasedTheProduct(customerFeedbackReviewWithUnVerifiedPruchase.getProduct()
                , customerFeedbackReviewWithUnVerifiedPruchase.getCustomer())).thenReturn(false);
    }

    private ProductModel getProductForPurchasedFlag(boolean isPurchased) {
        ProductModel productModel = new ProductModel();
        productModel.setCode("testProduct" + isPurchased);
        return productModel;
    }

    @Test
    public void testMaxAllowedReviewCount(){
        Config.setParameter(MAX_ALLOWED_REVIEW_COUNT_CONFIG_KEY, "2");
        when(customerFeedbackReviewDao.getTotalReviewCountOfCustomer(any(CustomerModel.class))).thenReturn(3L);

        assertThatThrownBy(() -> customerFeedbackReviewInterceptor.onValidate(customerFeedbackReviewWithVerifiedPruchase, mock(InterceptorContext.class)))
                .isInstanceOf(InterceptorException.class)
                .hasMessageContaining("Customer has already reached max allowed review");
    }

    @Test
    public void testOnPrepareWithVerifiedPurchase() throws InterceptorException {
        customerFeedbackReviewInterceptor.onPrepare(customerFeedbackReviewWithVerifiedPruchase, mock(InterceptorContext.class));
        assertTrue(customerFeedbackReviewWithVerifiedPruchase.getVerifiedPurchase());
    }

    @Test
    public void testOnPrepareWithUnVerifiedPurchase() throws InterceptorException {
        customerFeedbackReviewInterceptor.onPrepare(customerFeedbackReviewWithUnVerifiedPruchase, mock(InterceptorContext.class));
        assertFalse(customerFeedbackReviewWithUnVerifiedPruchase.getVerifiedPurchase());
    }

}
