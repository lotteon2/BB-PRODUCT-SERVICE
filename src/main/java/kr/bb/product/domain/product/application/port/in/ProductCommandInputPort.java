package kr.bb.product.domain.product.application.port.in;

import java.util.List;
import kr.bb.product.common.dto.NewOrderEvent;
import kr.bb.product.common.dto.ReviewRegisterEvent;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.category.repository.jpa.CategoryRepository;
import kr.bb.product.domain.flower.mapper.FlowerCommand.ProductFlowers;
import kr.bb.product.domain.flower.mapper.FlowerCommand.ProductFlowersRequestData;
import kr.bb.product.domain.product.application.port.out.ProductCommandOutPort;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.application.usecase.ProductCommandUseCase;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.infrastructure.message.ProductSQSPublisher;
import kr.bb.product.domain.product.mapper.ProductCommand;
import kr.bb.product.domain.product.mapper.ProductCommand.SubscriptionProduct;
import kr.bb.product.domain.product.mapper.ProductCommand.UpdateSubscriptionProduct;
import kr.bb.product.domain.product.mapper.mapper.ProductMapper;
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
public class ProductCommandInputPort implements ProductCommandUseCase {
  private final ProductMapper productMapper;
  private final TagRepository tagRepository;
  private final CategoryRepository categoryRepository;

  private final ProductCommandOutPort productCommandOutPort;
  private final ProductQueryOutPort productQueryOutPort;

  private final ProductSQSPublisher publishMessageToSQS;

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
  @Transactional
  public void updateProductSaleStatus(
      String productId, ProductCommand.ProductUpdate productRequestData) {
    Product product = productQueryOutPort.findByProductId(productId);
    if (productRequestData.getProductSaleStatus().equals(ProductSaleStatus.DELETED)) {
      productCommandOutPort.updateProductSaleStatus(product);
    } else if (productRequestData.getProductSaleStatus().equals(ProductSaleStatus.SALE)) {
      // sqs 재입고 알림 조회 요청
      publishMessageToSQS.publishProductResaleNotificationCheckQueue(
          productId, product.getProductName());
      productCommandOutPort.updateProductSaleStatus(product, productRequestData.getProductSaleStatus());
    } else {
      productCommandOutPort.updateProductSaleStatus(product, productRequestData.getProductSaleStatus());
    }
  }

  /**
   * 상품 등록
   *
   * @param productRequestData
   */
  @Override
  @Transactional
  public void createProduct(ProductCommand.ProductRegister productRequestData) {
    Category category = getCategory(productRequestData);
    List<Tag> tags = getTags(productRequestData);
    ProductFlowersRequestData representativeFlower = productRequestData.getRepresentativeFlower();
    List<ProductFlowers> flowers = getFlowers(productRequestData, representativeFlower);

    productCommandOutPort.createProduct(
        productMapper.createProductRequestToEntity(productRequestData, category, tags, flowers));
  }

  /**
   * 구독 상품 등록
   *
   * @param storeId
   * @param product
   */
  @Override
  @Transactional
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
  @Transactional
  public void updateSubscriptionProduct(String storeId, UpdateSubscriptionProduct product) {
    product.setProductId(storeId);
    productCommandOutPort.updateSubscriptionProduct(product);
  }

  /**
   * 리뷰 작성 시 상품 리뷰 정보 수정
   *
   * @param reviewRegisterEvent
   */
  @Override
  public void updateProductReviewData(ReviewRegisterEvent reviewRegisterEvent) {
    productCommandOutPort.updateProductReviewData(reviewRegisterEvent);
  }

  @Override
  public void saleCountUpdate(List<NewOrderEvent.ProductCount> newOrderEvent) {
    productCommandOutPort.updateProductSaleCount(newOrderEvent);
  }
}
