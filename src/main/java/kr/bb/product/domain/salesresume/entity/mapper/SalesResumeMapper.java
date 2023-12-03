package kr.bb.product.domain.salesresume.entity.mapper;

import kr.bb.product.domain.salesresume.entity.SalesResume;
import kr.bb.product.domain.salesresume.entity.SalesResumeCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SalesResumeMapper {
  SalesResumeMapper INSTANCE = Mappers.getMapper(SalesResumeMapper.class);

  @Mapping(target = "saleResumeId", ignore = true)
  @Mapping(target = "isNotified", ignore = true)
  SalesResume toEntity(SalesResumeCommand.SalesResumeRequest request);
}
