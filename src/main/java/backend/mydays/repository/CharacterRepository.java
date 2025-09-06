package backend.mydays.repository;

import backend.mydays.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByNameAndLevel(String name, int level);
    Optional<Character> findByLevel(int level);
}
