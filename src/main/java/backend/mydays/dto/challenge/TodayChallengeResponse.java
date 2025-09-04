package backend.mydays.dto.challenge;

import backend.mydays.domain.Challenge;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TodayChallengeResponse {
    private Long challengeId;
    private LocalDate challengeDate;
    private String content;

    public TodayChallengeResponse(Long challengeId, LocalDate challengeDate, String content) {
        this.challengeId = challengeId;
        this.challengeDate = challengeDate;
        this.content = content;
    }

    public static TodayChallengeResponse from(Challenge challenge) {
        return new TodayChallengeResponse(challenge.getId(), challenge.getChallengeDate(), challenge.getContent());
    }
}
