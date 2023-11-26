package kr.bb.product.domain.product.application.port.in;

import java.util.List;
import kr.bb.product.domain.product.application.ProductFindUseCase;
import kr.bb.product.domain.product.application.port.out.ProductOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.ProductByCategory;
import kr.bb.product.domain.product.entity.ProductCommand.ProductsByCategory;
import kr.bb.product.infrastructure.client.WishlistServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductFindInputPort implements ProductFindUseCase {
  private final ProductOutPort productOutPort;
  private final WishlistServiceClient wishlistServiceClient;

  private static List<ProductByCategory> getProduct(Page<Product> byCategory) {
    return ProductsByCategory.fromEntity(byCategory.getContent());
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
  public ProductsByCategory getProductsByCategory(Long categoryId, Pageable pageable) {
    Page<Product> byCategory = getProducts(categoryId, pageable);
    List<ProductByCategory> productByCategories = getProduct(byCategory);
    return ProductCommand.ProductsByCategory.getData(
        productByCategories, byCategory.getTotalPages());
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
  public ProductsByCategory getProductsByCategory(Long userId, Long categoryId, Pageable pageable) {
    Page<Product> byCategory = getProducts(categoryId, pageable);
    List<ProductByCategory> productByCategories = getProduct(byCategory);

    // isliked를 알아야 함 - 찜 feign
    List<ProductByCategory> data =
        wishlistServiceClient.getProductsMemberLikes(userId, productByCategories).getData();
    return ProductCommand.ProductsByCategory.getData(data, byCategory.getTotalPages());
  }
}
