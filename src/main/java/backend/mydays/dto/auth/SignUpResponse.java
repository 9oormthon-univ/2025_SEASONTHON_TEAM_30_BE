package backend.mydays.dto.auth;

import backend.mydays.domain.Users;
import lombok.Getter;

@Getter
public class SignUpResponse {
    private String userId;
    private String nickname;
    private String email;

    public SignUpResponse(String userId, String nickname, String email) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
    }

    public static SignUpResponse from(Users user) {
        return new SignUpResponse(String.valueOf(user.getId()), user.getNickname(), user.getEmail());
    }
}
