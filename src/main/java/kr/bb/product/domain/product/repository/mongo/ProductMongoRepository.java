package kr.bb.product.domain.product.repository.mongo;

import java.util.Optional;
import kr.bb.product.domain.product.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductMongoRepository
    extends MongoRepository<Product, String>, ProductCustomMongoRepository {
  @Query("{ '_id' : ?0 }")
  Optional<Product> findByProductId(String productId);
}
