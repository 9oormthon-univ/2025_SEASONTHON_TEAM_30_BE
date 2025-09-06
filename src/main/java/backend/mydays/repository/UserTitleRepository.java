package backend.mydays.repository;

import backend.mydays.domain.Title;
import backend.mydays.domain.UserTitle;
import backend.mydays.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTitleRepository extends JpaRepository<UserTitle, Long> {
    boolean existsByUserAndTitle(Users user, Title title);
    List<UserTitle> findByUser(Users user);
    Optional<UserTitle> findByUserAndTitle(Users user, Title title);
}
