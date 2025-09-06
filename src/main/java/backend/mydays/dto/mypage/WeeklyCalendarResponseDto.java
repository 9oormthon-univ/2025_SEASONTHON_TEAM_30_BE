package backend.mydays.dto.mypage;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class WeeklyCalendarResponseDto {
    private List<DayContentDto> dayContents;
}
