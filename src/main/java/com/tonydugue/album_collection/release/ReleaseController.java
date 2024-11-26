package com.tonydugue.album_collection.release;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("releases")
@RequiredArgsConstructor
@Tag(name = "Release")
public class ReleaseController {

  private final ReleaseService service;

  @PostMapping
  public ResponseEntity<Integer> saveRelease(
          @Valid @RequestBody ReleaseRequest request,
          Authentication connectedUser
  ) {
    return ResponseEntity.ok(service.save(request, connectedUser));
  }
}