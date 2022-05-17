package de.cyberporttest.core.services;

import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;

public interface CustomerFeedbackReviewService {

    SearchPageData<CustomerFeedbackReviewModel> getReviewsOfCustomer(CustomerModel customer, SearchPageData searchPageData);
}
