package backend.mydays.dto.settings;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationSettingsResponse {
    private boolean likesNotification;
    private boolean commentsNotification;
    private boolean challengeReminderNotification;
}
