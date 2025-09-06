package backend.mydays.service;

import backend.mydays.domain.Challenge;
import backend.mydays.domain.UserChallenge;
import backend.mydays.domain.Users;
import backend.mydays.dto.challenge.TodayChallengeResponseDto;
import backend.mydays.repository.ChallengeRepository;
import backend.mydays.repository.UserChallengeRepository;
import backend.mydays.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;

    public TodayChallengeResponseDto getTodayChallenge(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();
        Challenge challenge = challengeRepository.findByChallengeDate(today)
                .orElseThrow(() -> new IllegalStateException("오늘의 챌린지를 찾을 수 없습니다."));

        boolean isCompleted = userChallengeRepository.findByUserAndChallenge(user, challenge).isPresent();

        return new TodayChallengeResponseDto(
                user.getAvatarImageUrl(),
                challenge.getContent(),
                isCompleted
        );
    }
}
