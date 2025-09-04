package backend.mydays.controller;

import backend.mydays.dto.common.BaseResponse;
import backend.mydays.dto.settings.NotificationSettingsRequest;
import backend.mydays.dto.settings.NotificationSettingsResponse;
import backend.mydays.service.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "설정", description = "사용자 설정 관련 API")
@RestController
@RequestMapping("/api/v1/me/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    @Operation(summary = "알림 설정 변경", description = "푸시 알림 수신 여부 등을 변경합니다.")
    @PutMapping("/notifications")
    public ResponseEntity<BaseResponse<NotificationSettingsResponse>> updateNotificationSettings(
            @RequestBody NotificationSettingsRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        NotificationSettingsResponse response = settingsService.updateNotificationSettings(request, userDetails.getUsername());
        return BaseResponse.ok("알림 설정이 변경되었습니다.", response);
    }
}
