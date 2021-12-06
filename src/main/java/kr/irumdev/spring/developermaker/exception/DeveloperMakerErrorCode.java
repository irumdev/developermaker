package kr.irumdev.spring.developermaker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeveloperMakerErrorCode {
    NO_DEVELOPER("해당되는 타입의 개발자가 없습니다."),
    DUPLICATED_MEMBER_ID("Member ID가 중복되는 개발자가 있습니다."),
    LEVEL_EXPERIENCE_YEARS_NOT_MATCHED("개발자 레벨과 연차가 맞지 않습니다."),

    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String message;
}
