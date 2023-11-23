package kr.bb.product.mapper;

import java.util.List;
import kr.bb.product.dto.request.ProductRequestData;
import kr.bb.product.entity.ProductSaleStatus;
import kr.bb.product.entity.Category;
import kr.bb.product.vo.Flowers;
import kr.bb.product.entity.Product;
import kr.bb.product.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

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
  Product entityToData(
      ProductRequestData productRequestData,
      Category category,
      List<Tag> tag,
      List<Flowers> flowers);
}
