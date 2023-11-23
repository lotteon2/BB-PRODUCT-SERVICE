package kr.bb.product.mapper;

import java.util.List;
import kr.bb.product.vo.Flowers;
import kr.bb.product.vo.FlowersRequestData;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FlowerMapper {
  @Named("FR2FL")
  @Mapping(target = "isRepresentative", ignore = true)
  Flowers flowerRequestToFlowers(FlowersRequestData flowersRequestData);

  @IterableMapping(qualifiedByName = "FR2FL")
  List<Flowers> flowerRequestToFlowersList(List<FlowersRequestData> flowersRequestData);
}
