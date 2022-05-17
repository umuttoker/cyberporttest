package de.cyberporttest.core.daos.impl;

import de.cyberporttest.core.daos.CustomerPurchaseVerifierDAO;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

public class DefaultCustomerPurchaseVerifierDAO implements CustomerPurchaseVerifierDAO {

    static final private String PURCHASED_ORDER_QUERY = "Select {" + OrderModel.PK + "} From {" + OrderModel._TYPECODE
            +" AS o Join " + OrderEntryModel._TYPECODE + " As oe on {o.pk}={oe.order}} "
            +"Where {o.user}=?customer AND {oe.product}=?product";

    @Resource
    FlexibleSearchService flexibleSearchService;

    @Override
    public boolean isCustomerPurchasedTheProduct(ProductModel product, CustomerModel customer) {
        validateParameterNotNullStandardMessage("product", product);
        validateParameterNotNullStandardMessage("customer", customer);

        Map<String, ItemModel> params = Map.of("product", product,
                "customer", customer);

        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(PURCHASED_ORDER_QUERY,params);
        flexibleSearchQuery.setCount(1);

        SearchResult<OrderModel> searchResult = getFlexibleSearchService().search(flexibleSearchQuery);

        return CollectionUtils.isNotEmpty(searchResult.getResult());
    }

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}
