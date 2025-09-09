package ch.cag.recruiting.auth;

import ch.cag.recruiting.security.JwtService;
import ch.cag.recruiting.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final UserService users;
  private final JwtService jwt;

  public AuthController(UserService users, JwtService jwt) {
    this.users = users;
    this.jwt = jwt;
  }

  @PostMapping("/login")
  public Map<String,String> login(@RequestBody LoginDto dto) {
    User u = users.authenticate(dto.getUsername(), dto.getPassword())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
    String token = jwt.issue(u.getUsername(), u.getRoles());
    return Map.of("token", token);
  }
}
