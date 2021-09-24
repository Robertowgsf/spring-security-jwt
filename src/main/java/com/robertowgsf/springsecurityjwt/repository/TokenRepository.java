package com.robertowgsf.springsecurityjwt.repository;

import com.robertowgsf.springsecurityjwt.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findByRefreshTokenAndJwt(UUID refreshToken, String jwt);
}
