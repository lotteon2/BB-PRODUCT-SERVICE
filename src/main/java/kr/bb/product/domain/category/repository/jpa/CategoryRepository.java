package kr.bb.product.domain.category.repository.jpa;

import kr.bb.product.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
