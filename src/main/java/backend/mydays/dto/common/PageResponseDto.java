package backend.mydays.dto.common;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponseDto<T> {
    private final List<T> posts;

    public PageResponseDto(Page<T> page) {
        this.posts = page.getContent();
    }
}
