package ru.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.comment.dto.CommentDto;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.complaint.dto.ComplaintCommentDto;
import ru.practicum.model.complaint.dto.NewComplaintComment;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.params.ParamsFilterForPublicEvents;
import ru.practicum.model.sort.Sort;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.compilation.CompilationService;
import ru.practicum.service.complaint.ComplaintCommentService;
import ru.practicum.service.event.EventService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublicController {

    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final CommentService commentService;
    private final ComplaintCommentService complaintCommentService;

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

    @GetMapping("/comments/{eventId}")
    List<CommentDto> getCommentList(@PathVariable Long eventId) {
        log.info("Запрос на получение списка комментариев к событию по eventId = {}", eventId);
        return commentService.getCommentList(eventId);
    }

    @PostMapping("/complaints/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    ComplaintCommentDto addComplaintComment(@PathVariable Long commentId, @RequestBody @Valid NewComplaintComment newComplaintComment) {
        log.info("Запрос на добавление жалобы на комментарий события commentId = {}, newComplaintComment = {} ",
                commentId, newComplaintComment);
        return complaintCommentService.addComplaintComment(commentId, newComplaintComment);
    }
}
