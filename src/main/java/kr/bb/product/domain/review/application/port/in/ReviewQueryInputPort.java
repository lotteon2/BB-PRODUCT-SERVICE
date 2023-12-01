package kr.bb.product.domain.review.application.port.in;

import bloomingblooms.errors.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.review.application.port.out.ReviewQueryOutPort;
import kr.bb.product.domain.review.application.usecase.ReviewQueryUseCase;
import kr.bb.product.domain.review.entity.ReviewCommand.SortOption;
import kr.bb.product.domain.review.entity.ReviewCommand.StoreReview.StoreReviewItem;
import kr.bb.product.domain.review.entity.ReviewImages;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
  public List<StoreReviewItem> findReviewByStoreId(
      Long storeId, Pageable pageable, SortOption sortOption) {

    Pageable pageRequest = getPageable(pageable, sortOption);

    List<Product> productByStoreId = productQueryOutPort.findProductByStoreId(storeId);
    if (productByStoreId.isEmpty()) throw new EntityNotFoundException();
    List<String> productId =
        productByStoreId.stream().map(Product::getProductId).collect(Collectors.toList());

    Map<String, String> productName =
        productByStoreId.stream()
            .collect(Collectors.toMap(Product::getProductId, Product::getProductName));

    return reviewQueryOutPort.findReviewByProductId(productId, pageRequest).stream()
        .map(
            item ->
                StoreReviewItem.builder()
                    .reviewId(item.getReviewId())
                    .reviewImages(
                        item.getReviewImages().stream()
                            .map(ReviewImages::getReviewImageUrl)
                            .collect(Collectors.toList()))
                    .profileImage(item.getProfileImage())
                    .rating(item.getReviewRating())
                    .nickname(item.getNickname())
                    .productName(productName.get(item.getProductId()))
                    .createdAt(item.getCreatedAt())
                    .build())
        .collect(Collectors.toList());
  }
}
