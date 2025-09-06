package backend.mydays.repository;

import backend.mydays.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByNameAndLevelAndGroupId(String name, int level, Integer groupId);
    Optional<Character> findByLevel(int level);
    List<Character> findAllByLevel(int level);
    Optional<Character> findByLevelAndGroupId(int level, Integer groupId);
}
