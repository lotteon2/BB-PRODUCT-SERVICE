package kr.bb.product.service;

import kr.bb.product.dto.request.ProductRequestData;
import kr.bb.product.mapper.ProductMapper;
import kr.bb.product.repository.ProductMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductMongoRepository productMongoRepository;
  private final ProductMapper productMapper;

  public void createProduct(ProductRequestData productRequestData) {
    productMongoRepository.save(productMapper.entityToData(productRequestData));
  }
}
