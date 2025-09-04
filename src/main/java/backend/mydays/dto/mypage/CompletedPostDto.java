package backend.mydays.dto.mypage;

import backend.mydays.domain.UserChallenge;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CompletedPostDto {
    private LocalDate completedAt;
    private String imageUrl;

    public CompletedPostDto(LocalDate completedAt, String imageUrl) {
        this.completedAt = completedAt;
        this.imageUrl = imageUrl;
    }

    public static CompletedPostDto from(UserChallenge userChallenge) {
        return new CompletedPostDto(userChallenge.getCompletedAt(), userChallenge.getPost().getImageUrl());
    }
}
