package kr.bb.product.domain.category.repository.jpa;

import java.util.Optional;
import kr.bb.product.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Query("select c from Category c where c.categoryId=:id")
  Optional<Category> findByCategoryId(Long id);
}
