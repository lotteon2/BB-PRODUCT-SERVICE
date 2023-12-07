package kr.bb.product.domain.product.application.usecase;

import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.ProductList;
import kr.bb.product.domain.product.entity.ProductCommand.SortOption;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductList;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import org.springframework.data.domain.Pageable;

public interface ProductQueryUseCase {

  ProductCommand.ProductDetail getProductDetail(Long userId, String productId);

  ProductCommand.ProductDetail getProductDetail(String productId);

  ProductCommand.StoreProductDetail getStoreProductDetail(Long storeId, String productId);

  StoreProductList getStoreProducts(
      Long storeId,
      Long categoryId,
      Long flowerId,
      ProductSaleStatus saleStatus,
      Pageable pageable);

  ProductList getProductsByCategory(
      Long userId,
      Long categoryId,
      Long storeId,
      ProductCommand.SortOption sortOption,
      Pageable pageable);

  ProductList getProductsByCategory(
      Long categoryId, Long storeId, ProductCommand.SortOption sortOption, Pageable pageable);

  ProductCommand.ProductsGroupByCategory getProductsByTag(
      Long userId, Long tagId, Long categoryId, SortOption sortOption, Pageable pageable);

  ProductCommand.ProductsGroupByCategory getProductsByTag(
      Long tagId, Long categoryId, ProductCommand.SortOption sortOption, Pageable pageable);

  ProductCommand.BestSellerTopTen getBestSellerTopTen(Long storeId);

  ProductCommand.StoreManagerSubscriptionProduct getSubscriptionProductByStoreId(Long storeId);
}
