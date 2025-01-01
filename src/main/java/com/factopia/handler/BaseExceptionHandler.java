package com.factopia.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public abstract class BaseExceptionHandler {

    protected ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status,
                                                                    String message, String path){
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("상태코드", status.value());
        errorDetails.put("응답 메세지", message);
        errorDetails.put("요청경로", path);
        return new ResponseEntity<>(errorDetails, status);
    }
}
