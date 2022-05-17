package de.cyberporttest.facades.customerfeedbackreview.impl;

import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.cyberporttest.facades.customerfeedbackreview.CustomerFeedbackReviewFacade;
import de.cyberporttest.facades.customerfeedbackreview.data.CustomerFeedbackReviewData;
import de.cyberporttest.core.services.CustomerFeedbackReviewService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

public class DefaultCustomerFeedbackReviewFacade implements CustomerFeedbackReviewFacade {

    @Resource
    CustomerFeedbackReviewService customerFeedbackReviewService;

    @Resource
    UserService userService;

    @Resource
    Converter<CustomerFeedbackReviewModel, CustomerFeedbackReviewData> customerFeedbackReviewConverter;

    @Override
    public <T extends CustomerFeedbackReviewData> SearchPageData<T> getReviewsOfCustomer(String customerUid, SearchPageData searchPageData) {
        validateParameterNotNullStandardMessage("customerUid", customerUid);

        UserModel userByUid = getUserService().getUserForUID(customerUid);
        if(!(userByUid instanceof CustomerModel))
            throw new UnknownIdentifierException("Cannot find customer with uid '" + customerUid + "'");

        CustomerModel customerByUid = (CustomerModel)userByUid;

        SearchPageData<CustomerFeedbackReviewModel> reviewsOfCustomer = getCustomerFeedbackReviewService().getReviewsOfCustomer(customerByUid, searchPageData);

        final SearchPageData<T> customerFeedbackReviewSearchPageData = new SearchPageData<>();

        customerFeedbackReviewSearchPageData.setPagination(reviewsOfCustomer.getPagination());
        customerFeedbackReviewSearchPageData.setSorts(reviewsOfCustomer.getSorts());
        customerFeedbackReviewSearchPageData.setResults((List<T>) getCustomerFeedbackReviewConverter().convertAll(reviewsOfCustomer.getResults()));

        return customerFeedbackReviewSearchPageData;
    }

    public CustomerFeedbackReviewService getCustomerFeedbackReviewService() {
        return customerFeedbackReviewService;
    }

    public void setCustomerFeedbackReviewService(CustomerFeedbackReviewService customerFeedbackReviewService) {
        this.customerFeedbackReviewService = customerFeedbackReviewService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Converter<CustomerFeedbackReviewModel, CustomerFeedbackReviewData> getCustomerFeedbackReviewConverter() {
        return customerFeedbackReviewConverter;
    }

    public void setCustomerFeedbackReviewConverter(Converter<CustomerFeedbackReviewModel, CustomerFeedbackReviewData> customerFeedbackReviewConverter) {
        this.customerFeedbackReviewConverter = customerFeedbackReviewConverter;
    }
}
