package backend.mydays.service;

import backend.mydays.domain.Title;
import backend.mydays.domain.UserChallenge;
import backend.mydays.domain.Users;
import backend.mydays.dto.mypage.*;
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

    public MyStatusResponse getMyStatus(String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        return new MyStatusResponse(user);
    }

    public MyCalendarResponse getMyCalendar(int year, int month, String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<UserChallenge> challenges = userChallengeRepository.findByUserAndMonth(user, startDate, endDate);
        List<CompletedPostDto> completedPosts = challenges.stream()
                .map(CompletedPostDto::from)
                .collect(Collectors.toList());

        return new MyCalendarResponse(year, month, completedPosts);
    }

    public MyTitlesResponse getMyTitles(String userEmail) {
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        List<EarnedTitleDto> titles = userTitleRepository.findByUser(user).stream()
                .map(EarnedTitleDto::from)
                .collect(Collectors.toList());

        return new MyTitlesResponse(titles);
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
