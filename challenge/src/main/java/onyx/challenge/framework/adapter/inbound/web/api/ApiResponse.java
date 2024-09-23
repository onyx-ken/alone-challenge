package onyx.challenge.framework.adapter.inbound.web.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {
    private int statusCode; // 숫자 상태 코드
    private String status;  // 문자열 상태 (예: "BAD_REQUEST")
    private String message;
    private T data;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> create(ApiStatus status, String message, T data) {
        return ApiResponse.<T>builder()
                .statusCode(status.getStatusCode())
                .status(status.name())
                .message(message)
                .data(data)
                .build();
    }
}
