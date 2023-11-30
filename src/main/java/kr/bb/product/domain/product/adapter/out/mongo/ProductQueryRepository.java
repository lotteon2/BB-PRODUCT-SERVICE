package kr.bb.product.domain.product.adapter.out.mongo;

import java.util.List;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
