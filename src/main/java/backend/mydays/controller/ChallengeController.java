package backend.mydays.controller;

import backend.mydays.dto.challenge.TodayChallengeResponse;
import backend.mydays.dto.common.BaseResponse;
import backend.mydays.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "챌린지", description = "챌린지 관련 API")
@RestController
@RequestMapping("/api/v1/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @Operation(summary = "오늘의 챌린지 조회", description = "홈 화면 상단에서 오늘 수행해야 할 챌린지 내용을 조회합니다.")
    @GetMapping("/today")
    public ResponseEntity<BaseResponse<TodayChallengeResponse>> getTodayChallenge() {
        TodayChallengeResponse response = challengeService.getTodayChallenge();
        return BaseResponse.ok("오늘의 챌린지 조회에 성공했습니다.", response);
    }
}
