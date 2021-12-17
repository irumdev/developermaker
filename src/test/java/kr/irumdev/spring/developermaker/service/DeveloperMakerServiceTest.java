package kr.irumdev.spring.developermaker.service;

import kr.irumdev.spring.developermaker.code.StatusCode;
import kr.irumdev.spring.developermaker.dto.CreateDeveloper;
import kr.irumdev.spring.developermaker.dto.DeveloperDetailDto;
import kr.irumdev.spring.developermaker.entity.Developer;
import kr.irumdev.spring.developermaker.exception.DeveloperMakerErrorCode;
import kr.irumdev.spring.developermaker.exception.DeveloperMakerException;
import kr.irumdev.spring.developermaker.repository.DeveloperRepository;
import kr.irumdev.spring.developermaker.type.DeveloperLevel;
import kr.irumdev.spring.developermaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeveloperMakerServiceTest {
    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperMakerService developerMakerService;

    private final Developer defaultDeveloper =
            Developer.builder()
                    .developerLevel(DeveloperLevel.SENIOR)
                    .developerSkillType(DeveloperSkillType.FULL_STACK)
                    .experienceYears(12)
                    .statusCode(StatusCode.EMPLOYED)
                    .memberId("memberId")
                    .name("name")
                    .age(32)
                    .build();

    private final CreateDeveloper.Request defaultCreateRequest =
            CreateDeveloper.Request.builder()
                    .developerLevel(DeveloperLevel.SENIOR)
                    .developerSkillType(DeveloperSkillType.FRONT_END)
                    .experienceYears(12)
                    .memberId("memberId")
                    .name("name")
                    .age(32)
                    .build();

    @Test
    public void testSomething() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        DeveloperDetailDto developerDetail = developerMakerService.getDeveloperDetail("memberId");

        //then
        assertEquals(DeveloperLevel.SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FULL_STACK, developerDetail.getDeveloperSkillType());
        assertEquals("memberId", developerDetail.getMemberId());
    }

    @Test
    void createDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        //when
        developerMakerService.createDeveloper(defaultCreateRequest);

        //then
        verify(developerRepository, times(1))
                .save(captor.capture());
        Developer savedDeveloper = captor.getValue();
        assertEquals(DeveloperLevel.SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(12, savedDeveloper.getExperienceYears());
    }

    @Test
    void createDeveloperTest_failed_with_duplicated() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        //then
        DeveloperMakerException developerMakerException = assertThrows(DeveloperMakerException.class,
                () -> developerMakerService.createDeveloper(defaultCreateRequest)
        );
        assertEquals(DeveloperMakerErrorCode.DUPLICATED_MEMBER_ID, developerMakerException.getDeveloperMakerErrorCode());
    }
}