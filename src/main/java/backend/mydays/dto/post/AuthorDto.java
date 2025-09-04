package backend.mydays.dto.post;

import backend.mydays.domain.Users;
import lombok.Getter;

@Getter
public class AuthorDto {
    private String userId;
    private String nickname;
    private String avatarImageUrl;
    private ActiveTitleDto activeTitle;

    public AuthorDto(String userId, String nickname, String avatarImageUrl, ActiveTitleDto activeTitle) {
        this.userId = userId;
        this.nickname = nickname;
        this.avatarImageUrl = avatarImageUrl;
        this.activeTitle = activeTitle;
    }

    public static AuthorDto from(Users user) {
        return new AuthorDto(
                String.valueOf(user.getId()),
                user.getNickname(),
                user.getAvatarImageUrl(),
                ActiveTitleDto.from(user.getActiveTitle())
        );
    }
}
