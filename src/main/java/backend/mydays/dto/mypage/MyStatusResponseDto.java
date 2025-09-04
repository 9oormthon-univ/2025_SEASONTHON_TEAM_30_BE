package backend.mydays.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyStatusResponseDto {
    private String nickName;
    private String growthMessage;
    private boolean isBubbleVisible;
    private String userTitle;
    private String userTitleColor;
    private double progress;
    private String imageUrl;
    private int totalChallengeCount;
    private int daysCount;
    private boolean isCompleteMission;
}
