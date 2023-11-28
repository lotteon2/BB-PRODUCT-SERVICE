package kr.bb.product.domain.review.application.port.in;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.product.application.port.in.ProductCommandInputPort;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.domain.review.entity.ReviewCommand.StoreReview.StoreReviewItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReviewQueryInputPortTest {
  @Autowired private ReviewQueryInputPort reviewQueryInputPort;
  @Autowired private ProductCommandInputPort productCommandInputPort;

  private ProductCommand.ProductRegister getProductRequestData() {
    List<Long> tagList = new ArrayList<>();
    tagList.add(1L);
    tagList.add(2L);

    List<ProductFlowersRequestData> list = new ArrayList<>();
    list.add(ProductFlowersRequestData.builder().flowerId(1L).flowerCount(2L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(3L).flowerCount(3L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(2L).flowerCount(2L).build());

    return ProductCommand.ProductRegister.builder()
        .categoryId(1L)
        .productTag(tagList)
        .representativeFlower(
            ProductFlowersRequestData.builder().flowerCount(3L).flowerId(1L).build())
        .flowers(list)
        .storeId(1L)
        .productName("Example Product")
        .productSummary("Product Summary")
        .productDescriptionImage("image")
        .productThumbnail("thumbnail")
        .productPrice(100L)
        .productDescriptionImage("image_url")
        .build();
  }

  @Test
  void findReviewByStoreId() {
    productCommandInputPort.createProduct(getProductRequestData());

    PageRequest pageRequest = PageRequest.of(0, 5);
    List<StoreReviewItem> reviewByStoreId =
        reviewQueryInputPort.findReviewByStoreId(1L, pageRequest);
    System.out.println(reviewByStoreId.toString());
  }
}
