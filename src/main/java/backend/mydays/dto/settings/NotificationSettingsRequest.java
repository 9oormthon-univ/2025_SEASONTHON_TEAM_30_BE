package backend.mydays.dto.settings;

import lombok.Data;

@Data
public class NotificationSettingsRequest {
    private boolean likesNotification;
    private boolean commentsNotification;
    private boolean challengeReminderNotification;
}
