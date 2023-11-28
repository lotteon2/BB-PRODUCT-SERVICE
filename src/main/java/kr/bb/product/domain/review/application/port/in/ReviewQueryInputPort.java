package kr.bb.product.domain.review.application.port.in;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.review.application.port.out.ReviewQueryOutPort;
import kr.bb.product.domain.review.application.usecase.ReviewQueryUseCase;
import kr.bb.product.domain.review.entity.ReviewCommand.StoreReview.StoreReviewItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewQueryInputPort implements ReviewQueryUseCase {
  private final ReviewQueryOutPort reviewQueryOutPort;
  private final ProductQueryOutPort productQueryOutPort;

  @Override
  public List<StoreReviewItem> findReviewByStoreId(Long storeId, Pageable pageable) {
    Page<Product> productByStoreId = productQueryOutPort.findProductByStoreId(storeId, pageable);
    List<Product> content = productByStoreId.getContent();
    List<String> productId =
        content.stream().map(Product::getProductId).collect(Collectors.toList());

    Map<String, String> productName =
        content.stream().collect(Collectors.toMap(Product::getProductId, Product::getProductName));

    return reviewQueryOutPort.findReviewByProductId(productId).stream()
        .map(
            item ->
                StoreReviewItem.builder()
                    .reviewId(item.getReviewId())
                    .reviewImages(item.getReviewImages())
                    .profileImage(item.getProfileImage())
                    .rating(item.getReviewRating())
                    .nickname(item.getNickname())
                    .productName(productName.get(item.getProductId()))
                    .createdAt(item.getCreatedAt())
                    .build())
        .collect(Collectors.toList());
  }
}
