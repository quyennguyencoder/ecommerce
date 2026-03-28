package com.nguyenquyen.ecommerce.exception;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalHandlerException {
//    private static final String MIN_ATTRIBUTE = "min";
//
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Void>> handleRuntimeException(Exception e) {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getFieldError().getDefaultMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }


//    @ExceptionHandler(value = AppException.class)
//    ResponseEntity<ApiResponse> handleAppException(AppException e) {
//        ErrorCode errorCode = e.getErrorCode();
//
//        ApiResponse apiResponse = ApiResponse.builder()
//                .code(errorCode.getCode())
//                .message(errorCode.getMessage())
//                .build();
//        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
//        ApiResponse apiResponse = ApiResponse.builder()
//                .code(ErrorCode.UNAUTHORIZED.getCode())
//                .message(ErrorCode.UNAUTHORIZED.getMessage())
//                .build();
//        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatusCode()).body(apiResponse);
//    }
//
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException e) {
//        ApiResponse apiResponse = ApiResponse.builder()
//                .code(ErrorCode.UNAUTHENTICATED.getCode())
//                .message(ErrorCode.UNAUTHENTICATED.getMessage())
//                .build();
//        return ResponseEntity.status(ErrorCode.UNAUTHENTICATED.getStatusCode()).body(apiResponse);
//    }
//
//    private String mapAttribute(String message, Map<String, Object> attributes) {
//        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
//
//        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
//    }
}