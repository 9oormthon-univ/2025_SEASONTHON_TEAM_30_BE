package backend.mydays.repository;

import backend.mydays.domain.Comment;
import backend.mydays.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
    long countByPost(Post post);
}
