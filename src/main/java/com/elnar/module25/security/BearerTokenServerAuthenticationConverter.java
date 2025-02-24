package com.elnar.module25.security;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BearerTokenServerAuthenticationConverter implements ServerAuthenticationConverter {
  private final JwtHandler jwtHandler;
  private static final String BEARER_PREFIX = "Bearer ";
  private static final Function<String, Mono<String>> getBearerValue =
      authValue -> Mono.justOrEmpty(authValue.substring(BEARER_PREFIX.length()));

  @Override
  public Mono<Authentication> convert(ServerWebExchange exchange) {
    return extractHeader(exchange)
        .flatMap(getBearerValue)
        .flatMap(jwtHandler::check)
        .flatMap(UserAuthenticationBearer::create);
  }

  private Mono<String> extractHeader(ServerWebExchange exchange) {
    return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
  }
}
