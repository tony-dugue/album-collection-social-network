package com.tonydugue.album_collection.release;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReleaseRepository extends JpaRepository<Release, Integer> {
  @Query("""
            SELECT release
            FROM Release release
            WHERE release.archived = false
            AND release.shareable = true
            AND release.owner.id != :userId
            """)
  Page<Release> findAllDisplayableReleases(Pageable pageable, Integer userId);
}