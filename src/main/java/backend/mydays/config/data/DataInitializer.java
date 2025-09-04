package backend.mydays.config.data;

import backend.mydays.domain.Users;
import backend.mydays.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("test@test.com").isEmpty()) {
            Users testUser = Users.builder()
                    .nickname("테스트유저")
                    .email("test@test.com")
                    .password(passwordEncoder.encode("password"))
                    .build();
            userRepository.save(testUser);
        }
    }
}
