package backend.mydays.controller;

import backend.mydays.dto.challenge.TodayChallengeResponse;
import backend.mydays.dto.common.BaseResponse;
import backend.mydays.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/today")
    public ResponseEntity<BaseResponse<TodayChallengeResponse>> getTodayChallenge() {
        TodayChallengeResponse response = challengeService.getTodayChallenge();
        return BaseResponse.ok("오늘의 챌린지 조회에 성공했습니다.", response);
    }
}
