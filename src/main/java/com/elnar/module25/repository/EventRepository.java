package com.elnar.module25.repository;

import com.elnar.module25.entity.Event;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface EventRepository extends R2dbcRepository<Event, Long> {}
