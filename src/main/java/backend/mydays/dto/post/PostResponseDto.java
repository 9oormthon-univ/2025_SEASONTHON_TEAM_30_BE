package backend.mydays.dto.post;

import backend.mydays.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long postId;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private AuthorDto author;
    private long likeCount;
    private long commentCount;
    @Setter
    private boolean isLiked;

    public PostResponseDto(Post post, long likeCount, long commentCount, boolean isLiked) {
        this.postId = post.getId();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.createdAt = post.getCreatedAt();
        this.author = AuthorDto.from(post.getUser());
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.isLiked = isLiked;
    }
}
