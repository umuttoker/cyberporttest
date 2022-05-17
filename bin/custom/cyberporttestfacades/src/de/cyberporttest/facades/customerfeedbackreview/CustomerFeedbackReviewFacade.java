package de.cyberporttest.facades.customerfeedbackreview;

import de.cyberporttest.facades.customerfeedbackreview.data.CustomerFeedbackReviewData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

public interface CustomerFeedbackReviewFacade {

    <T extends CustomerFeedbackReviewData> SearchPageData<T> getReviewsOfCustomer(String customerUid, SearchPageData searchPageData);
}
