package kr.bb.product.repository.jpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import javax.transaction.Transactional;
import kr.bb.product.entity.Category;
import kr.bb.product.errors.CategoryNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class CategoryRepositoryTest {
  @Autowired CategoryRepository categoryRepository;

  @Test
  @DisplayName("select category by id ")
  void selectCategoryById() {
    Category category = categoryRepository.findById(1L).orElseThrow(CategoryNotFoundException::new);
    System.out.println(category.toString());
    assertThat(category).isNotNull();
  }
}
