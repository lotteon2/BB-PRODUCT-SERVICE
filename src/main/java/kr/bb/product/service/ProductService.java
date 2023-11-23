package kr.bb.product.service;

import java.util.List;
import kr.bb.product.dto.request.ProductRequestData;
import kr.bb.product.entity.Category;
import kr.bb.product.entity.Tag;
import kr.bb.product.errors.CategoryNotFoundException;
import kr.bb.product.mapper.FlowerMapper;
import kr.bb.product.mapper.ProductMapper;
import kr.bb.product.repository.jpa.CategoryRepository;
import kr.bb.product.repository.jpa.TagRepository;
import kr.bb.product.repository.mongo.ProductMongoRepository;
import kr.bb.product.vo.Flowers;
import kr.bb.product.vo.FlowersRequestData;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductMongoRepository productMongoRepository;
  private final CategoryRepository categoryRepository;
  private final TagRepository tagRepository;

  private final ProductMapper productMapper;
  private final FlowerMapper flowerMapper;

  public void createProduct(ProductRequestData productRequestData) {
    Category category = getCategory(productRequestData);
    List<Tag> tags = getTags(productRequestData);
    FlowersRequestData representativeFlower = productRequestData.getRepresentativeFlower();
    List<Flowers> flowers = getFlowers(productRequestData, representativeFlower);

    productMongoRepository.save(
            productMapper.entityToData(productRequestData, category, tags, flowers));
  }

  @NotNull
  private List<Flowers> getFlowers(
      ProductRequestData productRequestData, FlowersRequestData representativeFlower) {
    List<FlowersRequestData> flowersRequestData = productRequestData.getFlowers();
    List<Flowers> flowers = flowerMapper.flowerRequestToFlowersList(flowersRequestData);
    flowers.add(
        Flowers.builder()
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
