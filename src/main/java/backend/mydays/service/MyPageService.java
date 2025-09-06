package backend.mydays.service;

import backend.mydays.domain.*;
import backend.mydays.dto.comment.CommentDto;
import backend.mydays.dto.mypage.*;
import backend.mydays.dto.post.CalendarPostDetailResponseWrapperDto;
import backend.mydays.dto.post.FeedPostDto;
import backend.mydays.dto.post.MissionDateDto;
import backend.mydays.dto.post.PostDetailResponseWrapperDto;
import backend.mydays.exception.ForbiddenException;
import backend.mydays.exception.ResourceNotFoundException;
import backend.mydays.repository.*;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserTitleRepository userTitleRepository;
    private final TitleRepository titleRepository;
    private final PostService postService;
    private final ChallengeRepository challengeRepository;
    private final PostRepository postRepository;

    public MyStatusResponseDto getMyStatus(String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        String growthMessage = "오늘도 성장 중이에요!";
        boolean isBubbleVisible = true;
        double progress = 0.7;
        // totalChallengeCount: 유저가 작성한 전체 포스트 수
        int totalChallengeCount =  postRepository.countByUser(user);

        // 유저의 모든 포스트 날짜를 가져와서 LocalDate로 변환 후 Set으로 저장
        Set<LocalDate> postDates = postRepository.findAllByUser(user).stream()
            .map(post -> post.getCreatedAt().toLocalDate())
            .collect(Collectors.toSet());

// 오늘부터 거꾸로 연속 참여일 계산
        int consecutiveDays = 0;
        LocalDate today = LocalDate.now();

        while (postDates.contains(today.minusDays(consecutiveDays))) {
            consecutiveDays++;
        }

        int daysCount = consecutiveDays;

        Challenge challenge = challengeRepository.findByChallengeDate(today)
            .orElseThrow(() -> new IllegalStateException("오늘의 챌린지를 찾을 수 없습니다."));
        boolean isCompleteMission = userChallengeRepository.findByUserAndChallenge(user, challenge).isPresent();

        String userTitle = user.getActiveTitle() != null ? user.getActiveTitle().getName() : "";
        String userTitleColor = user.getActiveTitle() != null ? user.getActiveTitle().getColor() : "#000000";

        return new MyStatusResponseDto(
                user.getNickname(),
                growthMessage,
                isBubbleVisible,
                userTitle,
                userTitleColor,
                progress,
                user.getAvatarImageUrl(),
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

    public WeeklyCalendarResponseDto getWeeklyCalendar(UserDetails userDetails) {
        Users user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userDetails.getUsername()));

        LocalDate today = LocalDate.now();
        LocalDate sunday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        List<DayContentDto> dayContents = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M월 d일 E요일", Locale.KOREAN);

        for (LocalDate date = sunday; !date.isAfter(today); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            Challenge challenge = challengeRepository.findByChallengeDate(currentDate)
                    .orElse(null);

            if (challenge == null) {
                dayContents.add(DayContentDto.builder()
                        .day(currentDate.getDayOfMonth())
                        .date(currentDate.format(dateFormatter))
                        .missionText("오늘의 챌린지가 없습니다.")
                        .isCompleted(false)
                        .post(null)
                        .build());
                continue;
            }

            UserChallenge userChallenge = userChallengeRepository.findByUserAndChallenge(user, challenge)
                    .orElse(null);

            DayContentDto dayContent;
            if (userChallenge != null) {
                Post post = userChallenge.getPost();
                WeeklyCalendarPostDto postDto = WeeklyCalendarPostDto.builder()
                        .postId(String.valueOf(post.getId()))
                        .userimgUrl(post.getUser().getAvatarImageUrl())
                        .userName(post.getUser().getNickname())
                        .userTitle(post.getUser().getActiveTitle() != null ? post.getUser().getActiveTitle().getName() : "")
                        .userTitleColor(post.getUser().getActiveTitle() != null ? post.getUser().getActiveTitle().getColor() : "#FFFFFF")
                        .createdAt(post.getCreatedAt())
                        .content(post.getContent())
                        .contentImgUrl(post.getImageUrl())
                        .likeCount(post.getLikes().size())
                        .isLiked(post.getLikes().stream().anyMatch(like -> like.getUser().equals(user)))
                        .commentCount(post.getComments().size())
                        .build();

                dayContent = DayContentDto.builder()
                        .day(currentDate.getDayOfMonth())
                        .date(currentDate.format(dateFormatter))
                        .missionText(challenge.getContent())
                        .isCompleted(true)
                        .post(postDto)
                        .build();
            } else {
                dayContent = DayContentDto.builder()
                        .day(currentDate.getDayOfMonth())
                        .date(currentDate.format(dateFormatter))
                        .missionText(challenge.getContent())
                        .isCompleted(false)
                        .post(null)
                        .build();
            }
            dayContents.add(dayContent);
        }

        return WeeklyCalendarResponseDto.builder()
                .dayContents(dayContents)
                .build();
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일 E요일", Locale.KOREAN);
        String formattedDate = date.format(formatter);

        return new MissionDateDto(formattedDate,challenge.getContent());

    }

    public CalendarPostDetailResponseWrapperDto getCalendarPostDetail(Long postId, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

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
