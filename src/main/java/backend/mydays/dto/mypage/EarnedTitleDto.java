package backend.mydays.dto.mypage;

import backend.mydays.domain.UserTitle;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EarnedTitleDto {
    private Long titleId;
    private String titleName;
    private String titleDescription;
    private LocalDateTime earnedAt;

    public EarnedTitleDto(Long titleId, String titleName, String titleDescription, LocalDateTime earnedAt) {
        this.titleId = titleId;
        this.titleName = titleName;
        this.titleDescription = titleDescription;
        this.earnedAt = earnedAt;
    }

    public static EarnedTitleDto from(UserTitle userTitle) {
        return new EarnedTitleDto(
                userTitle.getTitle().getId(),
                userTitle.getTitle().getName(),
                userTitle.getTitle().getDescription(),
                userTitle.getEarnedAt()
        );
    }
}
