package backend.mydays.service;

import backend.mydays.domain.Challenge;
import backend.mydays.domain.Post;
import backend.mydays.domain.Title;
import backend.mydays.domain.UserChallenge;
import backend.mydays.domain.Users;
import backend.mydays.dto.comment.CommentDto;
import backend.mydays.dto.mypage.*;
import backend.mydays.dto.post.CalendarPostDetailResponseWrapperDto;
import backend.mydays.dto.post.FeedPostDto;
import backend.mydays.dto.post.MissionDateDto;
import backend.mydays.dto.post.MissionTextDto;
import backend.mydays.dto.post.PostDetailResponseWrapperDto;
import backend.mydays.exception.ForbiddenException;
import backend.mydays.exception.ResourceNotFoundException;
import backend.mydays.repository.ChallengeRepository;
import backend.mydays.repository.PostRepository;
import backend.mydays.repository.TitleRepository;
import backend.mydays.repository.UserChallengeRepository;
import backend.mydays.repository.UserRepository;
import backend.mydays.repository.UserTitleRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserTitleRepository userTitleRepository;
    private final TitleRepository titleRepository;
    private final PostService postService; // Inject PostService
    private final ChallengeRepository challengeRepository;
    private final PostRepository postRepository;

    public MyStatusResponseDto getMyStatus(String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        // Placeholder values for new fields
        String growthMessage = "오늘도 성장 중이에요!"; // Placeholder
        boolean isBubbleVisible = true; // Placeholder
        double progress = 0.5; // Placeholder
        int totalChallengeCount = 42; // Placeholder
        int daysCount = 7; // Placeholder
        boolean isCompleteMission = false; // Placeholder

        String userTitle = user.getActiveTitle() != null ? user.getActiveTitle().getName() : "";
        String userTitleColor = user.getActiveTitle() != null ? user.getActiveTitle().getColor() : "#000000";

        return new MyStatusResponseDto(
                user.getNickname(),
                growthMessage,
                isBubbleVisible,
                userTitle,
                userTitleColor,
                progress,
                user.getAvatarImageUrl(), // This is now from Character
                totalChallengeCount,
                daysCount,
                isCompleteMission
        );
    }

    public MyCalendarResponseDto getMyCalendar(int year, int month, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<UserChallenge> challenges = userChallengeRepository.findByUserAndMonth(user, startDate, endDate);
        List<CalendarPostDto> calendarPosts = challenges.stream()
                .map(uc -> new CalendarPostDto(
                        String.valueOf(uc.getPost().getId()),
                        uc.getPost().getImageUrl(),
                        uc.getPost().getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new MyCalendarResponseDto(user.getCreatedAt(), calendarPosts);
    }

    public MyCalendarResponseDto getAllMyChallenges(String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        List<UserChallenge> challenges = userChallengeRepository.findByUser(user);
        List<CalendarPostDto> calendarPosts = challenges.stream()
            .map(uc -> new CalendarPostDto(
                String.valueOf(uc.getPost().getId()),
                uc.getPost().getImageUrl(),
                uc.getPost().getCreatedAt()
            ))
            .collect(Collectors.toList());

        return new MyCalendarResponseDto(user.getCreatedAt(), calendarPosts);
    }

    public PostDetailResponseWrapperDto getMyPostByPostId(Long postId, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        // 사용자가 해당 postId를 수행한 챌린지인지 확인 (권한 체크)
        boolean isUserChallenge = userChallengeRepository.existsByUserAndPostId(user, postId);
        if (!isUserChallenge) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }

        return postService.getPostDetail(postId, userEmail);
    }

    public MissionDateDto getMyMissionByPostId(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Challenge challenge = challengeRepository.findById(post.getChallenge().getId())
            .orElseThrow(() -> new IllegalStateException("오늘의 챌린지를 찾을 수 없습니다."));

        LocalDate date = challenge.getChallengeDate();
        // "M월 d일 E요일" 포맷 정의, Locale.KOREAN으로 요일 한글 표시
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일 E요일", Locale.KOREAN);
        String formattedDate = date.format(formatter);

        return new MissionDateDto(formattedDate,challenge.getContent());

    }

    public CalendarPostDetailResponseWrapperDto getCalendarPostDetail(Long postId, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        // 사용자가 해당 postId를 수행한 챌린지인지 확인 (권한 체크)
        boolean isUserChallenge = userChallengeRepository.existsByUserAndPostId(user, postId);
        if (!isUserChallenge) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }

        PostDetailResponseWrapperDto postDetailResponseWrapperDto =  postService.getPostDetail(postId, userEmail);
        FeedPostDto feedPostDto = postDetailResponseWrapperDto.getPost();
        List<CommentDto> comments = postDetailResponseWrapperDto.getComments();
        MissionDateDto missionDateDto = getMyMissionByPostId(postId);
        return new CalendarPostDetailResponseWrapperDto(missionDateDto, feedPostDto, comments);
    }


    public MyTitlesResponseDto getMyTitles(String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        List<TitleDto> titles = userTitleRepository.findByUser(user).stream()
                .map(userTitle -> new TitleDto(
                        String.valueOf(userTitle.getTitle().getId()),
                        userTitle.getTitle().getName(),
                        userTitle.getTitle().getColor()
                ))
                .collect(Collectors.toList());

        return new MyTitlesResponseDto(titles);
    }

    @Transactional
    public void updateActiveTitle(ActiveTitleUpdateRequest request, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        Title title = titleRepository.findById(request.getTitleId())
                .orElseThrow(() -> new ResourceNotFoundException("Title", "id", request.getTitleId()));

        if (!userTitleRepository.existsByUserAndTitle(user, title)) {
            throw new ForbiddenException("보유하지 않은 칭호입니다.");
        }

        user.updateActiveTitle(title);
    }
}
