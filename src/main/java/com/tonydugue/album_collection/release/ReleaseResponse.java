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
public class ReleaseResponse {
  private Integer id;
  private String title;
  private String artist;
  private String reference;
  private String synopsis;
  private String owner;
  private byte[] cover;
  private double rate;
  private boolean archived;
  private boolean shareable;
}