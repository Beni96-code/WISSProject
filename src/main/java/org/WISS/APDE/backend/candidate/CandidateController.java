package ch.cag.recruiting.candidate;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

  private final CandidateService service;

  public CandidateController(CandidateService service) {
    this.service = service;
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('HR','ADMIN')")
  public Candidate create(@Valid @RequestBody Candidate c){
    return service.create(c);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('HR','AGENCY','ADMIN')")
  public Candidate get(@PathVariable String id){
    return service.get(id);
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('HR','AGENCY','ADMIN')")
  public Page<Candidate> list(@RequestParam Optional<String> status,
                              @RequestParam Optional<String> skill,
                              @PageableDefault(size = 20) Pageable pageable){
    return service.list(status, skill, pageable);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAnyRole('HR','ADMIN')")
  public Candidate update(@PathVariable String id, @RequestBody Candidate patch){
    return service.update(id, patch);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public void delete(@PathVariable String id){
    service.delete(id);
  }
}
