package backend.mydays.dto.mypage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyStatusResponseDto {
    private String nickName;
    private String growthMessage;
    @JsonProperty("isBubbleVisible")
    private boolean isBubbleVisible;
    private String userTitle;
    private String userTitleColor;
    private double progress;
    private String imageUrl;
    private int totalChallengeCount;
    private int daysCount;
    @JsonProperty("isCompleteMission")
    private boolean isCompleteMission;
}
