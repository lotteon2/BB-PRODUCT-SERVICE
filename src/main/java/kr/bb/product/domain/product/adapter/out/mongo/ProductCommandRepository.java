package kr.bb.product.domain.product.adapter.out.mongo;

import bloomingblooms.domain.order.NewOrderEvent.ProductCount;
import bloomingblooms.domain.review.ReviewRegisterEvent;
import java.util.List;
import kr.bb.product.domain.product.application.port.out.ProductCommandOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.mapper.ProductCommand.ProductUpdate;
import kr.bb.product.domain.product.mapper.ProductCommand.UpdateSubscriptionProduct;
import kr.bb.product.domain.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductCommandRepository implements ProductCommandOutPort {
  private static final String PRODUCT = "product";
  private final ProductMongoRepository productMongoRepository;
  private final MongoTemplate mongoTemplate;

  @Override
  @CacheEvict(cacheNames = PRODUCT, key = "'NEW_ARRIVAL'")
  public void createProduct(Product productRequestToEntity) {
    productMongoRepository.save(productRequestToEntity);
  }

  @Override
  public void updateSubscriptionProduct(UpdateSubscriptionProduct product) {
    mongoTemplate.updateMulti(
        Query.query(
            Criteria.where("_id").is(product.getProductId()).and("is_subscription").is(true)),
        Update.update("product_name", product.getProductName())
            .set("product_summary", product.getProductSummary())
            .set("product_price", product.getProductPrice())
            .set("product_description_image", product.getProductDescriptionImage())
            .set("product_thumbnail", product.getProductThumbnail()),
        Product.class);
  }

  @Override
  public void updateProductReviewData(ReviewRegisterEvent reviewRegisterEvent) {
    Query query = Query.query(Criteria.where("_id").is(reviewRegisterEvent.getProductId()));
    Product product = mongoTemplate.findOne(query, Product.class);

    Double averageRating = product.getAverageRating();
    Long reviewCount = product.getReviewCount();
    Double newAverageRating =
        ((averageRating * reviewCount) + reviewRegisterEvent.getReviewRating()) / (reviewCount + 1);
    Update update = new Update().set("averageRating", newAverageRating).inc("reviewCount", 1);
    mongoTemplate.updateFirst(query, update, Product.class);
  }

  @Override
  public void updateProductSaleCount(List<ProductCount> newOrderEvent) {
    BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkMode.UNORDERED, Product.class);
    for (ProductCount products : newOrderEvent) {
      Query query = Query.query(Criteria.where("_id").is(products.getProductId()));
      Update update = new Update().inc("productSaleAmount", products.getQuantity());
      bulkOperations.updateOne(query, update);
    }
    bulkOperations.execute();
  }

  @Override
  public void updateProductSaleStatus(String productId, ProductUpdate productRequestData) {
    mongoTemplate.updateFirst(
        Query.query(Criteria.where("_id").is(productId)),
        Update.update("is_deleted", true).set("product_sale_status", ProductSaleStatus.DELETED),
        Product.class);
  }

  @Override
  public void updateProductSaleStatus(
      String productId, ProductUpdate productRequestData, List<Tag> tags) {
    mongoTemplate.updateFirst(
        Query.query(Criteria.where("_id").is(productId)),
        Update.update("product_sale_status", productRequestData.getProductSaleStatus())
            .set("tag", tags)
            .set("product_summary", productRequestData.getProductSummary())
            .set("product_price", productRequestData.getProductPrice())
            .set("product_thumbnail", productRequestData.getProductThumbnail())
            .set("product_name", productRequestData.getProductName())
            .set("product_description_image", productRequestData.getProductDescriptionImage()),
        Product.class);
  }

  @Override
  public void deleteProductByAdmin(List<String> productId) {
    mongoTemplate.remove(Query.query(Criteria.where("productId").in(productId)), Product.class);
  }
}
