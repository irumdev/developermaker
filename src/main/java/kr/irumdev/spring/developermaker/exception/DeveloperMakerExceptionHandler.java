package kr.irumdev.spring.developermaker.exception;

import kr.irumdev.spring.developermaker.dto.DeveloperMakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class DeveloperMakerExceptionHandler {

    @ExceptionHandler(DeveloperMakerException.class)
    public DeveloperMakerErrorResponse handleException(
            DeveloperMakerException e,
            HttpServletRequest request
    ) {
        log.error("errorCode: {}, url: {}, message: {}",
                e.getDeveloperMakerErrorCode(),
                request.getRequestURI(),
                e.getDetailMessage()
        );

        return DeveloperMakerErrorResponse.builder()
                .errorCode(e.getDeveloperMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class
    })
    public DeveloperMakerErrorResponse handleBadRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("url: {}, message: {}",
                request.getRequestURI(),
                e.getMessage()
        );

        return DeveloperMakerErrorResponse.builder()
                .errorCode(DeveloperMakerErrorCode.INVALID_REQUEST)
                .errorMessage(DeveloperMakerErrorCode.INVALID_REQUEST.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public DeveloperMakerErrorResponse handleException(
            Exception e, HttpServletRequest request
    ) {
        log.error("url: {}, message: {}",
                request.getRequestURI(),
                e.getMessage()
        );

        return DeveloperMakerErrorResponse.builder()
                .errorCode(DeveloperMakerErrorCode.INTERNAL_SERVER_ERROR)
                .errorMessage(DeveloperMakerErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
