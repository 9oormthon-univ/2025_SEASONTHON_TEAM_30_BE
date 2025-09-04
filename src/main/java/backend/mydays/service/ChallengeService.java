package backend.mydays.service;

import backend.mydays.domain.Challenge;
import backend.mydays.dto.challenge.TodayChallengeResponse;
import backend.mydays.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public TodayChallengeResponse getTodayChallenge() {
        LocalDate today = LocalDate.now();
        Challenge challenge = challengeRepository.findByChallengeDate(today)
                .orElseThrow(() -> new IllegalStateException("오늘의 챌린지를 찾을 수 없습니다."));
        return TodayChallengeResponse.from(challenge);
    }
}
