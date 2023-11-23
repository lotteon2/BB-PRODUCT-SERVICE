package kr.bb.product.errors;

public class CategoryNotFoundException extends RuntimeException {
  private static final String message = "카테고리가 존재하지 않습니다.";

  public CategoryNotFoundException() {
    super(message);
  }
}
