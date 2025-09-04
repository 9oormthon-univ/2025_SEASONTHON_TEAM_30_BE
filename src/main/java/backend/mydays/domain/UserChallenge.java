package backend.mydays.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(name = "User_Challenges",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "challenge_id"}),
    indexes = @Index(name = "idx_user_challenges_user_date", columnList = "user_id, completed_at"))
public class UserChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userChallengeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;

    @Column
    private LocalDate completedAt;
}

