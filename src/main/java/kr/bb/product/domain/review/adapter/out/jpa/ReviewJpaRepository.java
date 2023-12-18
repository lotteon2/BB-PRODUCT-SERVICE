package kr.bb.product.domain.review.adapter.out.jpa;

import java.util.List;
import kr.bb.product.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
  @Query(
      "SELECT DISTINCT r FROM Review r LEFT JOIN r.reviewImages i WHERE r.productId IN :productIds")
  Page<Review> findReviewByProductIds(
      @Param("productIds") List<String> productIds, Pageable pageable);

  @Query(value = "SELECT r FROM Review r LEFT JOIN r.reviewImages i WHERE r.productId = :productId")
  Page<Review> findReviewsByProductId(@Param("productId") String productId, Pageable pageable);

  @Query(value = "SELECT r FROM Review r LEFT JOIN r.reviewImages i WHERE r.userId = :userId")
  Page<Review> findReviewsByUserId(@Param("userId") Long userId, Pageable pageable);

  @Query("select count(r) from Review r where r.productId=:productId")
  Long findReviewCountByProductId(String productId);
}
