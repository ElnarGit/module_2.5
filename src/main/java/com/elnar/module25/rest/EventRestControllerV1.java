package com.elnar.module25.rest;

import com.elnar.module25.dto.EventDto;
import com.elnar.module25.entity.Event;
import com.elnar.module25.mapper.EventMapper;
import com.elnar.module25.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventRestControllerV1 {
  private final EventService eventService;
  private final EventMapper eventMapper;

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  public Mono<ResponseEntity<EventDto>> getEventById(@PathVariable("id") Long id) {
    return eventService
        .getById(id)
        .map(eventMapper::map)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping()
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  public Flux<EventDto> getAllEvents() {
    return eventService.getAll().map(eventMapper::map);
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public Mono<ResponseEntity<EventDto>> saveEvent(@Valid @RequestBody EventDto eventDto) {
    Event event = eventMapper.map(eventDto);
    return eventService
        .save(event)
        .map(eventMapper::map)
        .map(savedEvent -> ResponseEntity.status(HttpStatus.CREATED).body(savedEvent));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  public Mono<ResponseEntity<EventDto>> updateEvent(
      @PathVariable("id") Long id, @Valid @RequestBody EventDto eventDto) {
    return eventService
        .getById(id)
        .flatMap(
            existingEvent -> {
              Event eventToUpdate = eventMapper.map(eventDto);
              eventToUpdate.setId(existingEvent.getId());
              return eventService
                  .update(eventToUpdate)
                  .map(eventMapper::map)
                  .map(ResponseEntity::ok);
            })
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  public Mono<ResponseEntity<Void>> deleteEventById(@PathVariable("id") Long id) {
    return eventService
        .getById(id)
        .flatMap(
            event ->
                eventService
                    .deleteById(event.getId())
                    .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
        .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
  }
}
