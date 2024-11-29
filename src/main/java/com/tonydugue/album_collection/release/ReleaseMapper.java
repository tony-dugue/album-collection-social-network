package com.tonydugue.album_collection.release;

import com.tonydugue.album_collection.history.ReleaseTransactionHistory;
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

  public BorrowedReleaseResponse toBorrowedReleaseResponse(ReleaseTransactionHistory history) {
    return BorrowedReleaseResponse.builder()
            .id(history.getRelease().getId())
            .title(history.getRelease().getTitle())
            .artist(history.getRelease().getArtist())
            .reference(history.getRelease().getReference())
            .rate(history.getRelease().getRate())
            .returned((history.isReturned()))
            .returnApproved(history.isReturnApproved())
            .build();
  }
}