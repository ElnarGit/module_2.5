package com.elnar.module25.service;

import com.elnar.module25.entity.User;
import reactor.core.publisher.Mono;

public interface UserService extends GenericService<User, Long> {
	Mono<User> getByUsername(String username);
	
	Mono<User> registerUser(User user);
}
