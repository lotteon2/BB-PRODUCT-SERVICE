package kr.bb.product.domain.product.application.port.out;

import java.util.List;
import kr.bb.product.common.dto.NewOrderEvent.ProductCount;
import kr.bb.product.common.dto.ReviewRegisterEvent;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.mapper.ProductCommand;
import kr.bb.product.domain.product.mapper.ProductCommand.ProductUpdate;
import kr.bb.product.domain.tag.entity.Tag;

public interface ProductCommandOutPort {
  void createProduct(Product product);

  void updateSubscriptionProduct(ProductCommand.UpdateSubscriptionProduct product);

  void updateProductReviewData(ReviewRegisterEvent reviewRegisterEvent);

  void updateProductSaleCount(List<ProductCount> newOrderEvent);

  void updateProductSaleStatus(Product product);

  void updateProductSaleStatus(Product product, ProductSaleStatus productSaleStatus);

  void updateProductSaleStatus(String productId, ProductUpdate productRequestData);

  void updateProductSaleStatus(String productId, ProductUpdate productRequestData, List<Tag> tags);
}
