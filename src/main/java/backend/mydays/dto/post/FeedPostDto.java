package backend.mydays.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private String content;
    private String contentImgUrl;
    private long likeCount;
    @JsonProperty("isLiked")
    private boolean isLiked;
    private long commentCount;
    @JsonProperty("isOwner")
    private boolean isOwner;
}
