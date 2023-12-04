package kr.bb.product.domain.salesresume.entity.mapper;

import java.util.List;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import kr.bb.product.domain.salesresume.entity.SalesResumeCommand;
import kr.bb.product.domain.salesresume.entity.SalesResumeCommand.ResaleNotification;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SalesResumeMapper {
  SalesResumeMapper INSTANCE = Mappers.getMapper(SalesResumeMapper.class);

  @Mapping(target = "saleResumeId", ignore = true)
  @Mapping(target = "isNotified", ignore = true)
  SalesResume toEntity(SalesResumeCommand.SalesResumeRequest request);

  @Named("RESALE")
  SalesResumeCommand.ResaleNotification toResaleNotificationList(SalesResume salesResume);

  @IterableMapping(qualifiedByName = "RESALE")
  List<ResaleNotification> toResaleNotificationList(List<SalesResume> salesResume);
}
