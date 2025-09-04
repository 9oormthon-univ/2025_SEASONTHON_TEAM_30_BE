package backend.mydays.dto.mypage;

import lombok.Getter;

import java.util.List;

@Getter
public class MyCalendarResponse {
    private int year;
    private int month;
    private List<CompletedPostDto> completedPosts;

    public MyCalendarResponse(int year, int month, List<CompletedPostDto> completedPosts) {
        this.year = year;
        this.month = month;
        this.completedPosts = completedPosts;
    }
}
