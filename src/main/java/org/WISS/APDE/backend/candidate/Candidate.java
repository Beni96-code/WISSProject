package ch.cag.recruiting.candidate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("candidates")
public class Candidate {
  @getters/setters
  @Id
  private String id;

  @getters/setters
  @NotBlank
  private String firstName;

  @getters/setters
  @NotBlank
  private String lastName;

  @getters/setters
  @Email
  @NotBlank
  private String email;

  @getters/setters
  private String phone;

  @getters/setters
  private List<String> skills = new ArrayList<>();

  @getters/setters
  private String location;

  @getters/setters
  private String linkedinUrl;

  @getters/setters
  @NotNull
  private Status status = Status.NEW;

  @getters/setters  
  private List<String> appliedJobs = new ArrayList<>();
  
  @getters/setters  
  @CreatedDate
  private Instant createdAt;

  @getters/setters
  @LastModifiedDate
  private Instant updatedAt;

  @getters/setters
  public enum Status { NEW, SCREENING, INTERVIEW, OFFER, REJECTED }

}
