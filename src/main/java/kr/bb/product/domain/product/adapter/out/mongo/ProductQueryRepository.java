package kr.bb.product.domain.product.adapter.out.mongo;

import bloomingblooms.domain.product.IsProductPriceValid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.SelectOption;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.exception.errors.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository implements ProductQueryOutPort {
  private final ProductMongoRepository productMongoRepository;
  private final MongoTemplate mongoTemplate;

  @Override
  public List<Product> findProductByStoreId(Long storeId) {
    return productMongoRepository.findProductByStoreId(storeId);
  }

  @Override
  public Product findStoreProductByStoreIdAndProductId(Long storeId, String productId) {
    return productMongoRepository.findProductByStoreIdAndProductId(storeId, productId);
  }

  /**
   * 가게 사장 상품 리스트 조회 - 카테고리, 꽃, 판매 상태
   *
   * @param storeId
   * @param categoryId
   * @param flowerId
   * @param saleStatus
   * @param pageable
   * @return
   */
  @Override
  public Page<Product> findStoreProducts(
      Long storeId,
      Long categoryId,
      Long flowerId,
      ProductSaleStatus saleStatus,
      Pageable pageable) {
    Query query = new Query();
    query.addCriteria(Criteria.where("store_id").is(storeId));
    query.addCriteria(Criteria.where("is_subscription").is(false));
    if (categoryId != null) query.addCriteria(Criteria.where("category.categoryId").is(categoryId));
    if (flowerId != null)
      query.addCriteria(Criteria.where("product_flowers.flowerId").is(flowerId));
    if (saleStatus != null) query.addCriteria(Criteria.where("product_sale_status").is(saleStatus));
    query.with(pageable);
    List<Product> products = mongoTemplate.find(query, Product.class);
    return PageableExecutionUtils.getPage(
        products,
        pageable,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Product.class));
  }

  /**
   * 카테고리별 상품 리스트 조회
   *
   * @param categoryId
   * @param storeId
   * @param pageable
   * @return
   */
  @Override
  public Page<Product> findProductsByCategory(Long categoryId, Long storeId, Pageable pageable) {
    Query query = new Query();
    if (storeId != null) query.addCriteria(Criteria.where("store_id").is(storeId));
    query.addCriteria(Criteria.where("category.categoryId").is(categoryId));
    query.addCriteria(Criteria.where("product_sale_status").is(ProductSaleStatus.SALE));
    query.with(pageable);
    List<Product> products = mongoTemplate.find(query, Product.class);
    return PageableExecutionUtils.getPage(
        products,
        pageable,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Product.class));
  }

  @Override
  public Page<Product> findProductsByTag(Long tagId, Long categoryId, Pageable pageable) {
    Query query = new Query();
    query.addCriteria(Criteria.where("tag.tagId").is(tagId));
    query.addCriteria(Criteria.where("category.categoryId").is(categoryId));
    query.addCriteria(Criteria.where("product_sale_status").is(ProductSaleStatus.SALE));
    query.with(pageable);

    List<Product> products = mongoTemplate.find(query, Product.class);
    return PageableExecutionUtils.getPage(
        products,
        pageable,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Product.class));
  }

  @Override
  public Product findByProductId(String productId) {
    return productMongoRepository
        .findByProductId(productId)
        .orElseThrow(ProductNotFoundException::new);
  }

  @Override
  public List<Product> findBestSellerTopTen(Long storeId) {
    return mongoTemplate.find(
        Query.query(Criteria.where("store_id").is(storeId))
            .limit(10)
            .with(Sort.by(Sort.Order.desc("productSaleAmount"))),
        Product.class);
  }

  /**
   * 가게 사장 구독 상품 조회
   *
   * @param storeId
   * @return
   */
  @Override
  public Product findSubscriptionProductByStoreId(Long storeId) {
    return productMongoRepository.findSubscriptionProductByStoreId(storeId);
  }

  @Override
  public List<Product> findMainPageProducts(SelectOption selectOption) {
    Query query =
        new Query(
            Criteria.where("is_subscription").is(false).and("product_sale_status").is("SALE"));
    query.limit(4).with(Sort.by(Sort.Order.desc(selectOption.getSelectOption())));
    return mongoTemplate.find(query, Product.class);
  }

  @Override
  public List<Product> findProductByProductIds(List<String> productIds) {
    Query query = new Query(Criteria.where("_id").in(productIds));
    query.with(Sort.by(Sort.Order.desc("created_at")));

    return mongoTemplate.find(query, Product.class);
  }

  @Override
  public boolean findProductPriceValid(List<IsProductPriceValid> productPriceValids) {
    List<String> collect =
        productPriceValids.stream()
            .map(IsProductPriceValid::getProductId)
            .collect(Collectors.toList());
    List<Product> products =
        mongoTemplate.find(Query.query(Criteria.where("_id").in(collect)), Product.class);
    Map<String, Long> collect1 =
        products.stream()
            .collect(Collectors.toMap(Product::getProductId, Product::getProductPrice));
    return productPriceValids.stream()
        .allMatch(item -> item.getPrice().equals(collect1.get(item.getProductId())));
  }

  @Override
  public ProductCommand.RepresentativeFlowerId findRepresentativeFlower(String productId) {
    return ProductCommand.RepresentativeFlowerId.getData(
        mongoTemplate
            .findOne(
                Query.query(
                    Criteria.where("_id")
                        .is(productId)
                        .and("product_flowers.isRepresentative")
                        .is(true)),
                Product.class)
            .getProductFlowers()
            .stream()
            .map(ProductFlowers::getFlowerId)
            .findFirst()
            .orElse(null));
  }

  @Override
  public Map<String, String> findProductNameByProductIdsForReviewByUserId(List<String> productIds) {

    List<Product> products =
        mongoTemplate.find(Query.query(Criteria.where("_id").in(productIds)), Product.class);

    return products.stream()
        .collect(Collectors.toMap(Product::getProductId, Product::getProductName));
  }
}
