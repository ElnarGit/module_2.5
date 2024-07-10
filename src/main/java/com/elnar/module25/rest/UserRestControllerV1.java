package com.elnar.module25.rest;

import com.elnar.module25.dto.UserDto;
import com.elnar.module25.entity.User;
import com.elnar.module25.mapper.UserMapper;
import com.elnar.module25.service.UserService;
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
@RequestMapping("/api/v1/users")
public class UserRestControllerV1 {
  private final UserService userService;
  private final UserMapper userMapper;

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('USER', 'MODERATOR', 'ADMIN')")
  public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable("id") Long id) {
    return userService
        .getById(id)
        .map(userMapper::map)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping("/by-username/{username}")
  @PreAuthorize("hasAnyAuthority('USER', 'MODERATOR', 'ADMIN')")
  public Mono<ResponseEntity<UserDto>> getUserByUsername(
      @PathVariable("username") String username) {
    return userService
        .getByUsername(username)
        .map(userMapper::map)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  public Flux<UserDto> getAllUsers() {
    return userService.getAll().map(userMapper::map);
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public Mono<ResponseEntity<UserDto>> saveUser(@Valid @RequestBody UserDto userDto) {
    User user = userMapper.map(userDto);
    return userService
        .save(user)
        .map(userMapper::map)
        .map(savedUser -> ResponseEntity.status(HttpStatus.CREATED).body(savedUser));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public Mono<ResponseEntity<UserDto>> updateUser(
      @PathVariable("id") Long id, @Valid @RequestBody UserDto userDto) {
    return userService
        .getById(id)
        .flatMap(
            existingUser -> {
              User userToUpdate = userMapper.map(userDto);
              userToUpdate.setId(existingUser.getId());
              return userService.update(userToUpdate).map(userMapper::map).map(ResponseEntity::ok);
            })
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable("id") Long id) {
    return userService
        .getById(id)
        .flatMap(
            user ->
                userService
                    .deleteById(user.getId())
                    .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
        .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
  }
}
