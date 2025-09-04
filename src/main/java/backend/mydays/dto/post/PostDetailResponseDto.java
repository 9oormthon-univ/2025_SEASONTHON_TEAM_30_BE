package backend.mydays.dto.post;

import backend.mydays.domain.Post;
import backend.mydays.dto.comment.CommentResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PostDetailResponseDto extends PostResponseDto {

    private final List<CommentResponseDto> comments;

    public PostDetailResponseDto(Post post, long likeCount, long commentCount, boolean isLiked, List<CommentResponseDto> comments) {
        super(post, likeCount, commentCount, isLiked);
        this.comments = comments;
    }
}
