package com.tonydugue.album_collection.feedback;

import com.tonydugue.album_collection.release.Release;
import org.springframework.stereotype.Service;

@Service
public class FeedbackMapper {

  public Feedback toFeedback(FeedbackRequest request) {
    return Feedback.builder()
            .note(request.note())
            .comment(request.comment())
            .release(Release.builder()
                    .id(request.releaseId())
                    .shareable(false) // Not required and has no impact :: just to satisfy lombok
                    .archived(false) // Not required and has no impact :: just to satisfy lombok
                    .build()
            )
            .build();
  }
}