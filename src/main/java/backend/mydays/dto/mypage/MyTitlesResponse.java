package backend.mydays.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyTitlesResponse {
    private List<EarnedTitleDto> titles;
}
