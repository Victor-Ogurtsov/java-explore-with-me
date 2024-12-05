package ru.practicum.model.category.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.model.category.Category;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);

    Category fromCategoryDto(CategoryDto categoryDto);

    List<CategoryDto> toCategoryDtoList(List<Category> categoryList);
}