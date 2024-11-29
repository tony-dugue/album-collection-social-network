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

  @Query("""
            SELECT history
            FROM ReleaseTransactionHistory history
            WHERE history.release.owner.id = :userId
            """)
  Page<ReleaseTransactionHistory> findAllReturnedReleases(Pageable pageable, Integer userId);

  @Query("""
            SELECT
            (COUNT (*) > 0) AS isBorrowed
            FROM ReleaseTransactionHistory releaseTransactionHistory
            WHERE releaseTransactionHistory.user.id = :userId
            AND releaseTransactionHistory.release.id = :releaseId
            AND releaseTransactionHistory.returnApproved = false
            """)
  boolean isAlreadyBorrowedByUser(Integer releaseId, Integer userId);
}