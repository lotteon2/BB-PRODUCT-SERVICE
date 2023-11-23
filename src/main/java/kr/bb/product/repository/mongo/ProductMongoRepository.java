package kr.bb.product.repository.mongo;

import kr.bb.product.entity.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductMongoRepository extends MongoRepository<Product, String> {}
