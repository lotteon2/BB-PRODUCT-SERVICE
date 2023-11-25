package kr.bb.product.domain.review.entity.mapper;

import java.util.List;
import kr.bb.product.domain.review.entity.ReviewImages;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewImageMapper {
  ReviewImageMapper INSTANCE = Mappers.getMapper(ReviewImageMapper.class);

  @Named("IMG")
  @Mappings({
    @Mapping(target = "reviewImageUrl", source = "image"),
    @Mapping(target = "review", ignore = true),
    @Mapping(target = "reviewImageId", ignore = true)
  })
  ReviewImages toEntity(String image);

  @IterableMapping(qualifiedByName = "IMG")
  List<ReviewImages> toEntityList(List<String> images);
}
