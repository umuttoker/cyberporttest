package de.cyberporttest.core.daos;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;

public interface CustomerPurchaseVerifierDAO{

    boolean isCustomerPurchasedTheProduct(ProductModel product, CustomerModel customer);
}
