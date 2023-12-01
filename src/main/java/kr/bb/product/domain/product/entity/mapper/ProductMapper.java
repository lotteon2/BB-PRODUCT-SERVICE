package kr.bb.product.domain.product.entity.mapper;

import java.util.List;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.category.entity.CategoryCommand.CategoryForProductList;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.ProductDetail;
import kr.bb.product.domain.product.entity.ProductCommand.ProductListItem;
import kr.bb.product.domain.product.entity.ProductCommand.SubscriptionProduct;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.domain.tag.entity.Tag;
import kr.bb.product.domain.tag.entity.TagCommand.TagForProductList;
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
    @Mapping(target = "isSubscription", expression = "java(false)"),
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
  @Mapping(target = "flowerName", ignore = true)
  ProductFlowers flowerRequestToFlowers(ProductFlowersRequestData flowersRequestData);

  @IterableMapping(qualifiedByName = "FR2FL")
  List<ProductFlowers> flowerRequestToFlowersList(
      List<ProductFlowersRequestData> flowersRequestData);

  @Named("CATEGORY")
  @Mappings({
    @Mapping(target = "isLiked", ignore = true),
    @Mapping(target = "salesCount", source = "product.productSaleAmount"),
    @Mapping(target = "key", source = "product.productId"),
    @Mapping(target = "productName", source = "product.productName"),
    @Mapping(target = "productSummary", source = "product.productSummary"),
    @Mapping(target = "productPrice", source = "product.productPrice"),
    @Mapping(target = "reviewCount", source = "product.reviewCount"),
    @Mapping(target = "averageRating", source = "product.averageRating"),
  })
  ProductListItem entityToListItem(Product product);

  @IterableMapping(qualifiedByName = "CATEGORY")
  List<ProductListItem> entityToList(List<Product> products);

  @Mapping(source = "product.productId", target = "productId")
  @Mapping(source = "product.productName", target = "productName")
  @Mapping(source = "product.productSummary", target = "productDescription")
  @Mapping(source = "product.productThumbnail", target = "productThumbnail")
  @Mapping(source = "product.productDescriptionImage", target = "productDetailImage")
  @Mapping(source = "product.productPrice", target = "productPrice")
  @Mapping(source = "product.productSaleStatus", target = "productSaleStatus")
  @Mapping(source = "product.productSaleAmount", target = "salesCount")
  @Mapping(source = "product.averageRating", target = "averageRating")
  @Mapping(target = "storeName", ignore = true)
  @Mapping(target = "isLiked", ignore = true)
  @Mapping(source = "product.category", target = "category", qualifiedByName = "mapCategory")
  @Mapping(source = "product.tag", target = "tag", qualifiedByName = "mapTag")
  ProductDetail entityToDetail(Product product);

  @Named("mapCategory")
  @Mapping(source = "category.categoryId", target = "categoryId")
  @Mapping(source = "category.categoryName", target = "categoryName")
  CategoryForProductList mapCategory(Category category);

  @Named("mapTag")
  @Mapping(source = "tag.tagId", target = "key")
  @Mapping(source = "tag.tagName", target = "tagName")
  TagForProductList mapTag(Tag tag);

  @Mappings({
    @Mapping(target = "productId", ignore = true),
    @Mapping(target = "productSaleStatus", expression = "java(ProductSaleStatus.SALE)"),
    @Mapping(target = "tag", ignore = true),
    @Mapping(target = "category", ignore = true),
    @Mapping(target = "productFlowers", ignore = true),
    @Mapping(target = "reviewCount", ignore = true),
    @Mapping(target = "averageRating", ignore = true),
    @Mapping(target = "productSaleAmount", ignore = true),
    @Mapping(target = "storeId", source = "storeId"),
    @Mapping(target = "createdAt", ignore = true),
    @Mapping(target = "updatedAt", ignore = true),
    @Mapping(target = "isDeleted", ignore = true)
  })
  Product subscriptionToEntity(SubscriptionProduct subscriptionProduct, Long storeId);
}
