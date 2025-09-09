package ch.cag.recruiting.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtService {

  private final Key key;
  private final Duration ttl;

  public JwtService(
      @Value("${app.security.jwt.secret}") String secret,
      @Value("${app.security.jwt.ttl:PT2H}") Duration ttl) {
    // Für HS256 muss der Secret-Key ausreichend lang sein
    if (secret == null || secret.length() < 32) {
      secret = String.format("%-32s", Objects.toString(secret, "dev-secret")).replace(' ', 'x');
    }
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.ttl = ttl;
  }

  public String issue(String username, Collection<String> roles) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + ttl.toMillis());
    return Jwts.builder()
      .setSubject(username)
      .claim("roles", roles)
      .setIssuedAt(now)
      .setExpiration(exp)
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public Authentication toAuthentication(String token) {
    try {
      Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      String username = jws.getBody().getSubject();
      @SuppressWarnings("unchecked")
      List<String> roles = (List<String>) jws.getBody().getOrDefault("roles", List.of());
      var authorities = roles.stream()
          .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
      return new UsernamePasswordAuthenticationToken(username, null, authorities);
    } catch (JwtException e) {
      return null; // ungültig/abgelaufen
    }
  }
}
