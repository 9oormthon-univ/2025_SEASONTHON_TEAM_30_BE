package backend.mydays.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequest {
    private String content;
    private String base64Img;
}
