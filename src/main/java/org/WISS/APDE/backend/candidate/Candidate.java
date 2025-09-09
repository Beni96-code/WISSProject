package org.wiss.apde.backend.candidate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
import lombok.Getter;
import lombok.Setter;


@Document("candidates")
@Getter
@Setter
public class Candidate {
  @Id
  private String id;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @Email
  @NotBlank
  private String email;

  private String phone;

  private List<String> skills = new ArrayList<>();

  private String location;

  private String linkedinUrl;

  @NotNull
  private Status status = Status.NEW;

  private List<String> appliedJobs = new ArrayList<>();
  
  @CreatedDate
  private Instant createdAt;

  @LastModifiedDate
  private Instant updatedAt;

  public enum Status { NEW, SCREENING, INTERVIEW, OFFER, REJECTED }

}


