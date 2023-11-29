package kr.bb.product.domain.product.adapter.out.mongo;

import java.util.List;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
  public List<Product> findStoreProducts(
      Long storeId,
      Long categoryId,
      Long flowerId,
      ProductSaleStatus saleStatus,
      Pageable pageable) {
    Query query = new Query();
    if (categoryId != null) query.addCriteria(Criteria.where("category.categoryId").is(categoryId));
    if (flowerId != null)
      query.addCriteria(Criteria.where("product_flowers.flowerId").is(flowerId));
    if (saleStatus != null) query.addCriteria(Criteria.where("product_sale_status").is(saleStatus));
    query.with(pageable);
    return mongoTemplate.find(query, Product.class);
  }
}
