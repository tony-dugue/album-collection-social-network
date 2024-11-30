package com.tonydugue.album_collection.feedback;

import com.tonydugue.album_collection.common.PageResponse;
import com.tonydugue.album_collection.exception.OperationNotPermittedException;
import com.tonydugue.album_collection.release.Release;
import com.tonydugue.album_collection.release.ReleaseRepository;
import com.tonydugue.album_collection.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

  @Transactional
  public PageResponse<FeedbackResponse> findAllFeedbacksByRelease(Integer releaseId, int page, int size, Authentication connectedUser) {
    Pageable pageable = PageRequest.of(page, size);
    User user = ((User) connectedUser.getPrincipal());
    Page<Feedback> feedbacks = feedbackRepository.findAllByReleaseId(releaseId, pageable);
    List<FeedbackResponse> feedbackResponses = feedbacks.stream()
            .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
            .toList();
    return new PageResponse<>(
            feedbackResponses,
            feedbacks.getNumber(),
            feedbacks.getSize(),
            feedbacks.getTotalElements(),
            feedbacks.getTotalPages(),
            feedbacks.isFirst(),
            feedbacks.isLast()
    );
  }
}