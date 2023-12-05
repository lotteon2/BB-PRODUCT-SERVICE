package kr.bb.product.repository.jpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import kr.bb.product.domain.tag.entity.Tag;
import kr.bb.product.domain.tag.repository.jpa.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;

@SpringBootTest
@Transactional
class TagRepositoryTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
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
