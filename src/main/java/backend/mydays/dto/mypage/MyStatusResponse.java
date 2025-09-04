package backend.mydays.dto.mypage;

import backend.mydays.domain.Users;
import backend.mydays.dto.post.ActiveTitleDto;
import lombok.Getter;

@Getter
public class MyStatusResponse {
    private String nickname;
    private String avatarImageUrl;
    private int consecutiveDays;
    private int totalCompletedDays;
    private ActiveTitleDto activeTitle;

    public MyStatusResponse(Users user) {
        this.nickname = user.getNickname();
        this.avatarImageUrl = user.getAvatarImageUrl();
        this.consecutiveDays = user.getConsecutiveDays();
        this.totalCompletedDays = user.getTotalCompletedDays();
        this.activeTitle = ActiveTitleDto.from(user.getActiveTitle());
    }
}
