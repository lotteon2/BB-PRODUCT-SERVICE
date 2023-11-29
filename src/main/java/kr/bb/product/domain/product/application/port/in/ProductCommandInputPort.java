package kr.bb.product.domain.product.application.port.in;

import java.util.List;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.category.repository.jpa.CategoryRepository;
import kr.bb.product.domain.product.application.port.out.ProductCommandOutPort;
import kr.bb.product.domain.product.application.port.out.ProductOutPort;
import kr.bb.product.domain.product.application.usecase.ProductCommandUseCased;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.SubscriptionProduct;
import kr.bb.product.domain.product.entity.ProductCommand.UpdateSubscriptionProduct;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.entity.mapper.ProductMapper;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.domain.tag.entity.Tag;
import kr.bb.product.domain.tag.repository.jpa.TagRepository;
import kr.bb.product.exception.errors.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductCommandInputPort implements ProductCommandUseCased {
  private final ProductOutPort productOutPort;
  private final ProductMapper productMapper;
  private final TagRepository tagRepository;
  private final CategoryRepository categoryRepository;
  private final ProductCommandOutPort productCommandOutPort;

  @NotNull
  private List<ProductFlowers> getFlowers(
      ProductCommand.ProductRegister productRequestData,
      ProductFlowersRequestData representativeFlower) {
    List<ProductFlowersRequestData> flowersRequestData = productRequestData.getFlowers();
    List<ProductFlowers> flowers = productMapper.flowerRequestToFlowersList(flowersRequestData);
    flowers.add(
        ProductFlowers.builder()
            .flowerId(representativeFlower.getFlowerId())
            .isRepresentative(true)
            .flowerCount(representativeFlower.getFlowerCount())
            .build());
    return flowers;
  }

  @NotNull
  private List<Tag> getTags(ProductCommand.ProductRegister productRequestData) {
    return tagRepository.findAllById(productRequestData.getProductTag());
  }

  private Category getCategory(ProductCommand.ProductRegister productRequestData) {
    return categoryRepository
        .findById(productRequestData.getCategoryId())
        .orElseThrow(CategoryNotFoundException::new);
  }

  /**
   * 상품 상태 변경
   *
   * @param productId
   * @param productRequestData
   */
  @Override
  public void updateProductSaleStatus(
      String productId, ProductCommand.ProductUpdate productRequestData) {
    Product product = productOutPort.findByProductId(productId);
    if (productRequestData.getProductSaleStatus().equals(ProductSaleStatus.DELETED)) {
      productOutPort.updateProductSaleStatus(product);
    } else {
      productOutPort.updateProductSaleStatus(product, productRequestData.getProductSaleStatus());
    }
  }

  /**
   * 상품 등록
   *
   * @param productRequestData
   */
  @Override
  public void createProduct(ProductCommand.ProductRegister productRequestData) {
    Category category = getCategory(productRequestData);
    List<Tag> tags = getTags(productRequestData);
    ProductFlowersRequestData representativeFlower = productRequestData.getRepresentativeFlower();
    List<ProductFlowers> flowers = getFlowers(productRequestData, representativeFlower);

    productOutPort.createProduct(
        productMapper.createProductRequestToEntity(productRequestData, category, tags, flowers));
  }

  /**
   * 구독 상품 등록
   *
   * @param storeId
   * @param product
   */
  @Override
  public void createSubscriptionProduct(Long storeId, SubscriptionProduct product) {
    productCommandOutPort.createProduct(SubscriptionProduct.toEntity(product, storeId));
  }

  /**
   * 구독 상품 수정
   *
   * @param storeId
   * @param product
   */
  @Override
  public void updateSubscriptionProduct(String storeId, UpdateSubscriptionProduct product) {
    product.setProductId(storeId);
    productCommandOutPort.updateSubscriptionProduct(product);
  }
}
