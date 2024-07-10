package com.elnar.module25.exception;

public class AuthException extends ApiException {

  public AuthException(String message, String errorCode) {
    super(message, errorCode);
  }
}
