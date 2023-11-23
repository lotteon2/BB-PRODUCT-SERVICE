package kr.bb.product.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Flowers {
  private Long flowerId;
  private Long flowerCount;
  @Builder.Default private Boolean isRepresentative = false;
}
