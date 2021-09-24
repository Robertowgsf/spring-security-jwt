package com.robertowgsf.springsecurityjwt.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String jwt;
    private UUID refreshToken;
}
