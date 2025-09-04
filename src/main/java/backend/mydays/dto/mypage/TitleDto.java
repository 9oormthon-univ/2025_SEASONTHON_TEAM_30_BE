package backend.mydays.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TitleDto {
    private String titleId;
    private String title;
    private String titleColor;
}
