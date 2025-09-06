package backend.mydays.service;

import backend.mydays.domain.Title;
import backend.mydays.domain.Users;
import backend.mydays.dto.auth.LoginRequest;
import backend.mydays.dto.auth.LoginResponse;
import backend.mydays.dto.auth.SignUpRequest;
import backend.mydays.dto.auth.SignUpResponse;
import backend.mydays.config.jwt.JwtProvider;
import backend.mydays.domain.Character;
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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final CharacterRepository characterRepository;
    private final TitleRepository titleRepository;

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

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtProvider.generateToken(authentication.getName());
        return new LoginResponse(token);
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
