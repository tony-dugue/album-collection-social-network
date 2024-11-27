package com.tonydugue.album_collection.release;

import com.tonydugue.album_collection.common.BaseEntity;
import com.tonydugue.album_collection.feedback.Feedback;
import com.tonydugue.album_collection.history.ReleaseTransactionHistory;
import com.tonydugue.album_collection.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Release extends BaseEntity {

  private String title;
  private String artist;
  private String reference;
  private String synopsis;
  private String releaseCover;
  private boolean archived;
  private boolean shareable;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private User owner;

  @OneToMany(mappedBy = "release")
  private List<Feedback> feedbacks;

  @OneToMany(mappedBy = "release")
  private List<ReleaseTransactionHistory> histories;

  @Transient
  public double getRate() {
    if (feedbacks == null || feedbacks.isEmpty()) {
      return 0.0;
    }
    var rate = this.feedbacks.stream()
            .mapToDouble(Feedback::getNote)
            .average()
            .orElse(0.0);

    // Return 4.0 if roundedRate is less than 4.5, otherwise return 4.5
    return Math.round(rate * 10.0) / 10.0;
  }
}