package kr.bb.product.repository;

import kr.bb.product.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductMongoRepository extends MongoRepository<Product, String> {}
