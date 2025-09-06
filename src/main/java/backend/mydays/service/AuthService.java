package backend.mydays.service;

import backend.mydays.domain.Title;
import backend.mydays.domain.Users;
import backend.mydays.dto.auth.*;
import backend.mydays.config.jwt.JwtProvider;
import backend.mydays.domain.Character;
import io.jsonwebtoken.Claims;
import backend.mydays.dto.kakao.KakaoUserInfoResponse;
import backend.mydays.exception.ResourceNotFoundException;
import backend.mydays.repository.CharacterRepository;
import backend.mydays.repository.TitleRepository;
import backend.mydays.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final CharacterRepository characterRepository;
    private final TitleRepository titleRepository;
    private final KakaoService kakaoService; // Inject KakaoService

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        Title title = titleRepository.findById(1L)
            .orElseThrow(() -> new IllegalArgumentException("Title with id=1 not found"));

        Character character = characterRepository.findById(1L)
            .orElseThrow(() -> new IllegalArgumentException("Character with id=1 not found"));


        Users user = Users.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .activeTitle(title)
                .character(character)
                .build();

        Character defaultCharacter = characterRepository.findByLevel(1)
                .orElseThrow(() -> new RuntimeException("Level 1 character not found!"));
        user.setCharacter(defaultCharacter);

        Users savedUser = userRepository.save(user);
        return SignUpResponse.from(savedUser);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        Users user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", authentication.getName()));

        String accessToken = jwtProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

        Date refreshTokenExpiryDate = jwtProvider.getExpirationDateFromToken(refreshToken);
        user.updateRefreshToken(refreshToken, Instant.ofEpochMilli(refreshTokenExpiryDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        userRepository.save(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    @Transactional
    public KakaoLoginResponse kakaoLogin(String idToken) {
        Claims claims = kakaoService.getClaimsFromIdToken(idToken);

        String email = claims.get("email", String.class);
        String nickname = claims.get("nickname", String.class);

        boolean isNewUser = !userRepository.findByEmail(email).isPresent();

        Users user = userRepository.findByEmail(email).orElseGet(() -> {
            String uniqueNickname = getUniqueNickname(nickname);
            Title title = titleRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("Title with id=1 not found"));
            Character character = characterRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("Character with id=1 not found"));

            Users newUser = Users.builder()
                    .email(email)
                    .nickname(uniqueNickname)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .activeTitle(title)
                    .character(character)
                    .build();
            return userRepository.save(newUser);
        });

        String accessToken = jwtProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

        Date refreshTokenExpiryDate = jwtProvider.getExpirationDateFromToken(refreshToken);
        user.updateRefreshToken(refreshToken, Instant.ofEpochMilli(refreshTokenExpiryDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        userRepository.save(user);

        return new KakaoLoginResponse(accessToken, refreshToken, isNewUser);
    }

    @Transactional
    public TokenRefreshResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        if (!jwtProvider.validateToken(requestRefreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String email = jwtProvider.getUsernameFromToken(requestRefreshToken);
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (!user.getRefreshToken().equals(requestRefreshToken) || user.getRefreshTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh token is not valid or expired");
        }

        String newAccessToken = jwtProvider.generateAccessToken(email);
        return new TokenRefreshResponse(newAccessToken);
    }

    private String getUniqueNickname(String nickname) {
        String uniqueNickname = nickname;
        int suffix = 1;
        while (userRepository.findByNickname(uniqueNickname).isPresent()) {
            uniqueNickname = nickname + suffix++;
        }
        return uniqueNickname;
    }


    @Transactional
    public void updateNickname(String email, String newNickname) {
        if (userRepository.findByNickname(newNickname).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setNickname(newNickname);
        userRepository.save(user);
    }
}
