package app.auth.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.auth.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${app.app.jwtSecret}")
  private String jwtSecret; // Секретный ключ для создания и проверки JWT-токенов

  @Value("${app.app.jwtExpirationMs}")
  private int jwtExpirationMs; // Время жизни JWT-токена в миллисекундах

  // Метод для генерации JWT-токена на основе аутентификации пользователя
  public String generateJwtToken(Authentication authentication) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    Map<String, Object> claims = new HashMap<>();
    claims.put("user_id", userPrincipal.getId()); // добавляем идентификатор пользователя (user_id)

    return Jwts.builder()
            .setSubject(userPrincipal.getUsername())
            .addClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
  }

  // Метод для получения ключа из секретного ключа в формате Base64
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  // Метод для извлечения имени пользователя из JWT-токена
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
            .parseClaimsJws(token).getBody().getSubject();
  }

  // Метод для извлечения идентификатора пользователя из JWT-токена
  public Long getUserIdFromJwtToken(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    return claims.get("user_id", Long.class);
  }

  // Метод для проверки действительности JWT-токена и возврата результата проверки
  public JwtValidationResult validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return new JwtValidationResult(true); // Токен действителен
    } catch (MalformedJwtException e) {
      return new JwtValidationResult("Invalid JWT Token");
    } catch (ExpiredJwtException e) {
      return new JwtValidationResult("Expired JWT Token");
    } catch (UnsupportedJwtException e) {
      return new JwtValidationResult("Unsupported JWT Token");
    } catch (IllegalArgumentException e) {
      return new JwtValidationResult("Empty JWT Claim");
    }
  }
}

