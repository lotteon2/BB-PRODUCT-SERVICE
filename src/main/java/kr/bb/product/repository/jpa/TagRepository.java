package kr.bb.product.repository.jpa;

import kr.bb.product.entity.product.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {}
