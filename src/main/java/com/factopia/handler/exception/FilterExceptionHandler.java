package com.factopia.handler.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 서비스 및 필터 레벨에서 발생하는 예외 처리 클래스
 */
@Component
public class FilterExceptionHandler extends BaseExceptionHandler {

    /**
     * 인증 실패 처리 (로그만 남기고 HTTP 응답 없음)
     */
    public void handleAuthenticationFailure(HttpServletResponse response, String message) throws IOException
    {
        createJsonErrorResponse(response, HttpStatus.UNAUTHORIZED, "인증 실패", message);
    }

    /**
     * 데이터베이스 오류 처리 (로그만 남기고 HTTP 응답 없음)
     */
    public void handleDatabaseError(HttpServletResponse response, String message) throws IOException
    {
        createJsonErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류", message);
    }

    /**
     * NullPointerException 처리 (로그만 남기고 HTTP 응답 없음)
     */
    public void handleNullPointerException(HttpServletResponse response, String message) throws IOException
    {
        createJsonErrorResponse(response, HttpStatus.OK, "데이터가 null 입니다.", message);
    }
}
