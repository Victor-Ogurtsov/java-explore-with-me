package ru.practicum.service.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ViolationRestrictException;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.QCategory;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.CategoryMapper;
import ru.practicum.model.category.dto.NewCategoryDto;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        String name = newCategoryDto.getName();
        checkCategoryByName(name);
        Category category = new Category();
        category.setName(name);
        Category addedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(addedCategory);
    }

    @Override
    public void deleteCategory(Long catId) {
        getCategoryOrThrowException(catId);
        List<Event> eventList = eventRepository.getEventListByCategoryId(catId);
        if (!eventList.isEmpty()) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    "Нельзя удалять категории, за которыми есть закрепленные позиции!");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        Category category = getCategoryOrThrowException(catId);
        if (category.getName().equals(newCategoryDto.getName())) {
            return categoryMapper.toCategoryDto(category);
        }
        checkCategoryByName(newCategoryDto.getName());
        category.setName(newCategoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public List<CategoryDto> getCategoryList(Integer from, Integer size) {
        List<Category> categoryList = jpaQueryFactory.selectFrom(QCategory.category).offset(from).limit(size).fetch();
        return categoryMapper.toCategoryDtoList(categoryList);
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = getCategoryOrThrowException(catId);
        return categoryMapper.toCategoryDto(category);
    }

     private Category getCategoryOrThrowException(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "Category with id=" + catId + " was not found"));
    }

    private void checkCategoryByName(String name) {
        if (categoryRepository.findByName(name).isPresent()) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name]; nested exception" +
                            " is org.hibernate.exception.ConstraintViolationException: could not execute statement");
        }
    }
}