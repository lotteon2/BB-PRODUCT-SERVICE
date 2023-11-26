package kr.bb.product.domain.product.entity.mapper;

import java.util.List;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.domain.tag.entity.Tag;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "spring",
    imports = {ProductSaleStatus.class})
public interface ProductMapper {
  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

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
      ProductCommand.ProductRegister productRequestData,
      Category category,
      List<Tag> tag,
      List<ProductFlowers> flowers);

  @Named("FR2FL")
  @Mapping(target = "isRepresentative", ignore = true)
  ProductFlowers flowerRequestToFlowers(ProductFlowersRequestData flowersRequestData);

  @IterableMapping(qualifiedByName = "FR2FL")
  List<ProductFlowers> flowerRequestToFlowersList(
      List<ProductFlowersRequestData> flowersRequestData);

  @Named("CATEGORY")
  @Mappings({
    @Mapping(target = "isLiked", ignore = true),
    @Mapping(target = "salesCount", source = "product.productSaleAmount"),
    @Mapping(target = "productId", source = "product.productId"),
    @Mapping(target = "productName", source = "product.productName"),
    @Mapping(target = "productSummary", source = "product.productSummary"),
    @Mapping(target = "productPrice", source = "product.productPrice"),
    @Mapping(target = "reviewCount", source = "product.reviewCount"),
  })
  ProductCommand.ProductByCategory entityToProductByCategory(Product product);

  @IterableMapping(qualifiedByName = "CATEGORY")
  List<ProductCommand.ProductByCategory> entityToProductsByCategory(List<Product> products);
}
