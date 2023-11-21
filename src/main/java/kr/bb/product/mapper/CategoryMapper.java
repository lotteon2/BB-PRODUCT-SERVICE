package kr.bb.product.mapper;

import java.util.List;
import kr.bb.product.dto.CategoryResponseDto;
import kr.bb.product.entity.Category;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  @Named("CATEGORY")
  @Mapping(target = "categoryId", source = "id")
  CategoryResponseDto entityToDto(Category category);

  @IterableMapping(qualifiedByName = "CATEGORY")
  List<CategoryResponseDto> categoryListToDtoList(List<Category> categories);
}
