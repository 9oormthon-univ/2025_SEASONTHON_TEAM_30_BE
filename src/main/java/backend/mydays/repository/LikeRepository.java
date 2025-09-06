package backend.mydays.repository;

import backend.mydays.domain.Like;
import backend.mydays.domain.Post;
import backend.mydays.domain.Users;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(Users user, Post post);
    List<Like> findAllByPost(Post post);
    long countByPost(Post post);
}
