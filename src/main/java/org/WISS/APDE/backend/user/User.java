package ch.cag.recruiting.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("users")
public class User {
  @getters/setters
  @Id
  private String id;

  @getters/setters
  @Indexed(unique = true)
  private String username;

  @getters/setters
  private String passwordHash;

  @getters/setters
  private List<String> roles = new ArrayList<>();
}
