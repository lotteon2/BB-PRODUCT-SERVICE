package kr.bb.product.domain.product.repository.mongo;

import kr.bb.product.domain.product.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductMongoRepository extends MongoRepository<Product, String> {}
