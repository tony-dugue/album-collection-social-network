package com.tonydugue.album_collection.release;

import com.tonydugue.album_collection.common.PageResponse;
import com.tonydugue.album_collection.exception.OperationNotPermittedException;
import com.tonydugue.album_collection.history.ReleaseTransactionHistory;
import com.tonydugue.album_collection.history.ReleasetransactionHistoryRepository;
import com.tonydugue.album_collection.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReleaseService {

  private final ReleaseRepository releaseRepository;
  private final ReleasetransactionHistoryRepository transactionHistoryRepository;
  private final ReleaseMapper releaseMapper;

  public Integer save(ReleaseRequest request, Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Release release = releaseMapper.toRelease(request);
    release.setOwner(user);
    return releaseRepository.save(release).getId();
  }

  public ReleaseResponse findById(Integer releaseId) {
    return releaseRepository.findById(releaseId)
            .map(releaseMapper::toReleaseResponse)
            .orElseThrow(() -> new EntityNotFoundException("No release found with ID:: " + releaseId));
  }

  public PageResponse<ReleaseResponse> findAllReleases(int page, int size, Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<Release> releases = releaseRepository.findAllDisplayableReleases(pageable, user.getId());
    List<ReleaseResponse> releasesResponse = releases.stream()
            .map(releaseMapper::toReleaseResponse)
            .toList();
    return new PageResponse<>(
            releasesResponse,
            releases.getNumber(),
            releases.getSize(),
            releases.getTotalElements(),
            releases.getTotalPages(),
            releases.isFirst(),
            releases.isLast()
    );
  }

  public PageResponse<ReleaseResponse> findAllReleasesByOwner(int page, int size, Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<Release> releases = releaseRepository.findAll(ReleaseSpecification.withOwnerId(user.getId()), pageable);
    List<ReleaseResponse> releasesResponse = releases.stream()
            .map(releaseMapper::toReleaseResponse)
            .toList();
    return new PageResponse<>(
            releasesResponse,
            releases.getNumber(),
            releases.getSize(),
            releases.getTotalElements(),
            releases.getTotalPages(),
            releases.isFirst(),
            releases.isLast()
    );
  }

  public PageResponse<BorrowedReleaseResponse> findAllBorrowedReleases(int page, int size, Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<ReleaseTransactionHistory> allBorrowedReleases = transactionHistoryRepository.findAllBorrowedReleases(pageable, user.getId());
    List<BorrowedReleaseResponse> releasesResponse = allBorrowedReleases.stream()
            .map(releaseMapper::toBorrowedReleaseResponse)
            .toList();
    return new PageResponse<>(
            releasesResponse,
            allBorrowedReleases.getNumber(),
            allBorrowedReleases.getSize(),
            allBorrowedReleases.getTotalElements(),
            allBorrowedReleases.getTotalPages(),
            allBorrowedReleases.isFirst(),
            allBorrowedReleases.isLast()
    );
  }

  public PageResponse<BorrowedReleaseResponse> findAllReturnedReleases(int page, int size, Authentication connectedUser) {
    User user = ((User) connectedUser.getPrincipal());
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<ReleaseTransactionHistory> allBorrowedReleases = transactionHistoryRepository.findAllReturnedReleases(pageable, user.getId());
    List<BorrowedReleaseResponse> releasesResponse = allBorrowedReleases.stream()
            .map(releaseMapper::toBorrowedReleaseResponse)
            .toList();
    return new PageResponse<>(
            releasesResponse,
            allBorrowedReleases.getNumber(),
            allBorrowedReleases.getSize(),
            allBorrowedReleases.getTotalElements(),
            allBorrowedReleases.getTotalPages(),
            allBorrowedReleases.isFirst(),
            allBorrowedReleases.isLast()
    );
  }

  public Integer updateShareableStatus(Integer releaseId, Authentication connectedUser) {
    Release release = releaseRepository.findById(releaseId)
            .orElseThrow(() -> new EntityNotFoundException("No release found with ID:: " + releaseId));

    User user = ((User) connectedUser.getPrincipal());

    if (!Objects.equals(release.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot update others releases shareable status");
    }
    release.setShareable(!release.isShareable());
    releaseRepository.save(release);
    return releaseId;
  }

  public Integer updateArchivedStatus(Integer releaseId, Authentication connectedUser) {
    Release release = releaseRepository.findById(releaseId)
            .orElseThrow(() -> new EntityNotFoundException("No release found with ID:: " + releaseId));

    User user = ((User) connectedUser.getPrincipal());

    if (!Objects.equals(release.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot update others releases archived status");
    }
    release.setArchived(!release.isArchived());
    releaseRepository.save(release);
    return releaseId;
  }

  public Integer borrowRelease(Integer releaseId, Authentication connectedUser) {
    Release release = releaseRepository.findById(releaseId)
            .orElseThrow(() -> new EntityNotFoundException("No release found with ID:: " + releaseId));

    if (release.isArchived() || !release.isShareable()) {
      throw new OperationNotPermittedException("The requested release cannot be borrowed since it is archived or not shareable");
    }

    User user = ((User) connectedUser.getPrincipal());

    if (Objects.equals(release.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot borrow your own release");
    }

    final boolean isAlreadyBorrowedByUser = transactionHistoryRepository.isAlreadyBorrowedByUser(releaseId, user.getId());

    if (isAlreadyBorrowedByUser) {
      throw new OperationNotPermittedException("Te requested release is already borrowed");
    }

    ReleaseTransactionHistory releaseTransactionHistory = ReleaseTransactionHistory.builder()
            .user(user)
            .release(release)
            .returned(false)
            .returnApproved(false)
            .build();
    return transactionHistoryRepository.save(releaseTransactionHistory).getId();
  }

  public Integer returnBorrowedRelease(Integer releaseId, Authentication connectedUser) {
    Release release = releaseRepository.findById(releaseId)
            .orElseThrow(() -> new EntityNotFoundException("No release found with ID:: " + releaseId));

    if (release.isArchived() || !release.isShareable()) {
      throw new OperationNotPermittedException("The requested release is archived or not shareable");
    }

    User user = ((User) connectedUser.getPrincipal());

    if (Objects.equals(release.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot borrow or return your own release");
    }

    ReleaseTransactionHistory releaseTransactionHistory = transactionHistoryRepository.findByReleaseIdAndUserId(releaseId, user.getId())
            .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this release"));

    releaseTransactionHistory.setReturned(true);
    return transactionHistoryRepository.save(releaseTransactionHistory).getId();
  }

  public Integer approveReturnBorrowedRelease(Integer releaseId, Authentication connectedUser) {
    Release release = releaseRepository.findById(releaseId)
            .orElseThrow(() -> new EntityNotFoundException("No release found with ID:: " + releaseId));

    if (release.isArchived() || !release.isShareable()) {
      throw new OperationNotPermittedException("The requested release is archived or not shareable");
    }

    User user = ((User) connectedUser.getPrincipal());

    if (Objects.equals(release.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot borrow or return your own release");
    }

    ReleaseTransactionHistory releaseTransactionHistory = transactionHistoryRepository.findByReleaseIdAndOwnerId(releaseId, user.getId())
            .orElseThrow(() -> new OperationNotPermittedException("The release is not returned yet. You cannot approve its return"));

    releaseTransactionHistory.setReturnApproved(true);
    return transactionHistoryRepository.save(releaseTransactionHistory).getId();
  }
}