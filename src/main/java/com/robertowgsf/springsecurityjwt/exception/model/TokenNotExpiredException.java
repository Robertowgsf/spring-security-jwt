package com.robertowgsf.springsecurityjwt.exception.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Token not expired yet!")
public class TokenNotExpiredException extends RuntimeException {
}
