package backend.mydays.dto.post;

import backend.mydays.domain.Title;
import lombok.Getter;

@Getter
public class ActiveTitleDto {
    private Long titleId;
    private String titleName;

    public ActiveTitleDto(Long titleId, String titleName) {
        this.titleId = titleId;
        this.titleName = titleName;
    }

    public static ActiveTitleDto from(Title title) {
        if (title == null) {
            return null;
        }
        return new ActiveTitleDto(title.getId(), title.getName());
    }
}
