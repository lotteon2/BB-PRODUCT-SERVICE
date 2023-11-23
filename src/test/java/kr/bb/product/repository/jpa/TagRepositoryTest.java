package kr.bb.product.repository.jpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import kr.bb.product.entity.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagRepositoryTest {
  @Autowired TagRepository tagRepository;
  @Autowired DataSource dataSource;

  @Test
  @DisplayName("select tag")
  void selectTag() {
    List<Tag> all = tagRepository.findAll();
    System.out.println(all.toString());
    assertThat(all.size()).isGreaterThan(0);
  }
}
