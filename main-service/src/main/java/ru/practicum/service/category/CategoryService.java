package ru.practicum.service.category;

import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategoryList(Integer from, Integer size);

    CategoryDto getCategory(Long catId);
}
