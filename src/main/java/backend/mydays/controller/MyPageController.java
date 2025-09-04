package backend.mydays.controller;

import backend.mydays.dto.common.BaseResponse;
import backend.mydays.dto.mypage.ActiveTitleUpdateRequest;
import backend.mydays.dto.mypage.MyCalendarResponse;
import backend.mydays.dto.mypage.MyStatusResponse;
import backend.mydays.dto.mypage.MyTitlesResponse;
import backend.mydays.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "마이페이지", description = "개인 챌린지 활동 및 관리 API")
@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "나의 챌린지 현황 조회", description = "마이페이지에서 나의 프로필, 챌린지 통계, 칭호 정보를 조회합니다.")
    @GetMapping("/status")
    public ResponseEntity<BaseResponse<MyStatusResponse>> getMyStatus(@AuthenticationPrincipal UserDetails userDetails) {
        MyStatusResponse response = myPageService.getMyStatus(userDetails.getUsername());
        return BaseResponse.ok("나의 챌린지 현황 조회에 성공했습니다.", response);
    }

    @Operation(summary = "나의 챌린지 달력 조회", description = "월별 달력 형태로 내가 챌린지를 수행한 날짜들을 확인합니다.")
    @GetMapping("/calendar")
    public ResponseEntity<BaseResponse<MyCalendarResponse>> getMyCalendar(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        MyCalendarResponse response = myPageService.getMyCalendar(year, month, userDetails.getUsername());
        return BaseResponse.ok("나의 챌린지 달력 조회에 성공했습니다.", response);
    }

    @Operation(summary = "보유 칭호 목록 조회", description = "내가 획득한 칭호 목록을 조회합니다.")
    @GetMapping("/titles")
    public ResponseEntity<BaseResponse<MyTitlesResponse>> getMyTitles(@AuthenticationPrincipal UserDetails userDetails) {
        MyTitlesResponse response = myPageService.getMyTitles(userDetails.getUsername());
        return BaseResponse.ok("보유 칭호 목록 조회에 성공했습니다.", response);
    }

    @Operation(summary = "대표 칭호 변경", description = "획득한 칭호 중 하나를 대표 칭호로 설정합니다.")
    @PutMapping("/active-title")
    public ResponseEntity<BaseResponse<Void>> updateActiveTitle(
            @RequestBody ActiveTitleUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        myPageService.updateActiveTitle(request, userDetails.getUsername());
        return BaseResponse.ok("대표 칭호가 변경되었습니다.", null);
    }
}
