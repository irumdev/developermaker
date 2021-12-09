package kr.irumdev.spring.developermaker.service;

import kr.irumdev.spring.developermaker.dto.CreateDeveloper;
import kr.irumdev.spring.developermaker.dto.DeveloperDetailDto;
import kr.irumdev.spring.developermaker.dto.DeveloperDto;
import kr.irumdev.spring.developermaker.dto.EditDeveloper;
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
        validateDeveloperRequest(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId())
        .ifPresent((developer -> {
            throw new DeveloperMakerException(DeveloperMakerErrorCode.DUPLICATED_MEMBER_ID);
        }));
    }

    private void validateDeveloperRequest(DeveloperLevel developerLevel, Integer experienceYears) {
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
    }

    public List<DeveloperDto> getAllDevelopers() {
        return developerRepository.findAll()
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
//        return developerRepository.findByMemberId(memberId)
//                .map(DeveloperDetailDto::fromEntity)
//                .orElseThrow(() -> new DeveloperMakerException(DeveloperMakerErrorCode.NO_DEVELOPER));

        return DeveloperDetailDto.fromEntity(developerRepository.findByMemberId(memberId).
                orElseThrow(() -> new DeveloperMakerException(DeveloperMakerErrorCode.NO_DEVELOPER)));
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateDeveloperRequest(request.getDeveloperLevel(), request.getExperienceYears());

        Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DeveloperMakerException(DeveloperMakerErrorCode.NO_DEVELOPER)
        );

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);
    }
}
