package backend.mydays.service;

import backend.mydays.domain.*;
import backend.mydays.dto.comment.CommentDto;
import backend.mydays.dto.post.FeedPostDto;
import backend.mydays.dto.post.MissionTextDto;
import backend.mydays.dto.post.PostCreateRequest;
import backend.mydays.dto.post.PostDetailResponseWrapperDto;
import backend.mydays.exception.ForbiddenException;
import backend.mydays.exception.ResourceNotFoundException;
import backend.mydays.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final CharacterRepository characterRepository;

    private final String uploadDir = "src/main/resources/static/images/posts/";

    @Transactional
    public Long createPost(PostCreateRequest request, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        Challenge challenge = challengeRepository.findByChallengeDate(LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("오늘의 챌린지를 찾을 수 없습니다."));

        String imageUrl = saveBase64Image(request.getBase64Img());

        Post post = Post.builder()
                .user(user)
                .challenge(challenge)
                .content(request.getContent())
                .imageUrl(imageUrl)
                .build();

        Post savedPost = postRepository.save(post);

        UserChallenge userChallenge = UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .post(savedPost)
                .completedAt(LocalDate.now())
                .build();
        userChallengeRepository.save(userChallenge);

        // 연속 챌린지 참여일 업데이트
        Optional<UserChallenge> lastChallenge = userChallengeRepository.findTopByUserOrderByCompletedAtDesc(user);
        if (lastChallenge.isPresent()) {
            LocalDate lastCompletedDate = lastChallenge.get().getCompletedAt();
            if (LocalDate.now().isEqual(lastCompletedDate.plusDays(1))) {
                user.setConsecutiveDays(user.getConsecutiveDays() + 1);
            } else if (!LocalDate.now().isEqual(lastCompletedDate)) {
                user.setConsecutiveDays(1);
            }
        } else {
            user.setConsecutiveDays(1);
        }

        // 레벨업 및 캐릭터 교체 로직
        user.setTotalCompletedDays(user.getTotalCompletedDays() + 1);
        if (user.getTotalCompletedDays() > 0 && user.getTotalCompletedDays() % 6 == 0) {
            user.levelUp();
            int newLevel = user.getLevel();

            if (newLevel == 2) {
                characterRepository.findByLevel(2).ifPresent(user::setCharacter);
            } else if (newLevel == 3) {
                List<backend.mydays.domain.Character> level3Chars = characterRepository.findAllByLevel(3);
                if (!level3Chars.isEmpty()) {
                    int randomIndex = (int) (Math.random() * level3Chars.size());
                    user.setCharacter(level3Chars.get(randomIndex));
                }
            } else if (newLevel > 3) {
                Integer groupId = Optional.ofNullable(user.getCharacter()).map(backend.mydays.domain.Character::getGroupId).orElse(null);
                if (groupId != null) {
                    characterRepository.findByLevelAndGroupId(newLevel, groupId).ifPresent(user::setCharacter);
                }
            }
        }
        userRepository.save(user);

        return savedPost.getId();
    }

    private String saveBase64Image(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            throw new IllegalArgumentException("이미지 데이터가 없습니다.");
        }

        // data:image/png;base64, 같은 프리픽스 제거
        String pureBase64 = base64Image.substring(base64Image.indexOf(",") + 1);
        byte[] imageBytes = Base64.getDecoder().decode(pureBase64);

        String filename = UUID.randomUUID().toString() + ".png"; // 확장자는 실제 이미지 타입에 맞게 결정하는 것이 좋습니다.
        File destinationFile = new File(uploadDir + filename);

        // 디렉토리 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
            fos.write(imageBytes);
        } catch (IOException e) {
            throw new IllegalStateException("이미지를 저장하는 데 실패했습니다.", e);
        }

        return "/images/posts/" + filename;
    }

    public Page<FeedPostDto> getFeed(Pageable pageable, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        return posts.map(post -> toFeedPostDto(post, user));
    }

    public MissionTextDto getMissionText() {
        Challenge challenge = challengeRepository.findByChallengeDate(LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("오늘의 챌린지를 찾을 수 없습니다."));
        return new MissionTextDto(challenge.getContent());
    }

    public PostDetailResponseWrapperDto getPostDetail(Long postId, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        FeedPostDto postDto = toFeedPostDto(post, user);

        List<CommentDto> comments = commentRepository.findAllByPost(post).stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());

        return new PostDetailResponseWrapperDto(postDto, comments);
    }

    @Transactional
    public void deletePost(Long postId, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        if (!post.getUser().equals(user)) {
            throw new ForbiddenException("해당 게시물을 삭제할 권한이 없습니다.");
        }

        // 연관된 UserChallenge, Likes, Comments 를 먼저 삭제해야 함
        userChallengeRepository.findByPost(post).ifPresent(userChallengeRepository::delete);
        likeRepository.deleteAll(likeRepository.findAllByPost(post));
        commentRepository.deleteAll(commentRepository.findAllByPost(post));

        postRepository.delete(post);
    }

    @Transactional
    public void toggleLikePost(Long postId, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            Like like = Like.builder().user(user).post(post).build();
            likeRepository.save(like);
        }
    }


    private FeedPostDto toFeedPostDto(Post post, Users user) {
        long likeCount = likeRepository.countByPost(post);
        long commentCount = commentRepository.countByPost(post);
        boolean isLiked = likeRepository.findByUserAndPost(user, post).isPresent();
        Title activeTitle = post.getUser().getActiveTitle();
        boolean isOwner = post.getUser().getId().equals(user.getId());

        return new FeedPostDto(
                String.valueOf(post.getId()),
                post.getUser().getAvatarImageUrl(),
                post.getUser().getNickname(),
                activeTitle != null ? activeTitle.getName() : "",
                activeTitle != null ? activeTitle.getColor() : "#000000",
                post.getCreatedAt(),
                post.getContent(),
                post.getImageUrl(),
                likeCount,
                isLiked,
                commentCount,
                isOwner
        );
    }

    private CommentDto toCommentDto(Comment comment) {
        Title activeTitle = comment.getUser().getActiveTitle();
        return new CommentDto(
                String.valueOf(comment.getId()),
                comment.getUser().getAvatarImageUrl(),
                comment.getUser().getNickname(),
                activeTitle != null ? activeTitle.getName() : "",
                activeTitle != null ? activeTitle.getColor() : "#000000",
                comment.getCreatedAt(),
                comment.getContent()
        );
    }
}