package backend.mydays.controller;

import backend.mydays.dto.auth.LoginRequest;
import backend.mydays.dto.auth.LoginResponse;
import backend.mydays.dto.auth.SignUpRequest;
import backend.mydays.dto.auth.SignUpResponse;
import backend.mydays.dto.common.BaseResponse;
import backend.mydays.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증", description = "사용자 인증 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "닉네임, 이메일, 비밀번호로 회원가입을 진행합니다.")
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<SignUpResponse>> signUp(@RequestBody SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);
        return BaseResponse.created("회원가입이 성공적으로 완료되었습니다.", response);
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return BaseResponse.ok("로그인에 성공했습니다.", response);
    }
}
