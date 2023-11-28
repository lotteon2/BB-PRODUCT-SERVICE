package kr.bb.product.domain.product.adapter.out.mongo;

import java.util.Optional;
import kr.bb.product.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductMongoRepository extends MongoRepository<Product, String> {
  @Query("{ '_id' : ?0 }")
  Optional<Product> findByProductId(String productId);

  @Query(
      "{ 'category.categoryId' : ?0, 'product_sale_status' : 'SALE', 'is_subscription' : false }")
  Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

  @Query("{ 'tag.tagId' : ?0, 'product_sale_status' : 'SALE', 'is_subscription' : false}")
  Page<Product> findProductsByTagId(Long tagId, Pageable pageable);

  @Query("{'store_id':  ?0}")
  Page<Product> findProductByStoreId(Long storeId, Pageable pageable);
}
