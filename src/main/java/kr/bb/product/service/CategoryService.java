package kr.bb.product.service;

import java.util.List;
import kr.bb.product.dto.CategoryResponseDto;
import kr.bb.product.entity.Category;
import kr.bb.product.mapper.CategoryMapper;
import kr.bb.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public List<CategoryResponseDto> getAllCategory() {
    List<Category> categoryList = categoryRepository.findAll();
    return categoryMapper.categoryListToDtoList(categoryList);
  }
}
