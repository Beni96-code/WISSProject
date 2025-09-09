package org.wiss.apde.backend.candidate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
  Optional<Candidate> findByEmail(String email);
  Page<Candidate> findByStatus(Candidate.Status status, Pageable p);
  Page<Candidate> findBySkillsContaining(String skill, Pageable p);
  @Query("{ skills: ?0 }") Page<Candidate> findBySkillsContaining(String skill, Pageable p);
}
