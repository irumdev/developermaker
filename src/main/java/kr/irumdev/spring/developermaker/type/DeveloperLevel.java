package kr.irumdev.spring.developermaker.type;

import kr.irumdev.spring.developermaker.exception.DeveloperMakerErrorCode;
import kr.irumdev.spring.developermaker.exception.DeveloperMakerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static kr.irumdev.spring.developermaker.constant.DeveloperMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static kr.irumdev.spring.developermaker.constant.DeveloperMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;

@AllArgsConstructor
@Getter
public enum DeveloperLevel {
    NEW("신입 개발자", years -> years == 0),
    JUNIOR("주니어 개발자", years -> years > 0 && years <= MAX_JUNIOR_EXPERIENCE_YEARS),
    JUNGNIOR("중니어 개발자", years -> years > MAX_JUNIOR_EXPERIENCE_YEARS &&
            years < MIN_SENIOR_EXPERIENCE_YEARS),
    SENIOR("시니어 개발자", years -> years > MIN_SENIOR_EXPERIENCE_YEARS &&
            years < 50);

    private final String description;
    private final Function<Integer, Boolean> validateFunction;

    public void validateExperienceYears(Integer years) {
        if (!validateFunction.apply(years)) {
            throw new DeveloperMakerException(DeveloperMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }
}
