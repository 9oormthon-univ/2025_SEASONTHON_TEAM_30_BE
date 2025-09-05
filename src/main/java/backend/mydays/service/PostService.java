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

import java.time.LocalDate;
import java.util.List;
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

    @Transactional
    public Long createPost(PostCreateRequest request, String userEmail, String imageUrl) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        Challenge challenge = challengeRepository.findByChallengeDate(LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("오늘의 챌린지를 찾을 수 없습니다."));

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

        return savedPost.getId();
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
    public void likePost(Long postId, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        if (likeRepository.findByUserAndPost(user, post).isPresent()) {
            throw new IllegalStateException("이미 좋아요를 누른 게시물입니다.");
        }

        Like like = Like.builder().user(user).post(post).build();
        likeRepository.save(like);
    }

    @Transactional
    public void unlikePost(Long postId, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Like like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalStateException("좋아요를 누르지 않은 게시물입니다."));

        likeRepository.delete(like);
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
