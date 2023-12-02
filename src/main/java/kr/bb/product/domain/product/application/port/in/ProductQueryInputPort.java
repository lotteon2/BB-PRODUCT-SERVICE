package kr.bb.product.domain.product.application.port.in;

import bloomingblooms.errors.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import kr.bb.product.domain.flower.adapter.out.jpa.FlowerJpaRepository;
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
import kr.bb.product.domain.product.entity.ProductCommand.ProductsGroupByCategory;
import kr.bb.product.domain.product.entity.ProductCommand.SortOption;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProduct;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductDetail;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductList;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.infrastructure.client.StoreServiceClient;
import kr.bb.product.domain.product.infrastructure.client.WishlistServiceClient;
import kr.bb.product.domain.product.vo.ProductFlowers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryInputPort implements ProductQueryUseCase {
  private final ProductOutPort productOutPort;
  private final WishlistServiceClient wishlistServiceClient;
  private final StoreServiceClient storeServiceClient;
  private final FlowerJpaRepository flowerJpaRepository;

  private final ProductQueryOutPort productQueryOutPort;
  private final FlowerQueryOutPort flowerQueryOutPort;

  @NotNull
  private static Pageable getPageable(Pageable pageable, ProductCommand.SortOption sortOption) {
    Direction direction = Direction.DESC;
    if (SortOption.LOW.equals(sortOption)) direction = Direction.ASC;
    return PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        Sort.by(direction, sortOption.getSortOption()));
  }

  private static List<ProductListItem> getProduct(Page<Product> byCategory) {
    return ProductList.fromEntity(byCategory.getContent());
  }

  private static ProductDetail getProductDetail(Product byProductId) {
    return ProductDetail.fromEntity(byProductId);
  }

  private Page<Product> getProductsByCategoryId(Long categoryId, Long storeId, Pageable pageable) {
    return productQueryOutPort.findProductsByCategory(categoryId, storeId, pageable);
  }

  private String getProductDetailStoreName(Product byProductId) {
    return storeServiceClient
        .getStoreNameOfProductDetail(byProductId.getStoreId())
        .getData()
        .getStoreName();
  }

  private Flower getFlowerById(Long flowerId) {
    return flowerJpaRepository.findById(flowerId).orElseThrow(EntityNotFoundException::new);
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
    List<Flower> productDetailFlower =
        flowerQueryOutPort.findProductDetailFlower(
            Product.getFlowerIds(storeProductByStoreIdAndProductId));
    return StoreProductDetail.fromEntity(storeProductByStoreIdAndProductId, productDetailFlower);
  }

  /**
   * 가게 사장 상품
   *
   * @param storeId
   * @return
   */
  @Override
  public StoreProductList getStoreProducts(
      Long storeId,
      Long categoryId,
      Long flowerId,
      ProductSaleStatus saleStatus,
      Pageable pageable) {
    Page<Product> productByStoreId =
        productQueryOutPort.findStoreProducts(storeId, categoryId, flowerId, saleStatus, pageable);

    return StoreProductList.builder()
        .products(
            productByStoreId.getContent().stream()
                .map(
                    product -> {
                      String flowerName =
                          product.getProductFlowers().stream()
                              .filter(ProductFlowers::getIsRepresentative)
                              .findFirst()
                              .map(flowers -> getFlowerById(flowers.getFlowerId()).getFlowerName())
                              .orElse("대표꽃이 없습니다.");
                      return StoreProduct.fromEntity(product, flowerName);
                    })
                .collect(Collectors.toList()))
        .totalCnt(productByStoreId.getTotalPages())
        .build();
  }

  /**
   * 카테고리별 상품 리스트 조회 - 로그인
   *
   * @param userId
   * @param categoryId
   * @param pageable
   * @return
   */
  @Override
  public ProductList getProductsByCategory(
      Long userId,
      Long categoryId,
      Long storeId,
      ProductCommand.SortOption sortOption,
      Pageable pageable) {
    Pageable pageRequest = getPageable(pageable, sortOption);
    Page<Product> byCategory = getProductsByCategoryId(categoryId, storeId, pageRequest);
    List<ProductListItem> productByCategories = getProduct(byCategory);
    List<String> ids = ProductCommand.ProductListItem.getProductIds(productByCategories);
    List<String> data = wishlistServiceClient.getProductsMemberLikes(userId, ids).getData();
    return ProductList.getData(productByCategories, data, byCategory.getTotalPages());
  }

  /**
   * 카테고리별 상품 리스트 조회 - 비로그인
   *
   * @param categoryId
   * @param storeId
   * @param sortOption
   * @param pageable
   * @return
   */
  @Override
  public ProductList getProductsByCategory(
      Long categoryId, Long storeId, SortOption sortOption, Pageable pageable) {
    Pageable pageRequest = getPageable(pageable, sortOption);
    Page<Product> byCategory = getProductsByCategoryId(categoryId, storeId, pageRequest);
    List<ProductListItem> productByCategories = getProduct(byCategory);
    return ProductList.getData(productByCategories, byCategory.getTotalPages());
  }

  /**
   * 태그별 상품 리스트 조회 - 로그인 group by category
   *
   * @param userId
   * @param categoryId
   * @param tagId
   * @param sortOption
   * @param pageable
   * @return
   */
  @Override
  public ProductCommand.ProductsGroupByCategory getProductsByTag(
      Long userId, Long tagId, Long categoryId, SortOption sortOption, Pageable pageable) {
    return getProductsGroupByCategory(userId, tagId, categoryId, sortOption, pageable);
  }

  // 태그 조회 카테고리별 묶음
  @NotNull
  private ProductsGroupByCategory getProductsGroupByCategory(
      Long userId, Long tagId, Long categoryId, SortOption sortOption, Pageable pageable) {
    ProductsGroupByCategory productsGroupByCategory = ProductsGroupByCategory.builder().build();
    for (int i = 0; i < 5; i++) {
      ProductList productWithLikes =
          getProductListByTagId(userId, categoryId, tagId, sortOption, pageable);
      productsGroupByCategory.setProducts(categoryId, productWithLikes);
      categoryId++;
    }
    return productsGroupByCategory;
  }

  // 태그별 조회 응답 생성 - 찜 포함
  private ProductList getProductListByTagId(
      Long userId, Long categoryId, Long tagId, SortOption sortOption, Pageable pageable) {
    Pageable pageRequest = getPageable(pageable, sortOption);
    Page<Product> productsByTag =
        productQueryOutPort.findProductsByTag(tagId, categoryId, pageRequest);
    List<ProductListItem> product = getProduct(productsByTag);

    if (userId == null) return ProductList.getData(product, productsByTag.getTotalPages());

    List<String> ids = ProductListItem.getProductIds(product);
    List<String> data = wishlistServiceClient.getProductsMemberLikes(userId, ids).getData();
    return ProductList.getData(product, data, productsByTag.getTotalPages());
  }

  /**
   * 태그별 상품 리스트 조회 - 비로그인
   *
   * @param tagId
   * @param categoryId
   * @param sortOption
   * @param pageable
   * @return
   */
  @Override
  public ProductCommand.ProductsGroupByCategory getProductsByTag(
      Long tagId, Long categoryId, SortOption sortOption, Pageable pageable) {
    return getProductsGroupByCategory(null, tagId, categoryId, sortOption, pageable);
  }
}
