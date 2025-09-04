package backend.mydays.dto.comment;

import lombok.Data;

@Data
public class CommentCreateRequestDto {
    private String postId;
    private String content;
}
