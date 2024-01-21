package kr.bb.product.domain.product.adapter.out.mongo;

import bloomingblooms.domain.product.IsProductPriceValid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.bb.product.domain.flower.mapper.FlowerCommand;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.mapper.ProductCommand;
import kr.bb.product.domain.product.mapper.ProductCommand.SelectOption;
import kr.bb.product.domain.product.mapper.ProductCommand.SortOption;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductQueryRepository implements ProductQueryOutPort {
  private static final String PRODUCT = "product";
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
    else query.addCriteria(Criteria.where("product_sale_status").ne(ProductSaleStatus.DELETED));
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
    query.addCriteria(Criteria.where("product_sale_status").ne(ProductSaleStatus.DELETED));
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
    if (categoryId != null) query.addCriteria(Criteria.where("category.categoryId").is(categoryId));
    query.addCriteria(Criteria.where("product_sale_status").ne(ProductSaleStatus.DELETED));
    query.with(pageable);

    List<Product> products = mongoTemplate.find(query, Product.class);
    return PageableExecutionUtils.getPage(
        products,
        pageable,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Product.class));
  }

  @Override
  public Product findByProductId(String productId) {
    return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(productId)), Product.class);
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
    return productMongoRepository
        .findSubscriptionProductByStoreId(storeId)
        .orElse(Product.builder().build());
  }

  @Override
  @Cacheable(cacheNames = PRODUCT, key = "'RECOMMEND'")
  public List<Product> findMainPageProductsRecommend() {
    Query query =
        new Query(
            Criteria.where("is_subscription").is(false).and("product_sale_status").is("SALE"));
    query.limit(4).with(Sort.by(Sort.Order.desc(SelectOption.RECOMMEND.getSelectOption())));
    return mongoTemplate.find(query, Product.class);
  }

  @Override
  @Cacheable(cacheNames = PRODUCT, key = "'NEW_ARRIVAL'")
  public List<Product> findMainPageProductsNewArrival() {
    Query query =
        new Query(
            Criteria.where("is_subscription").is(false).and("product_sale_status").is("SALE"));
    query.limit(4).with(Sort.by(Sort.Order.desc(SelectOption.NEW_ARRIVAL.getSelectOption())));
    return mongoTemplate.find(query, Product.class);
  }

  @Override
  @Cacheable(cacheNames = PRODUCT, key = "'RATING'")
  public List<Product> findMainPageProductsRating() {
    Query query =
        new Query(
            Criteria.where("is_subscription").is(false).and("product_sale_status").is("SALE"));
    query.limit(4).with(Sort.by(Sort.Order.desc(SelectOption.RATING.getSelectOption())));
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
            .map(FlowerCommand.ProductFlowers::getFlowerId)
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

  @Override
  public Map<Long, List<Product>> findProductsByProductsGroupByStoreId(List<String> productId) {
    Query query = Query.query(Criteria.where("_id").in(productId));
    List<Product> products = mongoTemplate.find(query, Product.class);
    return products.stream()
        .collect(Collectors.groupingBy(Product::getStoreId, Collectors.toList()));
  }

  @Override
  public Map<Long, Double> findStoreAverageRating() {
    AggregationOperation matchReviewsGreaterThanOne =
        Aggregation.match(Criteria.where("reviewCount").gt(1));
    AggregationOperation groupByStoreId =
        Aggregation.group("storeId").avg("averageRating").as("averageRating");
    TypedAggregation<Product> aggregation =
        Aggregation.newAggregation(Product.class, matchReviewsGreaterThanOne, groupByStoreId);

    // Execute the aggregation
    AggregationResults<AverageResult> aggregate =
        mongoTemplate.aggregate(aggregation, AverageResult.class);
    List<AverageResult> averageResults = aggregate.getMappedResults();

    // Logging for troubleshooting
    averageResults.forEach(
        result -> {
          System.out.println(
              "storeId: " + result.get_id() + ", averageRating: " + result.getAverageRating());
        });

    return averageResults.stream()
        .collect(Collectors.toMap(AverageResult::get_id, AverageResult::getAverageRating));
  }

  @Override
  public Page<Product> findProductsForAdmin(
      ProductCommand.AdminSelectOption adminSelectOption, Pageable pageable) {
    Query query = new Query();
    query.addCriteria(Criteria.where("is_subscription").is(false));
    if (adminSelectOption.getSalesAmount().equals(SortOption.TOP_SALE))
      query.with(Sort.by(Order.desc(adminSelectOption.getSalesAmount().getSortOption())));
    else query.with(Sort.by(Order.asc(adminSelectOption.getSalesAmount().getSortOption())));

    if (adminSelectOption.getStatus() != null)
      query.addCriteria(Criteria.where("product_sale_status").is(adminSelectOption.getStatus()));

    if (adminSelectOption.getDate().equals(SortOption.NEW))
      query.with(Sort.by(Order.desc(adminSelectOption.getDate().getSortOption())));
    else query.with(Sort.by(Order.asc(adminSelectOption.getDate().getSortOption())));

    query.with(pageable);

    List<Product> products = mongoTemplate.find(query, Product.class);
    return PageableExecutionUtils.getPage(
        products,
        pageable,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Product.class));
  }

  @Override
  public Page<Product> findProductsByFlowerId(Long flowerId, Pageable pageable) {
    Query query =
        Query.query(
            Criteria.where("product_flowers.flowerId")
                .is(flowerId)
                .and("product_sale_status")
                .is("SALE"));
    query.with(Sort.by(Order.desc("createdAt")));
    query.with(pageable);
    List<Product> products = mongoTemplate.find(query, Product.class);
    return PageableExecutionUtils.getPage(
        products,
        pageable,
        () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Product.class));
  }

  @Getter
  @AllArgsConstructor
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  private static class AverageResult {
    private Long _id;
    private Double averageRating;
  }
}
