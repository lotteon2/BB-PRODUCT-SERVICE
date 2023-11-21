package kr.bb.product.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kr.bb.product.entity.Category;
import kr.bb.product.errors.category.CategoryNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CategoryRepositoryTest {
  @Autowired private CategoryRepository categoryRepository;

  @Test
  @DisplayName("카테고리 조회 테스트 ")
  void getCategoryTest() {
    Category category = Category.builder().categoryName("cateogry1").build();
    categoryRepository.save(category);
    List<Category> all = categoryRepository.findAll();
    Assertions.assertThat(1).isEqualTo(all.size());
  }

  @Test
  @DisplayName("카테고리 전체 조회")
  void getAllCategory() {
    // given
    for (int i = 0; i < 10; i++) {
      Category category = Category.builder().categoryName(String.format("category%d", i)).build();
      categoryRepository.save(category);
    }
    // when
    List<Category> all = categoryRepository.findAll();
    // then
    Assertions.assertThat(10).isEqualTo(all.size());
  }

  @Test
  @DisplayName("카테고리 전체 조회 실패")
  void getAllCategoryFail() {
    // given
    // no data

    // when
    List<Category> all = categoryRepository.findAll();

    // then
    assertThatThrownBy(
            () -> {
              if (all.size() != 10) throw new CategoryNotFoundException();
            })
        .isInstanceOf(CategoryNotFoundException.class)
        .hasMessageContaining("카테고리");
  }
}
