package com.elnar.module25.repository;

import com.elnar.module25.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, Long> {
	Mono<User> findByUsername(String username);
}
