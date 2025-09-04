package backend.mydays.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "Challenges")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer challengeId;

    @Column(unique = true, nullable = false)
    private LocalDate challengeDate;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}

