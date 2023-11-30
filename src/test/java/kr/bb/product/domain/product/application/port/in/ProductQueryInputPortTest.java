package kr.bb.product.domain.product.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.product.adapter.out.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.entity.ProductCommand.ProductList;
import kr.bb.product.domain.product.entity.ProductCommand.ProductRegister;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProduct;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductList;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductQueryInputPortTest {
  @Autowired ProductQueryInputPort productQueryInputPort;
  @Autowired ProductCommandInputPort productCommandInputPort;
  @Autowired ProductMongoRepository productMongoRepository;

  @Test
  @DisplayName("가게 사장 상품 리스트 조회 ")
  void getStoreProducts() {
    productMongoRepository.deleteAll();
    ProductFlowers build1 = ProductFlowers.builder().flowerId(1L).isRepresentative(true).build();
    ProductFlowersRequestData flowers =
        ProductFlowersRequestData.builder().flowerId(1L).flowerCount(4L).build();
    List<Long> list = new ArrayList<>();
    List<ProductFlowersRequestData> lst = new ArrayList<>();
    lst.add(flowers);
    list.add(1L);
    for (int i = 0; i < 10; i++) {
      ProductRegister build =
          ProductRegister.builder()
              .productName("name")
              .storeId(1L)
              .categoryId(1L)
              .flowers(lst)
              .productTag(list)
              .representativeFlower(
                  ProductFlowersRequestData.builder().flowerCount(3L).flowerId(1L).build())
              .build();
      productCommandInputPort.createProduct(build);
    }
    productMongoRepository
        .findAll()
        .forEach(item -> System.out.println(item.getProductFlowers().toString()));

    PageRequest pageRequest = PageRequest.of(0, 5);

    ProductList productsByCategory = productQueryInputPort.getProductsByCategory(1L, pageRequest);
    StoreProductList storeProducts =
        productQueryInputPort.getStoreProducts(1L, 1L, null, ProductSaleStatus.SALE, pageRequest);
    for (StoreProduct p : storeProducts.getProducts()) {
      System.out.println(p.getProductSaleStatus());
      System.out.println(p.getRepresentativeFlower());
    }
    assertThat(storeProducts.getProducts().size()).isGreaterThan(0);
  }
}
