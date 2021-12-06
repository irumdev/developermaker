package kr.irumdev.spring.developermaker.exception;

import lombok.Getter;

@Getter
public class DeveloperMakerException extends RuntimeException{
    private DeveloperMakerErrorCode developerMakerErrorCode;
    private String detailMessage;

    public DeveloperMakerException(DeveloperMakerErrorCode errorCode) {
        super(errorCode.getMessage());
        this.developerMakerErrorCode = errorCode;
        this.detailMessage = errorCode.getMessage();
    }

    public DeveloperMakerException(DeveloperMakerErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.developerMakerErrorCode = errorCode;
        this.detailMessage = detailMessage;
    }
}
