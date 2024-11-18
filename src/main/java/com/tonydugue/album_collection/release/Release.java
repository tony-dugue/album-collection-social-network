package com.tonydugue.album_collection.release;

import com.tonydugue.album_collection.common.BaseEntity;
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
public class Release extends BaseEntity {

  private String title;
  private String artist;
  private String reference;
  private String synopsis;
  private String releaseCover;
  private boolean archived;
  private boolean shareable;
}