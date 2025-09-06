package backend.mydays.dto.post;

import backend.mydays.dto.comment.CommentDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalendarPostDetailResponseWrapperDto {
    private MissionDateDto mission;
    private FeedPostDto post;
    private List<CommentDto> comments;
}
