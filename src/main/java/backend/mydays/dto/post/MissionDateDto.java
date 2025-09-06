package backend.mydays.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MissionDateDto {
    private String date;
    private String missionText;

}
