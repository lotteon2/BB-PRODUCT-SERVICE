package kr.bb.product.service;

import java.util.List;
import javax.transaction.Transactional;
import kr.bb.product.dto.CategoryResponseDto;
import kr.bb.product.entity.Category;
import kr.bb.product.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class CategoryServiceTest {

  @Autowired private CategoryRepository categoryRepository;

  @Autowired private CategoryService categoryService;

  @Test
  @DisplayName("카테고리 전체 조회")
  void getAllCategories() {
    // given
    for (int i = 0; i < 10; i++) {
      Category category = Category.builder().categoryName(String.format("category%d", i)).build();
      categoryRepository.save(category);
    }

    List<CategoryResponseDto> allCategory = categoryService.getAllCategory();
    Assertions.assertThat(10).isEqualTo(allCategory.size());
  }
}