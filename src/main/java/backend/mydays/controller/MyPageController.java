package backend.mydays.controller;

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
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/status")
    public ResponseEntity<MyStatusResponse> getMyStatus(@AuthenticationPrincipal UserDetails userDetails) {
        MyStatusResponse response = myPageService.getMyStatus(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/calendar")
    public ResponseEntity<MyCalendarResponse> getMyCalendar(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        MyCalendarResponse response = myPageService.getMyCalendar(year, month, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/titles")
    public ResponseEntity<MyTitlesResponse> getMyTitles(@AuthenticationPrincipal UserDetails userDetails) {
        MyTitlesResponse response = myPageService.getMyTitles(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/active-title")
    public ResponseEntity<Void> updateActiveTitle(
            @RequestBody ActiveTitleUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        myPageService.updateActiveTitle(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
