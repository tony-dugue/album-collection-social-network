package com.tonydugue.album_collection.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReleasetransactionHistoryRepository extends JpaRepository<ReleaseTransactionHistory, Integer> {

  @Query("""
            SELECT history
            FROM ReleaseTransactionHistory history
            WHERE history.user.id = :userId
            """)
  Page<ReleaseTransactionHistory> findAllBorrowedReleases(Pageable pageable, Integer userId);
}