package backend.mydays.dto.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class BaseResponse<T> {
    private final Meta meta;
    private final T body;

    public BaseResponse(Meta meta, T body) {
        this.meta = meta;
        this.body = body;
    }

    public static <T> ResponseEntity<BaseResponse<T>> of(HttpStatus status, String message, T body) {
        Meta meta = new Meta(status.value(), message);
        BaseResponse<T> response = new BaseResponse<>(meta, body);
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<BaseResponse<T>> ok(String message, T body) {
        return of(HttpStatus.OK, message, body);
    }

    public static <T> ResponseEntity<BaseResponse<T>> created(String message, T body) {
        return of(HttpStatus.CREATED, message, body);
    }
}
