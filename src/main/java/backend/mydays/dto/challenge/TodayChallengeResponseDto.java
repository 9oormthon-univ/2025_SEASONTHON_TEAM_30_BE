package backend.mydays.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TodayChallengeResponseDto {
    private String userImgUrl;
    private String text;
    private boolean isCompleted;
}
