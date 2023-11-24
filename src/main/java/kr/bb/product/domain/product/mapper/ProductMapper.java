package kr.bb.product.domain.product.mapper;

import java.util.List;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.product.api.request.ProductRequestData;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.tag.entity.Tag;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    imports = {ProductSaleStatus.class})
public interface ProductMapper {
  @Mappings({
    @Mapping(target = "productId", ignore = true),
    @Mapping(target = "productSaleStatus", expression = "java(ProductSaleStatus.SALE)"),
    @Mapping(target = "tag", source = "tag"),
    @Mapping(target = "category", source = "category"),
    @Mapping(target = "productFlowers", source = "flowers"),
    @Mapping(target = "reviewCount", ignore = true),
    @Mapping(target = "averageRating", ignore = true),
    @Mapping(target = "productSaleAmount", ignore = true),
    @Mapping(target = "createdAt", ignore = true),
    @Mapping(target = "updatedAt", ignore = true),
    @Mapping(target = "isDeleted", ignore = true)
  })
  Product createProductRequestToEntity(
      ProductRequestData productRequestData,
      Category category,
      List<Tag> tag,
      List<ProductFlowers> flowers);

  @Named("FR2FL")
  @Mapping(target = "isRepresentative", ignore = true)
  ProductFlowers flowerRequestToFlowers(ProductFlowersRequestData flowersRequestData);

  @IterableMapping(qualifiedByName = "FR2FL")
  List<ProductFlowers> flowerRequestToFlowersList(
      List<ProductFlowersRequestData> flowersRequestData);
}
