package com.tonydugue.album_collection.release;

import com.tonydugue.album_collection.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

  @GetMapping("{release-id}")
  public ResponseEntity<ReleaseResponse> findReleaseById(
          @PathVariable("release-id") Integer releaseId
  ) {
    return ResponseEntity.ok(service.findById(releaseId));
  }

  @GetMapping
  public ResponseEntity<PageResponse<ReleaseResponse>> findAllReleases(
          @RequestParam(name = "page", defaultValue = "0", required = false) int page,
          @RequestParam(name = "size", defaultValue = "10", required = false) int size,
          Authentication connectedUser
  ) {
    return ResponseEntity.ok(service.findAllReleases(page, size, connectedUser));
  }

  @GetMapping("/owner")
  public ResponseEntity<PageResponse<ReleaseResponse>> findAllReleasesByOwner(
          @RequestParam(name = "page", defaultValue = "0", required = false) int page,
          @RequestParam(name = "size", defaultValue = "10", required = false) int size,
          Authentication connectedUser
  ) {
    return ResponseEntity.ok(service.findAllReleasesByOwner(page, size, connectedUser));
  }

  @GetMapping("/borrowed")
  public ResponseEntity<PageResponse<BorrowedReleaseResponse>> findAllBorrowedReleases(
          @RequestParam(name = "page", defaultValue = "0", required = false) int page,
          @RequestParam(name = "size", defaultValue = "10", required = false) int size,
          Authentication connectedUser
  ) {
    return ResponseEntity.ok(service.findAllBorrowedReleases(page, size, connectedUser));
  }

  @GetMapping("/returned")
  public ResponseEntity<PageResponse<BorrowedReleaseResponse>> findAllReturnedReleases(
          @RequestParam(name = "page", defaultValue = "0", required = false) int page,
          @RequestParam(name = "size", defaultValue = "10", required = false) int size,
          Authentication connectedUser
  ) {
    return ResponseEntity.ok(service.findAllReturnedReleases(page, size, connectedUser));
  }

  @PatchMapping("/shareable/{release-id}")
  public ResponseEntity<Integer> updateShareableStatus(
          @PathVariable("release-id") Integer releaseId,
          Authentication connectedUser
  ) {
    return ResponseEntity.ok(service.updateShareableStatus(releaseId, connectedUser));
  }

  @PatchMapping("/archived/{release-id}")
  public ResponseEntity<Integer> updateArchivedStatus(
          @PathVariable("release-id") Integer releaseId,
          Authentication connectedUser
  ) {
    return ResponseEntity.ok(service.updateArchivedStatus(releaseId, connectedUser));
  }

  @PostMapping("borrow/{release-id}")
  public ResponseEntity<Integer> borrowRelease(
          @PathVariable("release-id") Integer releaseId,
          Authentication connectedUser
  ) {
    return ResponseEntity.ok(service.borrowRelease(releaseId, connectedUser));
  }

  @PatchMapping("borrow/return/{release-id}")
  public ResponseEntity<Integer> returnBorrowRelease(
          @PathVariable("release-id") Integer releaseId,
          Authentication connectedUser
  ) {
    return ResponseEntity.ok(service.returnBorrowedRelease(releaseId, connectedUser));
  }

  @PatchMapping("borrow/return/approve/{release-id}")
  public ResponseEntity<Integer> approveReturnBorrowRelease(
          @PathVariable("release-id") Integer releaseId,
          Authentication connectedUser
  ) {
    return ResponseEntity.ok(service.approveReturnBorrowedRelease(releaseId, connectedUser));
  }

  @PostMapping(value = "/cover/{release-id}", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadReleaseCoverPicture(
          @PathVariable("release-id") Integer releaseId,
          @Parameter()
          @RequestPart("file") MultipartFile file,
          Authentication connectedUser
  ) {
    service.uploadReleaseCoverPicture(file, connectedUser, releaseId);
    return ResponseEntity.accepted().build();
  }
}