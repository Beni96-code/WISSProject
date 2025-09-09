package ch.cag.recruiting.auth;

import ch.cag.recruiting.user.User;
import ch.cag.recruiting.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  private final UserRepository repo;
  private final PasswordEncoder encoder;

  public UserService(UserRepository repo, PasswordEncoder encoder) {
    this.repo = repo;
    this.encoder = encoder;
  }

  public Optional<User> authenticate(String username, String rawPassword) {
    return repo.findByUsername(username)
      .filter(u -> encoder.matches(rawPassword, u.getPasswordHash()));
  }
}
