package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.NewCompilationDto;
import ru.practicum.model.event.update.UpdateEventAdminRequest;
import ru.practicum.model.state.State;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.NewCategoryDto;
import ru.practicum.model.params.ParamsFilterForEvents;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.user.dto.NewUserRequest;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.compilation.CompilationService;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;
import ru.practicum.model.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    UserDto addUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Запрос на добавление пользователя newUserRequest = {}", newUserRequest);
        return userService.addUser(newUserRequest);
    }

    @GetMapping("/users")
    List<UserDto> getUserList(@RequestParam(required = false) List<Long> ids, @RequestParam(defaultValue = "0") Integer from,
                              @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение пользователей ids = {}, from = {}, size = {}", ids, from, size);
        return userService.getUserList(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long userId) {
        log.info("Запрос на удаление пользователя userId = {}", userId);
        userService.deleteUser(userId);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Запрос на добавление категории newCategoryDto = {}", newCategoryDto);
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable Long catId) {
        log.info("Запрос на удаление категории catId = {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    CategoryDto updateCategory(@PathVariable Long catId, @RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Запрос на обновлении категории userId = {}, categoryDto = {}", catId, newCategoryDto);
        return categoryService.updateCategory(catId, newCategoryDto);
    }

    @GetMapping("/events")
    List<EventFullDto> getEventFullList(@RequestParam(required = false) List<Long> users,
                                    @RequestParam(required = false) List<State> states,
                                    @RequestParam(required = false) List<Long> categories,
                                    @RequestParam(required = false) String rangeStart,
                                    @RequestParam(required = false) String rangeEnd,
                                    @RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        ParamsFilterForEvents paramsFilterForEvents = new ParamsFilterForEvents();
        paramsFilterForEvents.setUsers(users);
        paramsFilterForEvents.setStates(states);
        paramsFilterForEvents.setCategories(categories);
        paramsFilterForEvents.setRangeStart(rangeStart);
        paramsFilterForEvents.setRangeEnd(rangeEnd);
        paramsFilterForEvents.setFrom(from);
        paramsFilterForEvents.setSize(size);
        log.info("Запрос на получение списка событий администратором paramsFilterForEvents = {}", paramsFilterForEvents);
        return eventService.getEventFullListForAdmin(paramsFilterForEvents);
    }

    @PatchMapping("/events/{eventId}")
    EventFullDto updateEventFromAdmin(@PathVariable Long eventId, @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Запрос на обновление события Администратором eventId = {}, updateEventAdminRequest = {}", eventId, updateEventAdminRequest);
        return eventService.updateEventFromAdmin(eventId, updateEventAdminRequest);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Запрос на добавление подборки событий newCompilationDto = {}", newCompilationDto);
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompilation(@PathVariable Long compId) {
        log.info("Запрос на удаление подборки событий compId = {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    CompilationDto updateCompilation(@PathVariable Long compId, @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Запрос на обновление подборки событий compId = {}, newCompilationDto = {} ", compId, newCompilationDto);
        return compilationService.updateCompilation(compId, newCompilationDto);
    }
}
