package backend.mydays.config.data;

import backend.mydays.domain.*;
import backend.mydays.domain.Character;
import backend.mydays.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChallengeRepository challengeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final TitleRepository titleRepository;
    private final UserTitleRepository userTitleRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final CharacterRepository characterRepository;

    @Override
    public void run(String... args) throws Exception {
        // 0. Create Characters
        createCharacterIfNotFound("새싹", 1, "/images/sprout_lv1.png");
        createCharacterIfNotFound("새싹", 2, "/images/sprout_lv2.png");
        createCharacterIfNotFound("불꽃", 3, "/images/flame_lv3.png");
        createCharacterIfNotFound("물방울", 3, "/images/droplet_lv3.png");
        createCharacterIfNotFound("풀잎", 3, "/images/leaf_lv3.png");
        createCharacterIfNotFound("불꽃", 4, "/images/flame_lv4.png");
        createCharacterIfNotFound("물방울", 4, "/images/droplet_lv4.png");
        createCharacterIfNotFound("풀잎", 4, "/images/leaf_lv4.png");
        createCharacterIfNotFound("불꽃", 5, "/images/flame_lv5.png");
        createCharacterIfNotFound("물방울", 5, "/images/droplet_lv5.png");
        createCharacterIfNotFound("풀잎", 5, "/images/leaf_lv5.png");

        // 1. Create Users
        Users user1 = createUserIfNotFound("test@test.com", "테스트유저", "password");
        Users user2 = createUserIfNotFound("user2@example.com", "챌린지마스터", "password");
        Users user3 = createUserIfNotFound("user3@example.com", "꾸준함의아이콘", "password");

        // 2. Create Challenges
        Challenge challenge1 = createChallengeIfNotFound(LocalDate.of(2025, 9, 1), "오늘의 챌린지: 물 2리터 마시기");
        Challenge challenge2 = createChallengeIfNotFound(LocalDate.of(2025, 9, 2), "오늘의 챌린지: 10분 명상하기");
        Challenge challenge3 = createChallengeIfNotFound(LocalDate.of(2025, 9, 3), "오늘의 챌린지: 감사일기 쓰기");
        Challenge challenge4 = createChallengeIfNotFound(LocalDate.of(2025, 9, 4), "오늘의 챌린지: 계단 이용하기");
        Challenge challenge5 = createChallengeIfNotFound(LocalDate.of(2025, 9, 5), "오늘의 챌린지: 아침 스트레칭 5분");

        // 3. Create Posts
        Post post1 = createPostIfNotFound(user1, challenge1, "물 2리터 마시기 성공! 상쾌하네요.", "https://picsum.photos/id/10/400/300");
        Post post2 = createPostIfNotFound(user2, challenge1, "물 마시기 챌린지 완료! 덕분에 피부가 좋아진 것 같아요.", "https://picsum.photos/id/20/400/300");
        Post post3 = createPostIfNotFound(user1, challenge2, "10분 명상으로 마음의 평화를 찾았습니다.", "https://picsum.photos/id/30/400/300");
        Post post4 = createPostIfNotFound(user3, challenge2, "명상 챌린지 성공! 꾸준히 해봐야겠어요.", "https://picsum.photos/id/40/400/300");
        Post post5 = createPostIfNotFound(user2, challenge3, "감사일기 작성 완료! 소소한 행복을 찾았어요.", "https://picsum.photos/id/50/400/300");
        Post post6 = createPostIfNotFound(user1, challenge4, "계단 이용 챌린지! 다리가 튼튼해지는 기분입니다.", "https://picsum.photos/id/60/400/300");
        Post post7 = createPostIfNotFound(user3, challenge5, "아침 스트레칭으로 개운하게 하루 시작!", "https://picsum.photos/id/70/400/300");

        // 4. Create UserChallenges (link posts to challenges for user completion)
        createUserChallengeIfNotFound(user1, challenge1, post1);
        createUserChallengeIfNotFound(user2, challenge1, post2);
        createUserChallengeIfNotFound(user1, challenge2, post3);
        createUserChallengeIfNotFound(user3, challenge2, post4);
        createUserChallengeIfNotFound(user2, challenge3, post5);
        createUserChallengeIfNotFound(user1, challenge4, post6);
        createUserChallengeIfNotFound(user3, challenge5, post7);

        // 5. Create Likes
        createLikeIfNotFound(user2, post1);
        createLikeIfNotFound(user3, post1);
        createLikeIfNotFound(user1, post2);
        createLikeIfNotFound(user3, post2);
        createLikeIfNotFound(user2, post3);
        createLikeIfNotFound(user1, post4);
        createLikeIfNotFound(user3, post5);
        createLikeIfNotFound(user2, post6);
        createLikeIfNotFound(user1, post7);

        // 6. Create Comments
        createCommentIfNotFound(post1, user2, "정말 대단하세요!");
        createCommentIfNotFound(post1, user3, "저도 오늘부터 시작해봐야겠어요.");
        createCommentIfNotFound(post2, user1, "피부 좋아지셨다니 저도 꾸준히 해봐야겠네요!");
        createCommentIfNotFound(post3, user3, "명상 저도 해보고 싶어요.");
        createCommentIfNotFound(post5, user1, "감사일기 좋은 습관이죠!");

        // 7. Create Titles
        Title title1 = createTitleIfNotFound("첫 걸음", "하루 챌린지를 처음 시작한 사용자");
        Title title2 = createTitleIfNotFound("꾸준함의 아이콘", "7일 연속 챌린지 성공");
        Title title3 = createTitleIfNotFound("챌린지 마스터", "30일 연속 챌린지 성공");

        // 8. Assign User Titles
        createUserTitleIfNotFound(user1, title1);
        createUserTitleIfNotFound(user2, title2);
        createUserTitleIfNotFound(user3, title3);

        // Set active titles for users
        user1.updateActiveTitle(title1);
        userRepository.save(user1);
        user2.updateActiveTitle(title2);
        userRepository.save(user2);
        user3.updateActiveTitle(title3);
        userRepository.save(user3);
    }

    private Users createUserIfNotFound(String email, String nickname, String password) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            Users newUser = Users.builder()
                    .nickname(nickname)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
            Character defaultCharacter = characterRepository.findByLevel(1)
                    .orElseThrow(() -> new RuntimeException("Level 1 character not found!"));
            newUser.setCharacter(defaultCharacter);
            return userRepository.save(newUser);
        });
    }

    private Challenge createChallengeIfNotFound(LocalDate date, String content) {
        return challengeRepository.findByChallengeDate(date).orElseGet(() -> {
            Challenge newChallenge = Challenge.builder()
                    .challengeDate(date)
                    .content(content)
                    .build();
            return challengeRepository.save(newChallenge);
        });
    }

    private Post createPostIfNotFound(Users user, Challenge challenge, String content, String imageUrl) {
        // Check if a post already exists for this user and challenge on this date
        return postRepository.findByUserAndChallenge(user, challenge).orElseGet(() -> {
            Post newPost = Post.builder()
                    .user(user)
                    .challenge(challenge)
                    .content(content)
                    .imageUrl(imageUrl)
                    .build();
            return postRepository.save(newPost);
        });
    }

    private Comment createCommentIfNotFound(Post post, Users user, String content) {
        // Simple check to avoid duplicate comments with same content by same user on same post
        return commentRepository.findByPostAndUserAndContent(post, user, content).orElseGet(() -> {
            Comment newComment = Comment.builder()
                    .post(post)
                    .user(user)
                    .content(content)
                    .build();
            return commentRepository.save(newComment);
        });
    }

    private Like createLikeIfNotFound(Users user, Post post) {
        return likeRepository.findByUserAndPost(user, post).orElseGet(() -> {
            Like newLike = Like.builder()
                    .user(user)
                    .post(post)
                    .build();
            return likeRepository.save(newLike);
        });
    }

    private Title createTitleIfNotFound(String name, String description) {
        return titleRepository.findByName(name).orElseGet(() -> {
            Title newTitle = Title.builder()
                    .name(name)
                    .description(description)
                    .build();
            return titleRepository.save(newTitle);
        });
    }

    private UserTitle createUserTitleIfNotFound(Users user, Title title) {
        return userTitleRepository.findByUserAndTitle(user, title).orElseGet(() -> {
            UserTitle newUserTitle = UserTitle.builder()
                    .user(user)
                    .title(title)
                    .earnedAt(LocalDateTime.now())
                    .build();
            return userTitleRepository.save(newUserTitle);
        });
    }

    private UserChallenge createUserChallengeIfNotFound(Users user, Challenge challenge, Post post) {
        return userChallengeRepository.findByUserAndChallenge(user, challenge).orElseGet(() -> {
            UserChallenge newUserChallenge = UserChallenge.builder()
                    .user(user)
                    .challenge(challenge)
                    .post(post)
                    .completedAt(challenge.getChallengeDate())
                    .build();
            return userChallengeRepository.save(newUserChallenge);
        });
    }

    private Character createCharacterIfNotFound(String name, int level, String imageUrl) {
        return characterRepository.findByNameAndLevel(name, level).orElseGet(() -> {
            Character newCharacter = Character.builder()
                    .name(name)
                    .level(level)
                    .imageUrl(imageUrl)
                    .build();
            return characterRepository.save(newCharacter);
        });
    }
}