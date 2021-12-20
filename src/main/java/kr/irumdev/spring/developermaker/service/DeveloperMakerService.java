package kr.irumdev.spring.developermaker.service;

import kr.irumdev.spring.developermaker.code.StatusCode;
import kr.irumdev.spring.developermaker.dto.CreateDeveloper;
import kr.irumdev.spring.developermaker.dto.DeveloperDetailDto;
import kr.irumdev.spring.developermaker.dto.DeveloperDto;
import kr.irumdev.spring.developermaker.dto.EditDeveloper;
import kr.irumdev.spring.developermaker.entity.Developer;
import kr.irumdev.spring.developermaker.entity.RetiredDeveloper;
import kr.irumdev.spring.developermaker.exception.DeveloperMakerErrorCode;
import kr.irumdev.spring.developermaker.exception.DeveloperMakerException;
import kr.irumdev.spring.developermaker.repository.DeveloperRepository;
import kr.irumdev.spring.developermaker.repository.RetiredDeveloperRepository;
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
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    // ACID
    // Atomic 원자성 -> All or Not
    // Consistency 일관성
    // Isolation 고립성
    // Durability 내구성
    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        // business logic start
        return CreateDeveloper.Response.fromEntity(
                developerRepository.save(
                        createDeveloperFromRequest(request)
                )
        );
    }

    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        Developer developer = getDeveloperByMemberId(memberId);
        developer.setStatusCode(StatusCode.RETIRED);

        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();
        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDto.fromEntity(developer);
    }

    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(
                getUpdatedDeveloperFromRequest(
                        request, getDeveloperByMemberId(memberId)
                )
        );
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
    }

    private Developer createDeveloperFromRequest(CreateDeveloper.Request request) {
        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .name(request.getName())
                .age(request.getAge())
                .statusCode(StatusCode.EMPLOYED)
                .build();
        return developer;
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        //business validation
        request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId())
        .ifPresent((developer -> {
            throw new DeveloperMakerException(DeveloperMakerErrorCode.DUPLICATED_MEMBER_ID);
        }));
    }

    private Developer getUpdatedDeveloperFromRequest(EditDeveloper.Request request, Developer developer) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return developer;
    }

    private Developer getDeveloperByMemberId(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DeveloperMakerException(DeveloperMakerErrorCode.NO_DEVELOPER));
    }
}
