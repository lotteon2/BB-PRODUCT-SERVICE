package kr.bb.product.domain.review.entity.mapper;

import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
  ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

  @Mappings({
    @Mapping(target = "userId", source = "userId"),
    @Mapping(target = "productId", source = "productId"),
    @Mapping(target = "reviewId", ignore = true),
    @Mapping(target = "reviewRating", source = "register.rating"),
    @Mapping(target = "reviewImages", ignore = true),
  })
  Review toEntity(ReviewCommand.Register register, Long userId, String productId);
}
