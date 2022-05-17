package de.cyberporttest.core.interceptors;

import de.cyberporttest.core.daos.CustomerFeedbackReviewDAO;
import de.cyberporttest.core.daos.CustomerPurchaseVerifierDAO;
import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

public class CustomerFeedbackReviewInterceptor implements ValidateInterceptor<CustomerFeedbackReviewModel>, PrepareInterceptor<CustomerFeedbackReviewModel> {

    public static final String MAX_ALLOWED_REVIEW_COUNT_CONFIG_KEY = "max.allowed.customerfeedbackreview.count";
    public static final String MAX_ALLOWED_REVIEW_COUNT_REACHED = "max.allowed.customerfeedbackreview.reached";

    @Resource
    private ModelService modelService;

    @Resource
    CustomerFeedbackReviewDAO<CustomerFeedbackReviewModel> customerFeedbackReviewDao;

    @Resource
    CustomerPurchaseVerifierDAO customerPurchaseVerifierDAO;

    @Resource
    private L10NService l10nService;

    @Override
    public void onValidate(CustomerFeedbackReviewModel customerFeedbackReviewModel, InterceptorContext interceptorContext) throws InterceptorException {

        if(getModelService().isNew(customerFeedbackReviewModel)){
            CustomerModel customer = customerFeedbackReviewModel.getCustomer();
            long totalReviewCountOfCustomer = getCustomerFeedbackReviewDao().getTotalReviewCountOfCustomer(customer);

            final Integer maxAllowedReviewCount = Config.getInt(MAX_ALLOWED_REVIEW_COUNT_CONFIG_KEY, 5000);

            if(totalReviewCountOfCustomer >= maxAllowedReviewCount){
                throw new InterceptorException(getL10nService().getLocalizedString(MAX_ALLOWED_REVIEW_COUNT_REACHED));
            }
        }
    }

    @Override
    public void onPrepare(CustomerFeedbackReviewModel customerFeedbackReviewModel, InterceptorContext interceptorContext) throws InterceptorException {

        if(getModelService().isNew(customerFeedbackReviewModel)){

            CustomerModel customer = customerFeedbackReviewModel.getCustomer();
            ProductModel product = customerFeedbackReviewModel.getProduct();

            validateParameterNotNullStandardMessage("customer", customer);
            validateParameterNotNullStandardMessage("product", product);

            boolean customerPurchasedTheProduct = getCustomerPurchaseVerifierDAO().isCustomerPurchasedTheProduct(product, customer);

            customerFeedbackReviewModel.setVerifiedPurchase(customerPurchasedTheProduct);
        }
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }


    public CustomerFeedbackReviewDAO<CustomerFeedbackReviewModel> getCustomerFeedbackReviewDao() {
        return customerFeedbackReviewDao;
    }

    public void setCustomerFeedbackReviewDao(CustomerFeedbackReviewDAO<CustomerFeedbackReviewModel> customerFeedbackReviewDao) {
        this.customerFeedbackReviewDao = customerFeedbackReviewDao;
    }

    public L10NService getL10nService() {
        return l10nService;
    }

    public void setL10nService(L10NService l10nService) {
        this.l10nService = l10nService;
    }

    public CustomerPurchaseVerifierDAO getCustomerPurchaseVerifierDAO() {
        return customerPurchaseVerifierDAO;
    }

    public void setCustomerPurchaseVerifierDAO(CustomerPurchaseVerifierDAO customerPurchaseVerifierDAO) {
        this.customerPurchaseVerifierDAO = customerPurchaseVerifierDAO;
    }
}
