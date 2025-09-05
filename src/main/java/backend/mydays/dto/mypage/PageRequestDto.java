package backend.mydays.dto.mypage;

import lombok.Data;

@Data
public class PageRequestDto {
    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
