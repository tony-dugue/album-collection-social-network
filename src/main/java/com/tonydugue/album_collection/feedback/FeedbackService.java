package com.tonydugue.album_collection.feedback;

import com.tonydugue.album_collection.exception.OperationNotPermittedException;
import com.tonydugue.album_collection.release.Release;
import com.tonydugue.album_collection.release.ReleaseRepository;
import com.tonydugue.album_collection.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

  private final ReleaseRepository releaseRepository;
  private final FeedbackMapper feedbackMapper;
  private final FeedbackRepository feedbackRepository;

  public Integer save(FeedbackRequest request, Authentication connectedUser) {
    Release release = releaseRepository.findById(request.releaseId())
            .orElseThrow(() -> new EntityNotFoundException("No release found with ID:: " + request.releaseId()));

    if (release.isArchived() || !release.isShareable()) {
      throw new OperationNotPermittedException("You cannot give a feedback for and archived or not shareable release");
    }

    User user = ((User) connectedUser.getPrincipal());

    if (Objects.equals(release.getCreatedBy(), user.getId())) {
      throw new OperationNotPermittedException("You cannot give feedback to your own release");
    }
    Feedback feedback = feedbackMapper.toFeedback(request);
    return feedbackRepository.save(feedback).getId();
  }
}