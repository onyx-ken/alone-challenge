package onyx.challenge.framework.adapter.inbound.web.exception;

import onyx.challenge.application.service.exceptiron.common.AlreadyExistsException;
import onyx.challenge.application.service.exceptiron.common.DomainException;
import onyx.challenge.application.service.exceptiron.common.NotFoundException;
import onyx.challenge.application.service.exceptiron.file.FileEmptyException;
import onyx.challenge.application.service.exceptiron.file.FileNotFoundException;
import onyx.challenge.application.service.exceptiron.file.process.FileDeleteFailException;
import onyx.challenge.application.service.exceptiron.file.process.FileProcessingException;
import onyx.challenge.application.service.exceptiron.file.process.FileSaveFailException;
import onyx.challenge.framework.adapter.inbound.web.api.ApiResponse;
import onyx.challenge.framework.adapter.inbound.web.api.ApiStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(@Qualifier("messageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // 1. 에러 유형별 처리
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleExceptionAlreadyExists(DomainException ex, Locale locale) {
        // 메시지 코드와 파라미터를 사용하여 국제화된 메시지 가져오기
        String errorMessage = messageSource.getMessage(ex.getMessageCode(), ex.getArgs(), locale);
        ApiResponse<Void> apiResponse = ApiResponse.create(ApiStatus.CONFLICT, errorMessage, null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleExceptionsNotFound(DomainException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessageCode(), ex.getArgs(), locale);
        ApiResponse<Void> apiResponse = ApiResponse.create(ApiStatus.NOT_FOUND, errorMessage, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileExceptionsProcessingError(DomainException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessageCode(), ex.getArgs(), locale);
        ApiResponse<Void> apiResponse = ApiResponse.create(ApiStatus.INTERNAL_SERVER_ERROR, errorMessage, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileExceptionsNotFound(DomainException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessageCode(), ex.getArgs(), locale);
        ApiResponse<Void> apiResponse = ApiResponse.create(ApiStatus.NOT_FOUND, errorMessage, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(FileEmptyException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileExceptionsEmpty(DomainException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessageCode(), ex.getArgs(), locale);
        ApiResponse<Void> apiResponse = ApiResponse.create(ApiStatus.BAD_REQUEST, errorMessage, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    // 2.유효성 검증 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 에러 메시지 추출
        String errorMessage = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ApiResponse<Void> apiResponse = ApiResponse.create(ApiStatus.BAD_REQUEST, errorMessage, null);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // 3. 기타 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllExceptions(Exception ex) {
        // ApiResponse 객체 생성
        ApiResponse<Void> apiResponse = ApiResponse.create(ApiStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

}
