package com.tonydugue.album_collection.release;

import org.springframework.data.jpa.domain.Specification;

public class ReleaseSpecification {
  public static Specification<Release> withOwnerId(Integer ownerId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
  }
}