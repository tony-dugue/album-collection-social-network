package com.tonydugue.album_collection.history;

import com.tonydugue.album_collection.common.BaseEntity;
import com.tonydugue.album_collection.release.Release;
import com.tonydugue.album_collection.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class ReleaseTransactionHistory extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
  @ManyToOne
  @JoinColumn(name = "release_id")
  private Release release;

  private boolean returned;
  private boolean returnApproved;
}