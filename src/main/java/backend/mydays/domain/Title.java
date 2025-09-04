package backend.mydays.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Titles")
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer titleId;

    @Column(length = 100, nullable = false)
    private String titleName;

    @Column(columnDefinition = "TEXT")
    private String titleDescription;
}

