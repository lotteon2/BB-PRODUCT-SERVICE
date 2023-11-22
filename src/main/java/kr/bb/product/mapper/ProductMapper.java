package kr.bb.product.mapper;

import kr.bb.product.dto.request.ProductRequestData;
import kr.bb.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(target = "productId", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "isDeleted", ignore = true)
  Product entityToData(ProductRequestData productRequestData);
}
