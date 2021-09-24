package com.robertowgsf.springsecurityjwt.model.entity;

import com.robertowgsf.springsecurityjwt.util.JwtUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID refreshToken;
    private String jwt;
    private Instant createdAt;

    public Token(String jwt, Date issuedAt) {
        this.jwt = jwt;
        this.createdAt = issuedAt.toInstant();
    }

    public boolean isExpired() {
        return createdAt.plusSeconds(JwtUtil.REFRESH_TOKEN_VALIDITY).isBefore(Instant.now());
    }
}
