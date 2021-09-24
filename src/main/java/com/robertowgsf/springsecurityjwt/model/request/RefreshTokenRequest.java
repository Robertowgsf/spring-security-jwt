package com.robertowgsf.springsecurityjwt.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RefreshTokenRequest {
    private UUID refreshToken;
}
