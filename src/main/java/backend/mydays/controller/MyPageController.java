package backend.mydays.controller;

import backend.mydays.dto.common.BaseResponse;
import backend.mydays.dto.mypage.ActiveTitleUpdateRequest;
import backend.mydays.dto.mypage.MyCalendarResponse;
import backend.mydays.dto.mypage.MyStatusResponse;
import backend.mydays.dto.mypage.MyTitlesResponse;
import backend.mydays.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/status")
    public ResponseEntity<BaseResponse<MyStatusResponse>> getMyStatus(@AuthenticationPrincipal UserDetails userDetails) {
        MyStatusResponse response = myPageService.getMyStatus(userDetails.getUsername());
        return BaseResponse.ok("나의 챌린지 현황 조회에 성공했습니다.", response);
    }

    @GetMapping("/calendar")
    public ResponseEntity<BaseResponse<MyCalendarResponse>> getMyCalendar(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        MyCalendarResponse response = myPageService.getMyCalendar(year, month, userDetails.getUsername());
        return BaseResponse.ok("나의 챌린지 달력 조회에 성공했습니다.", response);
    }

    @GetMapping("/titles")
    public ResponseEntity<BaseResponse<MyTitlesResponse>> getMyTitles(@AuthenticationPrincipal UserDetails userDetails) {
        MyTitlesResponse response = myPageService.getMyTitles(userDetails.getUsername());
        return BaseResponse.ok("보유 칭호 목록 조회에 성공했습니다.", response);
    }

    @PutMapping("/active-title")
    public ResponseEntity<BaseResponse<Void>> updateActiveTitle(
            @RequestBody ActiveTitleUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        myPageService.updateActiveTitle(request, userDetails.getUsername());
        return BaseResponse.ok("대표 칭호가 변경되었습니다.", null);
    }
}
