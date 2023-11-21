package kr.bb.product.mapper;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.dto.CategoryResponseDto;
import kr.bb.product.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CategoryMapperTest {
  private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

  @Test
  @DisplayName("카테고리 엔티티 변환")
  void entityToDto() {
    Category category = Category.builder().categoryName("categoryname1").build();

    CategoryResponseDto categoryResponseDto = categoryMapper.entityToDto(category);
    Assertions.assertThat(category.getCategoryName())
        .isEqualTo(categoryResponseDto.getCategoryName());
  }

  @Test
  @DisplayName("카테고리 엔티티 리스트 변환")
  void entityToDtoList() {
    List<Category> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Category category = Category.builder().categoryName(String.format("category%d", i)).build();
      list.add(category);
    }
    List<CategoryResponseDto> categoryResponseDtos = categoryMapper.categoryListToDtoList(list);
    Assertions.assertThat(10).isEqualTo(categoryResponseDtos.size());
  }
}
