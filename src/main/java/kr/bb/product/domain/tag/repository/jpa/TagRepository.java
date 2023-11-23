package kr.bb.product.domain.tag.repository.jpa;

import kr.bb.product.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {}
