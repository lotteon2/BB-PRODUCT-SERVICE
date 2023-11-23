package kr.bb.product.domain.product.application;

import java.util.List;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.category.repository.jpa.CategoryRepository;
import kr.bb.product.domain.product.api.request.ProductRequestData;
import kr.bb.product.domain.product.mapper.ProductMapper;
import kr.bb.product.domain.product.repository.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.domain.tag.entity.Tag;
import kr.bb.product.domain.tag.repository.jpa.TagRepository;
import kr.bb.product.exception.errors.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
  private final ProductMongoRepository productMongoRepository;
  private final CategoryRepository categoryRepository;
  private final TagRepository tagRepository;

  private final ProductMapper productMapper;

  @Transactional
  public void createProduct(ProductRequestData productRequestData) {
    Category category = getCategory(productRequestData);
    List<Tag> tags = getTags(productRequestData);
    ProductFlowersRequestData representativeFlower = productRequestData.getRepresentativeFlower();
    List<ProductFlowers> flowers = getFlowers(productRequestData, representativeFlower);

    productMongoRepository.save(
        productMapper.entityToData(productRequestData, category, tags, flowers));
  }

  @NotNull
  private List<ProductFlowers> getFlowers(
      ProductRequestData productRequestData, ProductFlowersRequestData representativeFlower) {
    List<ProductFlowersRequestData> flowersRequestData = productRequestData.getFlowers();
    List<ProductFlowers> flowers = productMapper.flowerRequestToFlowersList(flowersRequestData);
    flowers.add(
        ProductFlowers.builder()
            .flowerId(representativeFlower.getFlowerId())
            .isRepresentative(true)
            .flowerCount(representativeFlower.getFlowerCount())
            .build());
    return flowers;
  }

  @NotNull
  private List<Tag> getTags(ProductRequestData productRequestData) {
    return tagRepository.findAllById(productRequestData.getProductTag());
  }

  private Category getCategory(ProductRequestData productRequestData) {
    return categoryRepository
        .findById(productRequestData.getCategoryId())
        .orElseThrow(CategoryNotFoundException::new);
  }
}
