package com.robertowgsf.springsecurityjwt.controller;

import com.robertowgsf.springsecurityjwt.exception.model.TokenNotExpiredException;
import com.robertowgsf.springsecurityjwt.model.entity.Token;
import com.robertowgsf.springsecurityjwt.model.entity.User;
import com.robertowgsf.springsecurityjwt.exception.model.RefreshTokenExpiredException;
import com.robertowgsf.springsecurityjwt.exception.model.RefreshTokenNotFoundException;
import com.robertowgsf.springsecurityjwt.exception.model.UserNotFoundException;
import com.robertowgsf.springsecurityjwt.model.request.CreateAccountRequest;
import com.robertowgsf.springsecurityjwt.model.request.LoginRequest;
import com.robertowgsf.springsecurityjwt.model.request.RefreshTokenRequest;
import com.robertowgsf.springsecurityjwt.model.response.LoginResponse;
import com.robertowgsf.springsecurityjwt.model.response.RefreshTokenResponse;
import com.robertowgsf.springsecurityjwt.repository.TokenRepository;
import com.robertowgsf.springsecurityjwt.repository.UserRepository;
import com.robertowgsf.springsecurityjwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("authentication")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        var principal = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = (User) principal.getPrincipal();
        var jwt = jwtUtil.generateToken(user);
        var token = tokenRepository.save(new Token(jwt, jwtUtil.getIssuedAt(jwt)));

        return new LoginResponse(token.getJwt(), token.getRefreshToken());
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody CreateAccountRequest request) {
        var password = passwordEncoder.encode(request.getPassword());
        var authorities = "ROLE_" + request.getProfile();
        var user = new User(null, request.getUsername(), request.getEmail(), password, authorities, true, true, true, true);
        userRepository.save(user);
    }

    @PostMapping("refresh")
    public RefreshTokenResponse refresh(@RequestBody RefreshTokenRequest request, @RequestHeader String authorization) {
        var token = tokenRepository.findByRefreshTokenAndJwt(request.getRefreshToken(), authorization.substring("Bearer ".length()));

        if (token.isEmpty()) {
            throw new RefreshTokenNotFoundException();
        } else if (token.get().isExpired()) {
            throw new RefreshTokenExpiredException();
        } else if (jwtUtil.getExpirationDate(authorization).isAfter(Instant.now())) {
            throw new TokenNotExpiredException();
        } else {
            tokenRepository.delete(token.get());
        }

        var username = jwtUtil.getUsernameFromExpiredToken(authorization);
        var user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        var jwt = jwtUtil.generateToken(user.get());
        var newToken = tokenRepository.save(new Token(jwt, jwtUtil.getIssuedAt(jwt)));

        return new RefreshTokenResponse(newToken.getJwt(), newToken.getRefreshToken());
    }
}
