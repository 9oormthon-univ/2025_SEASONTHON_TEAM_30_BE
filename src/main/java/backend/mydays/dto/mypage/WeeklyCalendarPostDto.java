package backend.mydays.dto.mypage;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WeeklyCalendarPostDto {
    private String postId;
    private String userimgUrl;
    private String userName;
    private String userTitle;
    private String userTitleColor;
    private LocalDateTime createdAt;
    private String content;
    private String contentImgUrl;
    private int likeCount;
    private boolean isLiked;
    private int commentCount;
}
