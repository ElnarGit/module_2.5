package com.elnar.module25.exception;

public class UnauthorizedException extends ApiException {

  public UnauthorizedException(String message) {
    super(message, "ELNAR_UNAUTHORIZED");
  }
}
