package ru.practicum.model.event.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.location.Location;

@Setter
@Getter
@ToString
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid = false;
    @PositiveOrZero
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
/*
 if (newEventDto.getCategory() == null) {
            throw new IncorrectRequestException("BAD_REQUEST", "Incorrectly made request.",
                    "Field: category. Error: must not be blank. Value: null");
        }
        User user = getUserOrThrowException(userId);
        Category category = getCategoryOrThrowException(newEventDto.getCategory());
        Event event = newEventMapper.fromNewEventDto(newEventDto);
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ViolationRestrictException("FORBIDDEN", "For the requested operation the conditions are not met.",
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + event.getEventDate());
        }


         private void checkNewEventDto(NewEventDto dto) {
        String annotation = dto.getAnnotation();
        if (annotation == null) {
            getThrowIncorrectRequestException("Поле Annotation не должно быть null");
        }
        if (annotation.isBlank()) {
            getThrowIncorrectRequestException("Поле Annotation не должно быть пустым");
        }
        if (annotation.length() < 20 || annotation.length() > 2000) {
            getThrowIncorrectRequestException("Аннотация по количеству символов должна быть от 20 до 2000");
        }
        String description = dto.getDescription();
        if (description == null) {
            getThrowIncorrectRequestException("Поле Description не должно быть null");
        }
        if (description.isBlank()) {
            getThrowIncorrectRequestException("Поле Description не должно быть пустым");
        }
        if (description.length() < 20 || description.length() > 7000) {
            getThrowIncorrectRequestException("Description по количеству символов должно быть от 20 до 2000");
        }
        String title = dto.getTitle();
        if (title == null) {
            getThrowIncorrectRequestException("Поле Title не должно быть null");
        }
        if (title.isBlank()) {
            getThrowIncorrectRequestException("Поле Title не должно быть пустым");
        }
        if (title.length() < 3 || title.length() > 120) {
            getThrowIncorrectRequestException("Description по количеству символов должно быть от 20 до 2000");
        }
        if (dto.getParticipantLimit() == null) {
            getThrowIncorrectRequestException("Поле ParticipantLimit не должно быть null");
        }
        if (dto.getParticipantLimit() < 0) {
            getThrowIncorrectRequestException("Поле ParticipantLimit не должно быть меньше нуля");
        }
        String eventDate = dto.getEventDate();
        LocalDateTime eventDateLdt =LocalDateTime.parse(eventDate, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        if (eventDateLdt.isBefore(LocalDateTime.now())) {
            getThrowIncorrectRequestException("Дата события не длжна быть в прошлом");
        }
 */