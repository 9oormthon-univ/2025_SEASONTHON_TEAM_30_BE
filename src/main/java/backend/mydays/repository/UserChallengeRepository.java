package backend.mydays.repository;

import backend.mydays.domain.Post;
import backend.mydays.domain.UserChallenge;
import backend.mydays.domain.Users;
import backend.mydays.domain.Challenge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    // For My Page calendar (EPIC 5.2)
    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user = :user AND uc.completedAt >= :startDate AND uc.completedAt <= :endDate")
    List<UserChallenge> findByUserAndMonth(@Param("user") Users user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Optional<UserChallenge> findByPost(Post post);
    Optional<UserChallenge> findByUserAndChallenge(Users user, Challenge challenge);

    Optional<UserChallenge> findByUserAndCompletedAt(Users user, LocalDate completedAt);

    List<UserChallenge> findByUser(Users user);
}
