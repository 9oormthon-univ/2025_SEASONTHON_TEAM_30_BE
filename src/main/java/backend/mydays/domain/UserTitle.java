package backend.mydays.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(name = "User_Titles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "title_id"})
})
public class UserTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userTitleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "title_id", nullable = false)
    private Title title;

    @Column(nullable = false)
    private LocalDateTime earnedAt = LocalDateTime.now();
}

