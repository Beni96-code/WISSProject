package org.wiss.apde.backend.candidate;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CandidateService {
  private final CandidateRepository repo;

  public CandidateService(CandidateRepository repo) {
    this.repo = repo;
  }

  public Candidate create(@Valid Candidate c) {
    repo.findByEmail(c.getEmail()).ifPresent(x -> {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email exists");
    });
    return repo.save(c);
  }

  public Candidate get(String id){
    return repo.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));
  }

  public Page<Candidate> list(Optional<String> status, Optional<String> skill, Pageable p){
    if(status.isPresent()) {
      Candidate.Status st = Candidate.Status.valueOf(status.get());
      return repo.findByStatus(st, p);
    }
    if(skill.isPresent()) {
      return repo.findBySkillsContaining(skill.get(), p);
    }
    return repo.findAll(p);
  }

  public Candidate update(String id, @Valid Candidate patch){
    Candidate cur = get(id);
    if(patch.getFirstName()!=null) cur.setFirstName(patch.getFirstName());
    if(patch.getLastName()!=null)  cur.setLastName(patch.getLastName());
    if(patch.getStatus()!=null)    cur.setStatus(patch.getStatus());
    if(patch.getSkills()!=null)    cur.setSkills(patch.getSkills());
    if(patch.getLocation()!=null)  cur.setLocation(patch.getLocation());
    if(patch.getLinkedinUrl()!=null) cur.setLinkedinUrl(patch.getLinkedinUrl());
    if(patch.getPhone()!=null)     cur.setPhone(patch.getPhone());
    return repo.save(cur);
  }

  public void delete(String id){
    if (!repo.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found");
    }
    repo.deleteById(id);
  }
}
