package de.cyberporttest.occaddon.controllers;

import de.cyberporttest.facades.customerfeedbackreview.CustomerFeedbackReviewFacade;
import de.cyberporttest.facades.customerfeedbackreview.data.CustomerFeedbackReviewData;
import de.cyberporttest.occaddon.customerfeedbackreview.dto.CustomerFeedbackReviewSearchPageWsDTO;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.ycommercewebservices.v2.controller.BaseCommerceController;
import io.swagger.annotations.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Arrays;

import static de.cyberporttest.occaddon.constants.CyberporttestoccaddonWebConstants.*;

@Controller
@Api(tags = "Customer Feedback Review Controller")
@RequestMapping(value = "/{baseSiteId}/users/{userId}/customerfeedbackreviews")
public class CustomerFeedbackReviewController extends BaseCommerceController {

    @Resource
    CustomerFeedbackReviewFacade customerFeedbackReviewFacade;

    @Secured({"ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
    @GetMapping
    @ResponseBody
    @ApiOperation(nickname = "GetCustomerFeedbackReviews", value = "Get Customer Feedback Reviews", notes = "Get the paged response of given customer's feedback reviews.")
    @ApiBaseSiteIdParam
    public CustomerFeedbackReviewSearchPageWsDTO getFeedbackReviewsOfCustomer(
            @ApiParam(value = "User Id", required = true) @PathVariable final String userId,
            @ApiParam(value = "Current page", required = false) @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
            @ApiParam(value = "Page size", required = false) @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
            @ApiParam(value = "Need Total", required = false) @RequestParam(required = false, defaultValue = DEFAULT_TOTAL_NEED_FLAG) final Boolean needTotal,
            @ApiParam(value = "Sort parameter. Possible values: creationtime, rate", required = false) @RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort,
            @ApiParam(value = "Sort direction. Possible values: asc, desc", required = false) @RequestParam(required = false, defaultValue = DEFAULT_SORT_DIRECTION) final String sortDirection){

        SearchPageData searchPageData = getSearchPageData(currentPage, pageSize, needTotal, sort, sortDirection);

        SearchPageData<CustomerFeedbackReviewData> reviewsOfCustomer = getCustomerFeedbackReviewFacade().getReviewsOfCustomer(userId, searchPageData);

        return getDataMapper().map(reviewsOfCustomer, CustomerFeedbackReviewSearchPageWsDTO.class);
    }

    private SearchPageData getSearchPageData(int currentPage, int pageSize, Boolean needTotal, String sort, String sortDirection) {

        SortData sortData = new SortData();
        sortData.setCode(sort);
        sortData.setAsc("asc".equalsIgnoreCase(sortDirection));

        PaginationData paginationData = new PaginationData();
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        paginationData.setNeedsTotal(needTotal);

        SearchPageData<Object> searchPageData = new SearchPageData<>();
        searchPageData.setSorts(Arrays.asList(sortData));
        searchPageData.setPagination(paginationData);
        return searchPageData;
    }

    public CustomerFeedbackReviewFacade getCustomerFeedbackReviewFacade() {
        return customerFeedbackReviewFacade;
    }

    public void setCustomerFeedbackReviewFacade(CustomerFeedbackReviewFacade customerFeedbackReviewFacade) {
        this.customerFeedbackReviewFacade = customerFeedbackReviewFacade;
    }
}
