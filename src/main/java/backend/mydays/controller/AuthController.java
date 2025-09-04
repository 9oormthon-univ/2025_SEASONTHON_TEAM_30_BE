package backend.mydays.controller;

import backend.mydays.dto.auth.LoginRequest;
import backend.mydays.dto.auth.LoginResponse;
import backend.mydays.dto.auth.SignUpRequest;
import backend.mydays.dto.auth.SignUpResponse;
import backend.mydays.dto.common.BaseResponse;
import backend.mydays.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<SignUpResponse>> signUp(@RequestBody SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);
        return BaseResponse.created("회원가입이 성공적으로 완료되었습니다.", response);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return BaseResponse.ok("로그인에 성공했습니다.", response);
    }
}
