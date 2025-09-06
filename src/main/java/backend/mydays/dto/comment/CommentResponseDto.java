package backend.mydays.dto.comment;

import backend.mydays.domain.Comment;
import backend.mydays.dto.post.AuthorDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private AuthorDto author;
    private String content;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.author = AuthorDto.from(comment.getUser());
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
