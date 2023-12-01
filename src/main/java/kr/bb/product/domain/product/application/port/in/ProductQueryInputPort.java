package kr.bb.product.domain.product.application.port.in;

import java.util.List;
import kr.bb.product.domain.flower.application.port.out.FlowerQueryOutPort;
import kr.bb.product.domain.flower.entity.Flower;
import kr.bb.product.domain.product.application.port.out.ProductOutPort;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.ProductDetail;
import kr.bb.product.domain.product.entity.ProductCommand.ProductList;
import kr.bb.product.domain.product.entity.ProductCommand.ProductListItem;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductDetail;
import kr.bb.product.domain.product.infrastructure.client.StoreServiceClient;
import kr.bb.product.domain.product.infrastructure.client.WishlistServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryInputPort implements ProductQueryUseCase {
  private final ProductOutPort productOutPort;
  private final WishlistServiceClient wishlistServiceClient;
  private final StoreServiceClient storeServiceClient;

  private final ProductQueryOutPort productQueryOutPort;
  private final FlowerQueryOutPort flowerQueryOutPort;

  private static List<ProductListItem> getProduct(Page<Product> byCategory) {
    return ProductList.fromEntity(byCategory.getContent());
  }

  private static ProductDetail getProductDetail(Product byProductId) {
    return ProductDetail.fromEntity(byProductId);
  }

  private Page<Product> getProducts(Long categoryId, Pageable pageable) {
    return productOutPort.findByCategory(categoryId, pageable);
  }

  private String getProductDetailStoreName(Product byProductId) {
    return storeServiceClient
        .getStoreNameOfProductDetail(byProductId.getStoreId())
        .getData()
        .getStoreName();
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
   * 태그별 상품 리스트 조회 - 비 로그인 시
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
   * 상품 상세 조회 - 로그인 시
   *
   * @param userId
   * @param productId
   * @return
   */
  @Override
  public ProductDetail getProductDetail(Long userId, String productId) {
    Product byProductId = productOutPort.findByProductId(productId);
    ProductDetail productDetail = getProductDetail(byProductId);
    ProductCommand.ProductDetailLike isLiked =
        wishlistServiceClient.getProductDetailLikes(productId, userId).getData();
    String storeName = getProductDetailStoreName(byProductId);
    productDetail.setLiked(isLiked.getIsLiked());
    productDetail.setStoreName(storeName);
    return productDetail;
  }

  /**
   * 상품 상세 정보 - 비 로그인 시
   *
   * @param productId
   * @return
   */
  @Override
  public ProductDetail getProductDetail(String productId) {
    Product byProductId = productOutPort.findByProductId(productId);
    ProductDetail productDetail = getProductDetail(byProductId);
    String storeName = getProductDetailStoreName(byProductId);
    productDetail.setStoreName(storeName);
    return productDetail;
  }

  /**
   * 가게 사장 상품 상세 조회
   *
   * @param storeId
   * @param productId
   * @return
   */
  @Override
  public StoreProductDetail getStoreProductDetail(Long storeId, String productId) {
    Product storeProductByStoreIdAndProductId =
        productQueryOutPort.findStoreProductByStoreIdAndProductId(storeId, productId);
    // 꽃 정보 받기
    List<Flower> productDetailFlower =
        flowerQueryOutPort.findProductDetailFlower(
            Product.getFlowerIds(storeProductByStoreIdAndProductId));
    return StoreProductDetail.fromEntity(storeProductByStoreIdAndProductId, productDetailFlower);
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
