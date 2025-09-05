package backend.mydays.controller;

import backend.mydays.dto.common.BaseResponse;
import backend.mydays.dto.mypage.ActiveTitleUpdateRequest;
import backend.mydays.dto.mypage.MyCalendarResponseDto;
import backend.mydays.dto.mypage.MyStatusResponseDto;
import backend.mydays.dto.mypage.MyTitlesResponseDto;
import backend.mydays.dto.post.CalendarPostDetailResponseWrapperDto;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "나의 챌린지 현황 조회", description = "나의 프로필 정보, 챌린지 기록, 현재 캐릭터 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<MyStatusResponseDto>> getMyStatus(@AuthenticationPrincipal UserDetails userDetails) {
        MyStatusResponseDto response = myPageService.getMyStatus(userDetails.getUsername());
        return BaseResponse.ok("나의 챌린지 현황 조회에 성공했습니다.", response);
    }

    @Operation(summary = "나의 챌린지 달력 조회", description = "월별 달력 형태로 내가 챌린지를 수행한 날짜들을 확인합니다.")
    @GetMapping("/calendar")
    public ResponseEntity<BaseResponse<MyCalendarResponseDto>> getMyCalendar(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        MyCalendarResponseDto response = myPageService.getMyCalendar(year, month, userDetails.getUsername());
        return BaseResponse.ok("나의 챌린지 달력 조회에 성공했습니다.", response);
    }


    @Operation(summary = "내 게시물 상세 조회", description = "게시물 ID로 내가 작성한 게시물 상세 정보를 조회합니다.")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<BaseResponse<CalendarPostDetailResponseWrapperDto>> getMyPostByPostId(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        CalendarPostDetailResponseWrapperDto response = myPageService.getCalendarPostDetail(postId, userDetails.getUsername());
        return BaseResponse.ok("내 게시물 상세 조회에 성공했습니다.", response);
    }

    @Operation(summary = "보유 칭호 목록 조회", description = "내가 획득한 칭호 목록을 조회합니다.")
    @GetMapping("/titles")
    public ResponseEntity<BaseResponse<MyTitlesResponseDto>> getMyTitles(@AuthenticationPrincipal UserDetails userDetails) {
        MyTitlesResponseDto response = myPageService.getMyTitles(userDetails.getUsername());
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

    @Operation(summary = "나의 모든 챌린지 게시물 조회", description = "유저가 수행한 모든 챌린지 게시물 목록을 조회합니다.")
    @GetMapping("/calendar/all")
    public ResponseEntity<BaseResponse<MyCalendarResponseDto>> getAllMyChallenges(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        MyCalendarResponseDto response = myPageService.getAllMyChallenges(userDetails.getUsername());
        return BaseResponse.ok("나의 모든 챌린지 게시물 조회에 성공했습니다.", response);
    }

}
