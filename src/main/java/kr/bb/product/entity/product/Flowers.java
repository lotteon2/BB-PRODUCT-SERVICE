package kr.bb.product.entity.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Flowers {
  private Long flowerId;
  private Long flowerCount;
  private Boolean isRepresentative;
}
