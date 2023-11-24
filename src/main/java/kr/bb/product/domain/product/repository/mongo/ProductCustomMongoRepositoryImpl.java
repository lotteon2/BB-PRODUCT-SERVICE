package kr.bb.product.domain.product.repository.mongo;

import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class ProductCustomMongoRepositoryImpl implements ProductCustomMongoRepository {
  private final MongoTemplate mongoTemplate;

  @Override
  public void updateProductSaleStatus(Product product, ProductSaleStatus productSaleStatus) {
    mongoTemplate.updateFirst(
        Query.query(Criteria.where("_id").is(product.getProductId())),
        Update.update("product_sale_status", productSaleStatus),
        Product.class);
  }

  @Override
  public void updateProductSaleStatus(Product product) {
    mongoTemplate.updateFirst(
        Query.query(Criteria.where("_id").is(product.getProductId())),
        Update.update("is_deleted", true)
            .set("product_sale_status", ProductSaleStatus.DISCONTINUED),
        Product.class);
  }
}
