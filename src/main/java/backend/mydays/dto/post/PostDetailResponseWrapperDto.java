package backend.mydays.dto.post;

import backend.mydays.dto.comment.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostDetailResponseWrapperDto {
    private FeedPostDto post;
    private List<CommentDto> comments;
}
