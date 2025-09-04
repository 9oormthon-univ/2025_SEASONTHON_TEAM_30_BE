package backend.mydays.service;

import backend.mydays.domain.Users;
import backend.mydays.dto.settings.NotificationSettingsRequest;
import backend.mydays.dto.settings.NotificationSettingsResponse;
import backend.mydays.exception.ResourceNotFoundException;
import backend.mydays.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettingsService {

    private final UserRepository userRepository;

    public NotificationSettingsResponse updateNotificationSettings(NotificationSettingsRequest request, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        user.updateNotificationSettings(
                request.isLikesNotification(),
                request.isCommentsNotification(),
                request.isChallengeReminderNotification()
        );
        userRepository.save(user);

        return new NotificationSettingsResponse(
                user.isLikesNotification(),
                user.isCommentsNotification(),
                user.isChallengeReminderNotification()
        );
    }
}
