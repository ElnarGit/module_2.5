package com.elnar.module25.security;

import com.elnar.module25.entity.Status;
import com.elnar.module25.exception.UnauthorizedException;
import com.elnar.module25.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
  private final UserService userService;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
    return userService
        .getById(principal.getId())
        .filter(user -> user.getStatus().equals(Status.ACTIVE))
        .switchIfEmpty(Mono.error(new UnauthorizedException("User deleted")))
        .map(user -> authentication);
  }
}
