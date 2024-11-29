package com.tonydugue.album_collection.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

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
            FROM ReleaseTransactionHistory transaction
            WHERE transaction.user.id = :userId
            AND transaction.release.id = :releaseId
            AND transaction.returnApproved = false
            """)
  boolean isAlreadyBorrowedByUser(Integer releaseId, Integer userId);

  @Query("""
            SELECT transaction
            FROM ReleaseTransactionHistory transaction
            WHERE transaction.user.id = :userId
            AND transaction.release.id = :releaseId
            AND transaction.returned = false
            AND transaction.returnApproved = false
            """)
  Optional<ReleaseTransactionHistory> findByReleaseIdAndUserId(Integer releaseId, Integer userId);

  @Query("""
            SELECT transaction
            FROM ReleaseTransactionHistory transaction
            WHERE transaction.release.owner.id = :userId
            AND transaction.release.id = :releaseId
            AND transaction.returned = true
            AND transaction.returnApproved = false
            """)
  Optional<ReleaseTransactionHistory> findByReleaseIdAndOwnerId(Integer releaseId, Integer userId);
}