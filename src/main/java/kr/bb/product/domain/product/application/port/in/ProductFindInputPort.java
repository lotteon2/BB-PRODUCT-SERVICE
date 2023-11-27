package kr.bb.product.domain.product.application.port.in;

import java.util.List;
import kr.bb.product.domain.product.application.port.out.ProductOutPort;
import kr.bb.product.domain.product.application.usecase.ProductFindUseCase;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand.ProductDetail;
import kr.bb.product.domain.product.entity.ProductCommand.ProductList;
import kr.bb.product.domain.product.entity.ProductCommand.ProductListItem;
import kr.bb.product.infrastructure.client.StoreServiceClient;
import kr.bb.product.infrastructure.client.WishlistServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductFindInputPort implements ProductFindUseCase {
  private final ProductOutPort productOutPort;
  private final WishlistServiceClient wishlistServiceClient;
  private final StoreServiceClient storeServiceClient;

  private static List<ProductListItem> getProduct(Page<Product> byCategory) {
    return ProductList.fromEntity(byCategory.getContent());
  }

  private Page<Product> getProducts(Long categoryId, Pageable pageable) {
    return productOutPort.findByCategory(categoryId, pageable);
  }

  /**
   * product category list 조회 비 로그인
   *
   * @param categoryId
   * @param pageable
   * @return
   */
  @Override
  public ProductList getProductsByCategory(Long categoryId, Pageable pageable) {
    Page<Product> byCategory = getProducts(categoryId, pageable);
    List<ProductListItem> productByCategories = getProduct(byCategory);
    return ProductList.getData(productByCategories, byCategory.getTotalPages());
  }

  /**
   * 태그별 상품 리스트 조회
   *
   * @param tagId
   * @param pageable
   * @return
   */
  @Override
  public ProductList getProductsByTag(Long tagId, Pageable pageable) {
    Page<Product> productsByTagId = productOutPort.findProductsByTagId(tagId, pageable);
    List<ProductListItem> productByCategories = getProduct(productsByTagId);
    return ProductList.getData(productByCategories, productsByTagId.getTotalPages());
  }

  /**
   * 태그별 상품 리스트 조회 - 로그인
   *
   * @param tagId
   * @param pageable
   * @return
   */
  @Override
  public ProductList getProductsByTag(Long userId, Long tagId, Pageable pageable) {
    Page<Product> products = getProducts(tagId, pageable);
    List<ProductListItem> product = getProduct(products);
    List<ProductListItem> data =
        wishlistServiceClient.getProductsMemberLikes(userId, product).getData();
    return ProductList.getData(data, products.getTotalPages());
  }

  /**
   * 상품 상세 조회
   *
   * @param userId
   * @param productId
   * @return
   */
  @Override
  public ProductDetail getProductDetail(Long userId, String productId) {
    Product byProductId = productOutPort.findByProductId(productId);
    ProductDetail productDetail = ProductDetail.fromEntity(byProductId);
    return storeServiceClient.getStoreNameOfProductDetail(productId, productDetail).getData();
  }

  /**
   * product category list 조회 로그인
   *
   * @param userId
   * @param categoryId
   * @param pageable
   * @return
   */
  @Override
  public ProductList getProductsByCategory(Long userId, Long categoryId, Pageable pageable) {
    Page<Product> byCategory = getProducts(categoryId, pageable);
    List<ProductListItem> productByCategories = getProduct(byCategory);

    List<ProductListItem> data =
        wishlistServiceClient.getProductsMemberLikes(userId, productByCategories).getData();
    return ProductList.getData(data, byCategory.getTotalPages());
  }
}
