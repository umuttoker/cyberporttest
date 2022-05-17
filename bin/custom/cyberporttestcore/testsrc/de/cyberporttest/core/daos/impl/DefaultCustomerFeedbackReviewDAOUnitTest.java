package de.cyberporttest.core.daos.impl;

import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCustomerFeedbackReviewDAOUnitTest {

    @Mock
    PaginatedFlexibleSearchService paginatedFlexibleSearchService;

    @InjectMocks
    private DefaultCustomerFeedbackReviewDAO<CustomerFeedbackReviewModel> customerFeedbackReviewDAO = new DefaultCustomerFeedbackReviewDAO<>(CustomerFeedbackReviewModel._TYPECODE);

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTestGetTotalReviewCountOfCustomer()
    {
       customerFeedbackReviewDAO.getTotalReviewCountOfCustomer(null);
    }


}
