package kr.bb.product.repository.jpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import javax.transaction.Transactional;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.category.repository.jpa.CategoryRepository;
import kr.bb.product.exception.errors.CategoryNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;

@SpringBootTest
@Transactional
class CategoryRepositoryTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired CategoryRepository categoryRepository;
  @MockBean RedissonAutoConfiguration redissonAutoConfiguration;

  @Test
  @DisplayName("select category by id ")
  void selectCategoryById() {
    Category category = categoryRepository.findById(1L).orElseThrow(CategoryNotFoundException::new);
    System.out.println(category.toString());
    assertThat(category).isNotNull();
  }
}
