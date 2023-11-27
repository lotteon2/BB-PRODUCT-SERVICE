package kr.bb.product.domain.product.application.usecase;

import kr.bb.product.domain.product.entity.ProductCommand;

public interface ProductStoreUseCase {
    void updateProductSaleStatus(String productId, ProductCommand.ProductUpdate productRequestData);
    void createProduct(ProductCommand.ProductRegister productRequestData);
}
