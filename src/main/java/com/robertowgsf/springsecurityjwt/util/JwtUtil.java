package com.robertowgsf.springsecurityjwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final long JWT_TOKEN_VALIDITY = 10; // minutes
    public static final long REFRESH_TOKEN_VALIDITY = 15770000; // 6 months
    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Date getIssuedAt(String token) {
        return getClaim(token, Claims::getIssuedAt);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList()));
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        var expiration = LocalDateTime.now().plusMinutes(JWT_TOKEN_VALIDITY);

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key).compact();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String jwt) {
        var authorities = (List<String>) getAllClaims(jwt).get("roles");
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getUsernameFromExpiredToken(String token) {
        var decoded = decode(token);
        var username = decoded.substring(decoded.indexOf("sub"), decoded.indexOf("\",\"iat\":")).replaceAll("\"", "").replaceAll("sub:", "");
        return username;
    }

    public Instant getExpirationDate(String token) {
        var decoded = decode(token);
        var pattern = Pattern.compile("exp\":[0-9].*[0-9]");
        var matcher = pattern.matcher(decoded);
        matcher.find();
        var exp = matcher.group(0).replace("exp\":", "");
        return Instant.ofEpochSecond(Long.parseLong(exp));
    }

    private String decode(String token) {
        var encodedPayload = token.split("\\.")[1].replace('-', '+').replace('_', '/');
        return new String(Base64.getDecoder().decode(encodedPayload));
    }
}
