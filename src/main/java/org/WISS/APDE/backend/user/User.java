package org.wiss.apde.backend.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Document("users")
@Getter
@Setter
public class User {
  @Id
  private String id;

  @Indexed(unique = true)
  private String username;

  private String passwordHash;

  private List<String> roles = new ArrayList<>();
}
