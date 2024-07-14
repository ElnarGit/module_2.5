package com.elnar.module25.service.impl;

import com.elnar.module25.entity.Status;
import com.elnar.module25.entity.User;
import com.elnar.module25.entity.UserRole;
import com.elnar.module25.repository.UserRepository;
import com.elnar.module25.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Mono<User> getById(Long id) {
    return userRepository
        .findById(id)
        .doOnNext(user -> log.debug("Found user by id: {}", id))
        .doOnError(error -> log.error("Error finding user by id: {}", id, error));
  }

  @Override
  public Mono<User> getByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .doOnNext(user -> log.debug("Found user by username: {}", username))
        .doOnError(error -> log.error("Error finding user by username: {}", username, error));
  }

  @Override
  public Flux<User> getAll() {
    return userRepository
        .findAll()
        .doOnNext(user -> log.debug("Retrieved user: {}", user.getUsername()))
        .doOnError(error -> log.error("Error retrieving users", error));
  }

  @Override
  public Mono<User> save(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository
        .save(user)
        .doOnNext(savedUser -> log.debug("Saved user with id: {}", savedUser.getId()))
        .doOnError(error -> log.error("Error saving user", error));
  }

  @Override
  public Mono<User> update(User user) {
    return userRepository
        .findById(user.getId())
        .flatMap(
            existingUser -> {
              // Кодируем пароль только если он изменился
              if (!existingUser.getPassword().equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
              }
              return userRepository.save(user);
            })
        .doOnNext(updatedUser -> log.debug("Updated user with id: {}", updatedUser.getId()))
        .doOnError(error -> log.error("Error updating user", error));
  }

  @Override
  public Mono<Void> deleteById(Long id) {
    return userRepository
        .deleteById(id)
        .doOnSuccess(result -> log.debug("Deleted user with id: {}", id))
        .doOnError(error -> log.error("Error deleting user with id: {}", id, error));
  }

  @Override
  public Mono<User> registerUser(User user) {
    return userRepository
        .save(
            user.toBuilder()
                .password(passwordEncoder.encode(user.getPassword()))
                .role(UserRole.USER)
                .status(Status.ACTIVE)
                .build())
        .doOnSuccess(u -> log.info("IN registerUser - user {} created", u))
        .doOnError(error -> log.error("Error registering user", error));
  }
}
