package kr.bb.product.domain.salesresume.entity;

import kr.bb.product.domain.salesresume.entity.mapper.SalesResumeMapper;
import lombok.Builder;
import lombok.Getter;

public class SalesResumeCommand {
  @Getter
  @Builder
  public static class SalesResumeRequest {
    private Long userId;
    private String phoneNumber;
    private String userName;
    private String productId;

    public static SalesResume toEntity(SalesResumeCommand.SalesResumeRequest request) {
      return SalesResumeMapper.INSTANCE.toEntity(request);
    }

    public void setUserId(Long userId) {
      this.userId = userId;
    }

    public void setProductId(String productId) {
      this.productId = productId;
    }

    public void setPhoneNumber(String format) {
      this.phoneNumber = format;
    }
  }
}
