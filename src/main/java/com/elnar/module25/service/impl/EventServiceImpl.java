package com.elnar.module25.service.impl;

import com.elnar.module25.entity.Event;
import com.elnar.module25.repository.EventRepository;
import com.elnar.module25.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
  private final EventRepository eventRepository;

  @Override
  public Mono<Event> getById(Long id) {
    return eventRepository
        .findById(id)
        .doOnNext(user -> log.debug("Found event by id: {}", id))
        .doOnError(error -> log.error("Error finding event by id: {}", id, error));
  }

  @Override
  public Flux<Event> getAll() {
    return eventRepository
        .findAll()
        .doOnNext(event -> log.debug("Retrieved event: {}", event.getId()))
        .doOnError(error -> log.error("Error retrieving events", error));
  }

  @Override
  public Mono<Event> save(Event event) {
    return eventRepository
        .save(event)
        .doOnNext(savedEvent -> log.debug("Saved event with id: {}", savedEvent.getId()))
        .doOnError(error -> log.error("Error saving event", error));
  }

  @Override
  public Mono<Event> update(Event event) {
    return eventRepository
        .save(event)
        .doOnNext(updatedEvent -> log.debug("Updated event with id: {}", updatedEvent.getId()))
        .doOnError(error -> log.error("Error updating event", error));
  }

  @Override
  public Mono<Void> deleteById(Long id) {
    return eventRepository
        .deleteById(id)
        .doOnSuccess(result -> log.debug("Deleted event with id: {}", id))
        .doOnError(error -> log.error("Error deleting event with id: {}", id, error));
  }
}
