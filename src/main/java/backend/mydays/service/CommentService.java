package backend.mydays.service;

import backend.mydays.domain.Comment;
import backend.mydays.domain.Post;
import backend.mydays.domain.Users;
import backend.mydays.dto.comment.CommentUpdateRequest;
import backend.mydays.exception.ForbiddenException;
import backend.mydays.exception.ResourceNotFoundException;
import backend.mydays.repository.CommentRepository;
import backend.mydays.repository.PostRepository;
import backend.mydays.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createComment(Long postId, String content, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }

    @Transactional
    public void updateComment(Long commentId, CommentUpdateRequest request, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getUser().equals(user)) {
            throw new ForbiddenException("해당 댓글을 수정할 권한이 없습니다.");
        }

        comment.updateContent(request.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getUser().equals(user)) {
            throw new ForbiddenException("해당 댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
