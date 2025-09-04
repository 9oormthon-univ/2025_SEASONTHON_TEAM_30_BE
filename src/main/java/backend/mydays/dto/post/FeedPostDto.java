package backend.mydays.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FeedPostDto {
    private String postId;
    private String userimgUrl;
    private String userName;
    private String userTitle;
    private String userTitleColor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private String content;
    private String contentImgUrl;
    private long likeCount;
    private boolean isLiked;
    private long commentCount;
}
