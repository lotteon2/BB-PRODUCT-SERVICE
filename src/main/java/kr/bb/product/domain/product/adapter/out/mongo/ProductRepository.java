package kr.bb.product.domain.product.adapter.out.mongo;

import kr.bb.product.domain.product.application.port.out.ProductOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepository implements ProductOutPort {
  private final MongoTemplate mongoTemplate;
  private final ProductMongoRepository productMongoRepository;

  @Override
  public Page<Product> findByCategory(Long categoryId, Pageable pageable) {
    // TODO: sort
    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
    return productMongoRepository.findByCategoryId(categoryId, pageable);
  }

  @Override
  public Page<Product> findProductsByTagId(Long tagId, Pageable pageable) {
    // TODO: sort
    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
    return productMongoRepository.findProductsByTagId(tagId, pageable);
  }

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

  @Override
  public void createProduct(Product productRequestToEntity) {
    productMongoRepository.save(productRequestToEntity);
  }
}
