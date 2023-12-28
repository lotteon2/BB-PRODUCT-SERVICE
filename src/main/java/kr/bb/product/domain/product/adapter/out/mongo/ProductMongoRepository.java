package kr.bb.product.domain.product.adapter.out.mongo;

import java.util.List;
import java.util.Optional;
import kr.bb.product.domain.product.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductMongoRepository extends MongoRepository<Product, String> {
  @Query("{ '_id' : ?0 }")
  Optional<Product> findByProductId(String productId);

  @Query("{'store_id':  ?0}")
  List<Product> findProductByStoreId(Long storeId);

  @Query("{'store_id':  ?0, '_id':  ?1}")
  Product findProductByStoreIdAndProductId(Long storeId, String productId);

  @Query("{'store_id':  ?0, 'is_subscription':  true}")
  Product findSubscriptionProductByStoreId(Long storeId);
}
