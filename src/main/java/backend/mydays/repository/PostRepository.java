package backend.mydays.repository;

import backend.mydays.domain.Post;
import backend.mydays.domain.Users;
import backend.mydays.domain.Challenge;
import java.time.LocalDate;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // For the main feed (EPIC 2.2)
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // For My Page calendar specific date posts (EPIC 5.3)
    @Query("SELECT p FROM Post p WHERE p.user = :user AND p.createdAt >= :startOfDay AND p.createdAt < :endOfDay")
    List<Post> findByUserAndDate(@Param("user") Users user, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    Optional<Post> findByUserAndChallenge(Users user, Challenge challenge);

    Integer countByUser(Users user);

    List<Post> findAllByUser(Users user);
}
