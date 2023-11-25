package kr.bb.product.domain.product.adapter.out.mongo;

import java.util.List;
import java.util.Optional;
import kr.bb.product.domain.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductMongoRepository extends MongoRepository<Product, String> {
  @Query("{ '_id' : ?0 }")
  Optional<Product> findByProductId(String productId);

  @Query("{'category.categoryId':  ?0}")
  List<Product> findByCategoryId(Long categoryId, Pageable pageable);
}
