package onyx.challenge.framework.adapter.inbound.web.exception;

import onyx.challenge.framework.adapter.inbound.web.api.ApiResponse;
import onyx.challenge.framework.adapter.inbound.web.api.ApiStatus;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 유효성 검증 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 에러 메시지 추출
        String errorMessage = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        // ApiResponse 객체 생성
        ApiResponse<Void> apiResponse = ApiResponse.create(ApiStatus.BAD_REQUEST, errorMessage, null);
        // 400 Bad Request 응답 반환
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // 2. 기타 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllExceptions(Exception ex) {
        // ApiResponse 객체 생성
        ApiResponse<Void> apiResponse = ApiResponse.create(ApiStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", null);
        // 500 Internal Server Error 응답 반환
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

}
