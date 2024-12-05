package ru.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.params.ParamsFilterForPublicEvents;
import ru.practicum.model.sort.Sort;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.compilation.CompilationService;
import ru.practicum.service.event.EventService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublicController {

    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    @GetMapping("/categories")
    List<CategoryDto> getCategoryList(@RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получения списка категорий from = {}, size = {}", from, size);
        return categoryService.getCategoryList(from, size);
    }

    @GetMapping("/categories/{catId}")
    CategoryDto getCategory(@PathVariable Long catId) {
        log.info("Запрос на категории по catId = {}", catId);
        return categoryService.getCategory(catId);
    }

    @GetMapping("/events")
    List<EventFullDto> getPublicEventsByParams(@RequestParam(required = false) String text,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) Boolean paid,
                                       @RequestParam(required = false) String rangeStart,
                                       @RequestParam(required = false) String rangeEnd,
                                       @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                       @RequestParam(required = false) Sort sort,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size,
                                               HttpServletRequest request) {
        ParamsFilterForPublicEvents params = new ParamsFilterForPublicEvents();
        params.setText(text);
        params.setCategories(categories);
        params.setPaid(paid);
        params.setRangeStart(rangeStart);
        params.setRangeEnd(rangeEnd);
        params.setOnlyAvailable(onlyAvailable);
        params.setSort(sort);
        params.setFrom(from);
        params.setSize(size);

        log.info("Запрос на получение общедоступных событий params = {}", params);
        return eventService.getPublicEventsByParams(params, request);
    }

    @GetMapping("/events/{id}")
    EventFullDto getPublicEvent(@PathVariable Long id,  HttpServletRequest request) {
        log.info("Запрос на получение события по id = {}", id);
        return eventService.getPublicEvent(id, request);
    }

    @GetMapping("/compilations")
    List<CompilationDto> getCompilationList(@RequestParam(required = false) Boolean pinned,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка подборок событий по pinned = {}, from = {}, size = {}", pinned, from, size);
        return  compilationService.getCompilationList(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    CompilationDto getCompilation(@PathVariable Long compId) {
        log.info("Запрос на получение подборок событий по compId = {}", compId);
        return  compilationService.getCompilation(compId);
    }
}
