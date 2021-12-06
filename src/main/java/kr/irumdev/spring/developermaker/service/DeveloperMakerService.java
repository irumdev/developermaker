package kr.irumdev.spring.developermaker.service;

import kr.irumdev.spring.developermaker.dto.CreateDeveloper;
import kr.irumdev.spring.developermaker.dto.DeveloperDto;
import kr.irumdev.spring.developermaker.entity.Developer;
import kr.irumdev.spring.developermaker.exception.DeveloperMakerErrorCode;
import kr.irumdev.spring.developermaker.exception.DeveloperMakerException;
import kr.irumdev.spring.developermaker.repository.DeveloperRepository;
import kr.irumdev.spring.developermaker.type.DeveloperLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeveloperMakerService {
    private final DeveloperRepository developerRepository;

    // ACID
    // Atomic 원자성 -> All or Not
    // Consistency 일관성
    // Isolation 고립성
    // Durability 내구성
    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .name(request.getName())
                .age(request.getAge())
                .build();

        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        //business validation
        DeveloperLevel developerLevel = request.getDeveloperLevel();
        Integer experienceYears = request.getExperienceYears();
        if (developerLevel == DeveloperLevel.SENIOR
                && experienceYears < 10) {
            throw new DeveloperMakerException(DeveloperMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNGNIOR
                && (experienceYears < 4 || experienceYears > 10)) {
            throw new DeveloperMakerException(DeveloperMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
            throw new DeveloperMakerException(DeveloperMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        developerRepository.findByMemberId(request.getMemberId())
        .ifPresent((developer -> {
            throw new DeveloperMakerException(DeveloperMakerErrorCode.DUPLICATED_MEMBER_ID);
        }));
    }

    public List<DeveloperDto> getAllDevelopers() {
        return developerRepository.findAll()
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }
}
