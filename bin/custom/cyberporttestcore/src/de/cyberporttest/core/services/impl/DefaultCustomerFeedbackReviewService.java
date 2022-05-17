package de.cyberporttest.core.services.impl;

import de.cyberporttest.core.daos.CustomerFeedbackReviewDAO;
import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.cyberporttest.core.services.CustomerFeedbackReviewService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import javax.annotation.Resource;
import java.util.Map;

public class DefaultCustomerFeedbackReviewService implements CustomerFeedbackReviewService {

    @Resource
    CustomerFeedbackReviewDAO<CustomerFeedbackReviewModel> customerFeedbackReviewDao;

    @Override
    public SearchPageData<CustomerFeedbackReviewModel> getReviewsOfCustomer(CustomerModel customer, SearchPageData searchPageData) {
        return this.getCustomerFeedbackReviewDao().find(Map.of("customer", customer), searchPageData);
    }

    public CustomerFeedbackReviewDAO<CustomerFeedbackReviewModel> getCustomerFeedbackReviewDao() {
        return customerFeedbackReviewDao;
    }

    public void setCustomerFeedbackReviewDao(CustomerFeedbackReviewDAO<CustomerFeedbackReviewModel> customerFeedbackReviewDao) {
        this.customerFeedbackReviewDao = customerFeedbackReviewDao;
    }
}
