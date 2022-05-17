package de.cyberporttest.core.daos.impl;

import de.cyberporttest.core.daos.CustomerFeedbackReviewDAO;

import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.dao.impl.DefaultPaginatedGenericDao;
import de.hybris.platform.servicelayer.search.paginated.util.PaginatedSearchUtils;
import org.springframework.util.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

public class DefaultCustomerFeedbackReviewDAO<T extends CustomerFeedbackReviewModel> extends DefaultPaginatedGenericDao<T> implements CustomerFeedbackReviewDAO<T> {

    private final String typeCode;

    public DefaultCustomerFeedbackReviewDAO(String typeCode) {
        super(typeCode);
        this.typeCode = typeCode;
    }

    @Override
    public long getTotalReviewCountOfCustomer(CustomerModel customer) {
        validateParameterNotNullStandardMessage("customer", customer);

        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT {pk} ");
        sql.append("FROM {").append(this.typeCode).append("} ");
        sql.append("WHERE {").append(CustomerFeedbackReviewModel.CUSTOMER).append("}=?customer ");

        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(sql.toString());
        flexibleSearchQuery.addQueryParameter("customer", customer);

        SearchPageData searchPageDataWithPagination = PaginatedSearchUtils.createSearchPageDataWithPagination(1, 0, true);

        PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
        parameter.setFlexibleSearchQuery(flexibleSearchQuery);
        parameter.setSearchPageData(searchPageDataWithPagination);
        parameter.setSortCodeToQueryAlias(this.getSortCodeToQueryAlias(searchPageDataWithPagination));

        final SearchPageData<CustomerFeedbackReviewModel> searchResult =  getPaginatedFlexibleSearchService().search(parameter);

        return searchResult.getPagination().getTotalNumberOfResults();
    }
}
