package com.tonydugue.album_collection.release;

import org.springframework.stereotype.Service;

@Service
public class ReleaseMapper {
  public Release toRelease(ReleaseRequest request) {
    return Release.builder()
            .id(request.id())
            .title(request.title())
            .reference(request.reference())
            .artist(request.artist())
            .synopsis(request.synopsis())
            .archived(false)
            .shareable(request.shareable())
            .build();
  }

  public ReleaseResponse toReleaseResponse(Release release) {
    return ReleaseResponse.builder()
            .id(release.getId())
            .title(release.getTitle())
            .artist(release.getArtist())
            .reference(release.getReference())
            .synopsis(release.getSynopsis())
            .rate(release.getRate())
            .archived(release.isArchived())
            .shareable(release.isShareable())
            .owner(release.getOwner().fullName())
            // todo implement this later
            //.cover()
            .build();
  }
}