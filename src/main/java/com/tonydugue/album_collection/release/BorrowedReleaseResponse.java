package com.tonydugue.album_collection.release;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedReleaseResponse {
  private Integer id;
  private String title;
  private String artist;
  private String reference;
  private double rate;
  private boolean returned;
  private boolean returnApproved;
}