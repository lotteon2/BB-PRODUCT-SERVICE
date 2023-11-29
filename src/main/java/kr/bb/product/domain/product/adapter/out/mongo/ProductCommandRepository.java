package kr.bb.product.domain.product.adapter.out.mongo;

import kr.bb.product.domain.product.application.port.out.ProductCommandOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand.UpdateSubscriptionProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductCommandRepository implements ProductCommandOutPort {
  private final ProductMongoRepository productMongoRepository;
  private final MongoTemplate mongoTemplate;

  @Override
  public void createProduct(Product productRequestToEntity) {
    productMongoRepository.save(productRequestToEntity);
  }

  @Override
  public void updateSubscriptionProduct(UpdateSubscriptionProduct product) {
    mongoTemplate.updateMulti(
        Query.query(
            Criteria.where("store_id").is(product.getStoreId()).and("is_subscription").is(true)),
        Update.update("product_name", product.getProductName())
            .set("product_summary", product.getProductSummary())
            .set("product_price", product.getProductPrice())
            .set("product_description_image", product.getProductDescriptionImage())
            .set("product_thumbnail", product.getProductThumbnail()),
        Product.class);
  }
}
