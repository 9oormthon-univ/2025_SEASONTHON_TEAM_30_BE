package backend.mydays.repository;

import backend.mydays.domain.Comment;
import backend.mydays.domain.Post;
import backend.mydays.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
    long countByPost(Post post);
    Optional<Comment> findByPostAndUserAndContent(Post post, Users user, String content);
}
