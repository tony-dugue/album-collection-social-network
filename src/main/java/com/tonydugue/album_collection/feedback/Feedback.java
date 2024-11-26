package com.tonydugue.album_collection.feedback;

import com.tonydugue.album_collection.common.BaseEntity;
import com.tonydugue.album_collection.release.Release;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {

  private Double note;
  private String comment;

  @ManyToOne
  @JoinColumn(name = "release_id")
  private Release release;
}