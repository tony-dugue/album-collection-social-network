package com.tonydugue.album_collection.release;

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
}