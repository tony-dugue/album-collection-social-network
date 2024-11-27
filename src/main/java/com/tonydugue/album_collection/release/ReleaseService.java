package com.tonydugue.album_collection.release;

import com.tonydugue.album_collection.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
}