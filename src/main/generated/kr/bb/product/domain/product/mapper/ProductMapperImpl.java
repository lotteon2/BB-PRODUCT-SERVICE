package kr.bb.product.domain.product.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.product.api.request.ProductRequestData;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.domain.tag.entity.Tag;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-25T00:47:50+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.18 (Azul Systems, Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product createProductRequestToEntity(ProductRequestData productRequestData, Category category, List<Tag> tag, List<ProductFlowers> flowers) {
        if ( productRequestData == null && category == null && tag == null && flowers == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        if ( productRequestData != null ) {
            product.productName( productRequestData.getProductName() );
            product.productSummary( productRequestData.getProductSummary() );
            product.productPrice( productRequestData.getProductPrice() );
            product.productDescriptionImage( productRequestData.getProductDescriptionImage() );
            product.storeId( productRequestData.getStoreId() );
        }
        product.category( category );
        List<Tag> list = tag;
        if ( list != null ) {
            product.tag( new ArrayList<Tag>( list ) );
        }
        List<ProductFlowers> list1 = flowers;
        if ( list1 != null ) {
            product.productFlowers( new ArrayList<ProductFlowers>( list1 ) );
        }
        product.productSaleStatus( ProductSaleStatus.SALE );

        return product.build();
    }

    @Override
    public ProductFlowers flowerRequestToFlowers(ProductFlowersRequestData flowersRequestData) {
        if ( flowersRequestData == null ) {
            return null;
        }

        ProductFlowers.ProductFlowersBuilder productFlowers = ProductFlowers.builder();

        productFlowers.flowerId( flowersRequestData.getFlowerId() );
        productFlowers.flowerCount( flowersRequestData.getFlowerCount() );

        return productFlowers.build();
    }

    @Override
    public List<ProductFlowers> flowerRequestToFlowersList(List<ProductFlowersRequestData> flowersRequestData) {
        if ( flowersRequestData == null ) {
            return null;
        }

        List<ProductFlowers> list = new ArrayList<ProductFlowers>( flowersRequestData.size() );
        for ( ProductFlowersRequestData productFlowersRequestData : flowersRequestData ) {
            list.add( flowerRequestToFlowers( productFlowersRequestData ) );
        }

        return list;
    }
}
