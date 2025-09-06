package backend.mydays.dto.mypage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DayContentDto {
    private int day;
    private String date;
    private String missionText;
    private boolean isCompleted;
    private WeeklyCalendarPostDto post;
}
