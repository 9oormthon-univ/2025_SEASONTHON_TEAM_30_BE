package backend.mydays.repository;

import backend.mydays.domain.Title;
import backend.mydays.domain.UserTitle;
import backend.mydays.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTitleRepository extends JpaRepository<UserTitle, Long> {
    boolean existsByUserAndTitle(Users user, Title title);
    List<UserTitle> findByUser(Users user);
}
