package de.cyberporttest.core.daos;

import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.paginated.dao.PaginatedGenericDao;

public interface CustomerFeedbackReviewDAO<T extends CustomerFeedbackReviewModel> extends PaginatedGenericDao<T> {

    long getTotalReviewCountOfCustomer(CustomerModel customer);
}
