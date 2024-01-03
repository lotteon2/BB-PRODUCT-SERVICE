package kr.bb.product.domain.tag.entity;

import java.util.List;
import kr.bb.product.domain.product.mapper.mapper.ProductMapper;
import lombok.Builder;
import lombok.Getter;

public class TagCommand {

  public static List<TagForProductList> entityToTagList(List<Tag> tag) {
    return ProductMapper.INSTANCE.entityToTagList(tag);
  }

  @Getter
  @Builder
  public static class TagForProductList {
    private Long key;
    private String tagName;
  }
}
