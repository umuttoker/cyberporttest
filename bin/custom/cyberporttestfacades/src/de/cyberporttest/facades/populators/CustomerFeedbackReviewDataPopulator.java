package de.cyberporttest.facades.populators;

import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.cyberporttest.facades.customerfeedbackreview.data.CustomerFeedbackReviewData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.spockframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CustomerFeedbackReviewDataPopulator implements Populator<CustomerFeedbackReviewModel, CustomerFeedbackReviewData> {

    private static final List<ProductOption> OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.URL, ProductOption.IMAGES);

    @Resource
    private Converter<LanguageModel, LanguageData> languageConverter;

    @Resource
    private Converter<CustomerModel, CustomerData> customerConverter;

    @Resource
    private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productVariantConfiguredPopulator;

    @Override
    public void populate(CustomerFeedbackReviewModel source, CustomerFeedbackReviewData target) throws ConversionException {
        Assert.notNull(source, "Source must not be null.");
        Assert.notNull(source, "Target must not be null.");

        target.setReviewTitle(source.getReviewTitle());
        target.setReview(source.getReview());
        target.setRate(source.getRate());
        target.setCustomer(getCustomerConverter().convert(source.getCustomer()));

        ProductData productData = new ProductData();
        getProductVariantConfiguredPopulator().populate(source.getProduct(), productData, OPTIONS);
        target.setProduct(productData);

        if(Objects.nonNull(source.getLanguage())){
            target.setLanguage(getLanguageConverter().convert(source.getLanguage()));
        }


    }

    public Converter<LanguageModel, LanguageData> getLanguageConverter() {
        return languageConverter;
    }

    public void setLanguageConverter(Converter<LanguageModel, LanguageData> languageConverter) {
        this.languageConverter = languageConverter;
    }

    public Converter<CustomerModel, CustomerData> getCustomerConverter() {
        return customerConverter;
    }

    public void setCustomerConverter(Converter<CustomerModel, CustomerData> customerConverter) {
        this.customerConverter = customerConverter;
    }

    public ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductVariantConfiguredPopulator() {
        return productVariantConfiguredPopulator;
    }

    public void setProductVariantConfiguredPopulator(ConfigurablePopulator<ProductModel, ProductData, ProductOption> productVariantConfiguredPopulator) {
        this.productVariantConfiguredPopulator = productVariantConfiguredPopulator;
    }
}
