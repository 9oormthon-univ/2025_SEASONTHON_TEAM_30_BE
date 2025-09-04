package backend.mydays.service;

import backend.mydays.domain.Title;
import backend.mydays.domain.UserChallenge;
import backend.mydays.domain.Users;
import backend.mydays.dto.mypage.*;
import backend.mydays.dto.post.PostDetailResponseWrapperDto;
import backend.mydays.exception.ForbiddenException;
import backend.mydays.exception.ResourceNotFoundException;
import backend.mydays.repository.TitleRepository;
import backend.mydays.repository.UserChallengeRepository;
import backend.mydays.repository.UserRepository;
import backend.mydays.repository.UserTitleRepository;
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

    public PostDetailResponseWrapperDto getMyPostByDate(String dateString, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);

        UserChallenge userChallenge = userChallengeRepository.findByUserAndCompletedAt(user, date)
                .orElseThrow(() -> new ResourceNotFoundException("UserChallenge", "date", dateString));

        return postService.getPostDetail(userChallenge.getPost().getId(), userEmail);
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
