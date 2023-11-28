package kr.bb.product.domain.review.adapter.out.jpa;

import static kr.bb.product.domain.review.entity.QReview.review;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import kr.bb.product.domain.review.application.port.out.ReviewQueryOutPort;
import kr.bb.product.domain.review.entity.ReviewCommand;
import kr.bb.product.domain.review.entity.ReviewCommand.StoreReview.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewQueryRepository implements ReviewQueryOutPort {
  private final JPAQueryFactory jpaQueryFactory;

  public ReviewQueryRepository(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public List<Review> findReviewsWithReviewImages(List<String> productIds) {
    return null;
  }

  @Override
  public List<Review> findReviewByProductId(String productId) {
    return jpaQueryFactory
        .select(
            Projections.constructor(
                ReviewCommand.StoreReview.Review.class,
                review.reviewId,
                review.createdAt,
                review.reviewRating,
                review.reviewContent,
                review.reviewImages,
                review.nickname))
        .from(review)
        .where(review.productId.eq(productId))
        .fetch();
  }
}
