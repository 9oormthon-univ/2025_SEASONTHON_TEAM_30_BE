package backend.mydays.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Challenges")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate challengeDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder
    public Challenge(LocalDate challengeDate, String content) {
        this.challengeDate = challengeDate;
        this.content = content;
    }
}
