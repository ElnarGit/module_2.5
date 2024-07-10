package com.elnar.module25.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenericService<T, ID> {
  Mono<T> getById(ID id);

  Flux<T> getAll();

  Mono<T> save(T entity);

  Mono<T> update(T entity);

  Mono<Void> deleteById(ID id);
}
