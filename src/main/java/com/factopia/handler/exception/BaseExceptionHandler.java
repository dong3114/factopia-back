package com.factopia.handler.exception;

import com.factopia.handler.domain.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * 에러 객체 반환 (기본 형태)
 */
public abstract class BaseExceptionHandler
{
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(BaseExceptionHandler.class.getName());

    protected ResponseEntity<ErrorResponse> createErrorResponse
            (HttpStatus status, String message, String path) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .path(path)
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    protected void createJsonErrorResponse
            (HttpServletResponse response, HttpStatus status, String message, String detail) throws IOException
    {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .path(detail)
                .build();

        // JSON 변환 및 로그 출력
        String jsonResponse = objectMapper.writeValueAsString(errorResponse.toString());
        logger.warning("에러 발생" + jsonResponse);

        // HTTP 응답 설정
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}
