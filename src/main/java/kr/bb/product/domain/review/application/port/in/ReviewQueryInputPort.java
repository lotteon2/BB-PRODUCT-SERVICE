package kr.bb.product.domain.review.application.port.in;

import bloomingblooms.errors.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.review.application.port.out.ReviewQueryOutPort;
import kr.bb.product.domain.review.application.usecase.ReviewQueryUseCase;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.mapper.ReviewCommand;
import kr.bb.product.domain.review.mapper.ReviewCommand.ReviewList;
import kr.bb.product.domain.review.mapper.ReviewCommand.SortOption;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryInputPort implements ReviewQueryUseCase {
  private final ReviewQueryOutPort reviewQueryOutPort;
  private final ProductQueryOutPort productQueryOutPort;

  @NotNull
  private static Pageable getPageable(Pageable pageable, SortOption sortOption) {
    Direction direction = Direction.DESC;
    if (SortOption.LOW.equals(sortOption)) direction = Direction.ASC;
    return PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        Sort.by(direction, sortOption.getProperty()));
  }

  /**
   * 가게 사장 리뷰 조회
   *
   * @param storeId
   * @param pageable
   * @param sortOption
   * @return
   */
  @Override
  public ReviewCommand.StoreReviewList findReviewByStoreId(
      Long storeId, Pageable pageable, SortOption sortOption) {

    Pageable pageRequest = getPageable(pageable, sortOption);

    List<Product> productByStoreId = productQueryOutPort.findProductByStoreId(storeId);
    if (productByStoreId.isEmpty()) throw new EntityNotFoundException();
    List<String> productId =
        productByStoreId.stream().map(Product::getProductId).collect(Collectors.toList());

    Map<String, String> productName =
        productByStoreId.stream()
            .collect(Collectors.toMap(Product::getProductId, Product::getProductName));

    Page<Review> reviewByProductId =
        reviewQueryOutPort.findReviewByProductId(productId, pageRequest);
    return ReviewCommand.StoreReviewList.getData(reviewByProductId, productName);
  }

  @Override
  public ReviewCommand.ProductDetailReviewList findReviewsByProductId(
      String productId, Pageable pageable, SortOption sortOption) {
    Pageable pageRequest = getPageable(pageable, sortOption);
    return ReviewCommand.ProductDetailReviewList.getData(
        reviewQueryOutPort.findReviewsByProductId(productId, pageRequest));
  }

  @Override
  public ReviewList findReviewsByUserId(Long userId, Pageable pageable, SortOption sortOption) {
    Pageable pageRequest = getPageable(pageable, sortOption);
    Page<Review> reviewsByUserId = reviewQueryOutPort.findReviewsByUserId(userId, pageRequest);
    List<String> productIds =
        reviewsByUserId.getContent().stream()
            .map(Review::getProductId)
            .collect(Collectors.toList());
    return ReviewCommand.ReviewList.getData(
        reviewsByUserId,
        productQueryOutPort.findProductNameByProductIdsForReviewByUserId(productIds));
  }
}
