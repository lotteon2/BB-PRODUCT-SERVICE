package kr.bb.product.domain.product.application.port.out;

import java.util.List;
import kr.bb.product.common.dto.IsProductPriceValid;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryOutPort {
  List<Product> findProductByStoreId(Long storeId);

  Product findStoreProductByStoreIdAndProductId(Long storeId, String productId);

  Page<Product> findStoreProducts(
      Long soreId, Long categoryId, Long flowerId, ProductSaleStatus saleStatus, Pageable pageable);

  Page<Product> findProductsByCategory(Long categoryId, Long tagId, Pageable pageable);

  Page<Product> findProductsByTag(Long tagId, Long categoryId, Pageable pageable);

  Product findByProductId(String productId);

  List<Product> findBestSellerTopTen(Long storeId);

  Product findSubscriptionProductByStoreId(Long storeId);

  List<Product> findMainPageProducts(ProductCommand.SelectOption selectOption);

  List<Product> findProductByProductIds(List<String> productIds);

  boolean findProductPriceValid(List<IsProductPriceValid> productPriceValids);
}
