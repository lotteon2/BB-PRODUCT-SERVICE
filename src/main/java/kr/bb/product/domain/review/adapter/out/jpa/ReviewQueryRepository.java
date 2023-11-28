package kr.bb.product.domain.review.adapter.out.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import kr.bb.product.domain.review.application.port.out.ReviewQueryOutPort;
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
}
