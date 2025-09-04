package backend.mydays.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // Setter is needed for ModelAttribute binding
public class PostCreateRequest {
    private String content;
}
