package kr.irumdev.spring.developermaker.dto;

import kr.irumdev.spring.developermaker.exception.DeveloperMakerErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeveloperMakerErrorResponse {
    private DeveloperMakerErrorCode errorCode;
    private String errorMessage;
}
