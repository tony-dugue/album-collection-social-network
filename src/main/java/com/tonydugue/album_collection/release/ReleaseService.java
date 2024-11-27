package com.tonydugue.album_collection.release;

import com.tonydugue.album_collection.common.PageResponse;
import com.tonydugue.album_collection.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReleaseService {

  private final ReleaseRepository releaseRepository;
  private final ReleaseMapper releaseMapper;

  public Integer save(ReleaseRequest request, Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Release release = releaseMapper.toRelease(request);
    release.setOwner(user);
    return releaseRepository.save(release).getId();
  }

  public ReleaseResponse findById(Integer releaseId) {
    return releaseRepository.findById(releaseId)
            .map(releaseMapper::toReleaseResponse)
            .orElseThrow(() -> new EntityNotFoundException("No release found with ID:: " + releaseId));
  }

  public PageResponse<ReleaseResponse> findAllReleases(int page, int size, Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<Release> releases = releaseRepository.findAllDisplayableReleases(pageable, user.getId());
    List<ReleaseResponse> releasesResponse = releases.stream()
            .map(releaseMapper::toReleaseResponse)
            .toList();
    return new PageResponse<>(
            releasesResponse,
            releases.getNumber(),
            releases.getSize(),
            releases.getTotalElements(),
            releases.getTotalPages(),
            releases.isFirst(),
            releases.isLast()
    );
  }

  public PageResponse<ReleaseResponse> findAllReleasesByOwner(int page, int size, Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<Release> releases = releaseRepository.findAll(ReleaseSpecification.withOwnerId(user.getId()), pageable);
    List<ReleaseResponse> releasesResponse = releases.stream()
            .map(releaseMapper::toReleaseResponse)
            .toList();
    return new PageResponse<>(
            releasesResponse,
            releases.getNumber(),
            releases.getSize(),
            releases.getTotalElements(),
            releases.getTotalPages(),
            releases.isFirst(),
            releases.isLast()
    );
  }
}