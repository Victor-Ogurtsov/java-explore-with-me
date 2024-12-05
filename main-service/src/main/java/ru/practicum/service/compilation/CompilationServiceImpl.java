package ru.practicum.service.compilation;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.IncorrectRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.QCompilation;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.CompilationMapper;
import ru.practicum.model.compilation.dto.NewCompilationDto;
import ru.practicum.model.compilation.dto.NewCompilationMapper;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.event.EventService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {

    private final EventRepository eventRepository;
    private final NewCompilationMapper newCompilationMapper;
    private final CompilationMapper compilationMapper;
    private final CompilationRepository compilationRepository;
    private final EventService eventService;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> eventSet = new HashSet<>();
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (!newCompilationDto.getEvents().isEmpty()) {
            List<Event> eventList = getEventListOrThrowException(newCompilationDto.getEvents());
            eventSet.addAll(eventList);
            eventShortDtoList.addAll(eventService.getEventShortDtoListFromEventList(eventList));
        }
        Compilation compilation = newCompilationMapper.fromNewCompilationDto(newCompilationDto, eventSet);
        Compilation addedCompilation = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(addedCompilation, eventShortDtoList);
    }

    @Override
    public void deleteCompilation(Long compId) {
        getCompilationOrThrowException(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = getCompilationOrThrowException(compId);
        Boolean pinned = newCompilationDto.getPinned();
        if (pinned != null) {
            compilation.setPinned(pinned);
        }
        String title = newCompilationDto.getTitle();
        if (title != null) {
            if (title.isBlank()) {
                throw new IncorrectRequestException("BAD_REQUEST", "Incorrectly made request.",
                        "Field: title. Error: must not be blank.");
            }
            if (title.length() > 50) {
                throw new IncorrectRequestException("BAD_REQUEST", "Incorrectly made request.",
                        "Поле Title должно быть не больше 50 символов!");
            }
            compilation.setTitle(title);
        }
        List<Long> events = newCompilationDto.getEvents();
        if (events != null) {
            List<Event> eventList = new ArrayList<>();
            if (!events.isEmpty()) {
                eventList = getEventListOrThrowException(events);
            }
            compilation.setEvents(new HashSet<>(eventList));
        }
        Compilation updatedCompilation = compilationRepository.save(compilation);
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (!updatedCompilation.getEvents().isEmpty()) {
            eventShortDtoList = eventService.getEventShortDtoListFromEventList(new ArrayList<>(updatedCompilation.getEvents()));
        }
        return compilationMapper.toCompilationDto(updatedCompilation, eventShortDtoList);
    }

    @Override
    public List<CompilationDto> getCompilationList(Boolean pinned, Integer from, Integer size) {
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        JPAQuery<Compilation> jpaQuery = jpaQueryFactory.selectFrom(QCompilation.compilation).offset(from).limit(size);
        if (pinned != null) {
            jpaQuery = jpaQuery.where(QCompilation.compilation.pinned.eq(pinned));
        }
        List<Compilation> compilationList = jpaQuery.fetch();
        if (!compilationList.isEmpty()) {
            for (Compilation compilation : compilationList) {
                List<EventShortDto> eventShortDtoList = new ArrayList<>();
                if (!compilation.getEvents().isEmpty()) {
                    eventShortDtoList.addAll(eventService.getEventShortDtoListFromEventList(new ArrayList<>(compilation.getEvents())));
                }
                CompilationDto compilationDto = compilationMapper.toCompilationDto(compilation, eventShortDtoList);
                compilationDtoList.add(compilationDto);
            }
        }
        return compilationDtoList;
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = getCompilationOrThrowException(compId);
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (!compilation.getEvents().isEmpty()) {
            eventShortDtoList = eventService.getEventShortDtoListFromEventList(new ArrayList<>(compilation.getEvents()));
        }
        return compilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }

    private Compilation getCompilationOrThrowException(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "Compilation with id=" + compId + " was not found"));
    }

    private List<Event> getEventListOrThrowException(List<Long> eventIdList) {
        List<Event> eventList = eventRepository.getEventListByIdList(eventIdList);
        if (eventIdList.size() != eventList.size()) {
            throw new NotFoundException("NOT_FOUND", "The required object was not found.",
                    "Events with ids=" + eventIdList + " was not found!");
        }
        return eventList;
    }
}
