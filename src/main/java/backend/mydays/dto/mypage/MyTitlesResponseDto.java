package backend.mydays.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MyTitlesResponseDto {
    private List<TitleDto> titles;
}
