package de.cyberporttest.core.util;

import de.cyberporttest.core.model.CustomerFeedbackReviewModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractCustomerFeedbackReviewTest extends ServicelayerTransactionalTest {

    @Resource
    ModelService modelService;
    @Resource
    private ProductService productService;
    @Resource
    private UserService userService;
    @Resource
    private CartService cartService;
    @Resource
    private TypeService typeService;
    @Resource
    private OrderService orderService;

    public abstract void setUp() throws Exception;

    protected Collection<CustomerFeedbackReviewModel> createCustomerFeedbackReviews(final CustomerModel user, final int number) throws Exception
    {
        final Collection<CustomerFeedbackReviewModel> ret = new ArrayList<CustomerFeedbackReviewModel>();
        for (int i = 0; i < number; i++)
        {
            CustomerFeedbackReviewModel review = createCustomerFeedbackReview(user, productService.getProductForCode("testProduct" + i), i);
            ret.add(review);
        }
        return ret;
    }

    protected CustomerFeedbackReviewModel createCustomerFeedbackReview(CustomerModel customer, ProductModel product, Integer index) throws Exception{
        final CustomerFeedbackReviewModel review = modelService.create(CustomerFeedbackReviewModel.class);
        review.setCustomer(customer);
        review.setReviewTitle("testReviewTitle_" + index);

        review.setReview("testReview_"+index);
        review.setRate((index%5)+1);

        review.setProduct(product);

        modelService.save(review);
        return review;
    }

    protected OrderModel placeOrderWith(CustomerModel customer, ProductModel product) throws InvalidCartException {

        userService.setCurrentUser(customer);
        final CartModel cart = cartService.getSessionCart();

        final AddressModel deliveryAddress = modelService.create(AddressModel.class);
        deliveryAddress.setOwner(customer);
        deliveryAddress.setFirstname(customer.getName());
        deliveryAddress.setLastname("Toker");
        deliveryAddress.setTown("Muenchen");

        final DebitPaymentInfoModel paymentInfo = modelService.create(DebitPaymentInfoModel.class);
        paymentInfo.setOwner(cart);
        paymentInfo.setBank("MeineBank");
        paymentInfo.setUser(customer);
        paymentInfo.setAccountNumber("34434");
        paymentInfo.setBankIDNumber("1111112");
        paymentInfo.setBaOwner("Ich");

        final PaymentModeModel paymentMode = modelService.create(PaymentModeModel.class);
        paymentMode.setActive(Boolean.TRUE);
        paymentMode.setCode("myTestPaymentMode");
        paymentMode.setName("my payment mode");
        paymentMode.setPaymentInfoType(typeService.getComposedTypeForCode(DebitPaymentInfoModel._TYPECODE));
        modelService.save(paymentMode);

        cartService.addToCart(cart, product, 1, null);
        cart.setPaymentMode(paymentMode);
        return orderService.placeOrder(cart, deliveryAddress, null, paymentInfo);
    }

    protected SearchPageData getSearchPageData(int currentPage, int pageSize, Boolean needTotal, String sort, String sortDirection) {

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
}
