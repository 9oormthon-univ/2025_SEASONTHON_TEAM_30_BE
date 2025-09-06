package backend.mydays.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "active_title_id")
    private Title activeTitle;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int consecutiveDays;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int totalCompletedDays;

    @Column(columnDefinition = "INT DEFAULT 1")
    private int level;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean likesNotification = true;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean commentsNotification = true;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean challengeReminderNotification = true;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "refresh_token_expiry_date")
    private LocalDateTime refreshTokenExpiryDate;

    @Builder
    public Users(String nickname, String email, String password, Title activeTitle, Character character) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.activeTitle = activeTitle;
        this.character = character;
        this.level = 1;
    }

    public void updateRefreshToken(String refreshToken, LocalDateTime refreshTokenExpiryDate) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiryDate = refreshTokenExpiryDate;
    }

    public void levelUp() {
        this.level++;
    }

    public void updateActiveTitle(Title title) {
        this.activeTitle = title;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void setConsecutiveDays(int consecutiveDays) {
        this.consecutiveDays = consecutiveDays;
    }

    public void setTotalCompletedDays(int totalCompletedDays) {
        this.totalCompletedDays = totalCompletedDays;
    }

    public String getAvatarImageUrl() {
        return this.character != null ? this.character.getImageUrl() : null;
    }

    public void updateNotificationSettings(boolean likesNotification, boolean commentsNotification, boolean challengeReminderNotification) {
        this.likesNotification = likesNotification;
        this.commentsNotification = commentsNotification;
        this.challengeReminderNotification = challengeReminderNotification;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
