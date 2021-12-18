package kr.irumdev.spring.developermaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.irumdev.spring.developermaker.code.StatusCode;
import kr.irumdev.spring.developermaker.dto.CreateDeveloper;
import kr.irumdev.spring.developermaker.dto.DeveloperDetailDto;
import kr.irumdev.spring.developermaker.dto.DeveloperDto;
import kr.irumdev.spring.developermaker.entity.Developer;
import kr.irumdev.spring.developermaker.service.DeveloperMakerService;
import kr.irumdev.spring.developermaker.type.DeveloperLevel;
import kr.irumdev.spring.developermaker.type.DeveloperSkillType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeveloperMakerController.class)
class DeveloperMakerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeveloperMakerService developerMakerService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);

    private Developer defaultDeveloper = Developer.builder()
            .developerSkillType(DeveloperSkillType.FRONT_END)
                .developerLevel(DeveloperLevel.SENIOR)
                .name("name")
                .age(32)
                .experienceYears(12)
                .statusCode(StatusCode.EMPLOYED)
                .memberId("memberId")
                .build();

    @Test
    void CreateDeveloper_success() throws Exception {
        given(developerMakerService.createDeveloper(any(CreateDeveloper.Request.class)))
                .willReturn(CreateDeveloper.Response.fromEntity(defaultDeveloper));

        mockMvc.perform(post("/create-developer").contentType(contentType)
                        .content(objectMapper.writeValueAsString(defaultDeveloper)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.developerSkillType",
                                CoreMatchers.is((DeveloperSkillType.FRONT_END.name())))
                ).andExpect(
                        jsonPath("$.developerLevel",
                                CoreMatchers.is((DeveloperLevel.SENIOR.name())))
                ).andExpect(
                        jsonPath("$.name",
                                CoreMatchers.is(("name")))
                ).andExpect(
                        jsonPath("$.experienceYears",
                                CoreMatchers.is(12))
                ).andExpect(
                        jsonPath("$.statusCode",
                                CoreMatchers.is((StatusCode.EMPLOYED.name())))
                ).andExpect(
                        jsonPath("$.memberId",
                                CoreMatchers.is(("memberId")))
                );
    }

    @Test
    void GetDeveloperDetail_success() throws Exception {
        given(developerMakerService.getDeveloperDetail(anyString()))
                .willReturn(DeveloperDetailDto.fromEntity(defaultDeveloper));

        mockMvc.perform(get("/developer/memberId").contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.developerSkillType",
                                CoreMatchers.is((DeveloperSkillType.FRONT_END.name())))
                ).andExpect(
                        jsonPath("$.developerLevel",
                                CoreMatchers.is((DeveloperLevel.SENIOR.name())))
                ).andExpect(
                        jsonPath("$.name",
                                CoreMatchers.is(("name")))
                ).andExpect(
                        jsonPath("$.age",
                                CoreMatchers.is(32))
                ).andExpect(
                        jsonPath("$.experienceYears",
                                CoreMatchers.is(12))
                ).andExpect(
                        jsonPath("$.statusCode",
                                CoreMatchers.is((StatusCode.EMPLOYED.name())))
                ).andExpect(
                        jsonPath("$.memberId",
                                CoreMatchers.is(("memberId")))
                );
    }

    @Test
    void GetAllDevelopers_success() throws Exception {
        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.BACK_END)
                .developerLevel(DeveloperLevel.JUNIOR)
                .memberId("memberId1").build();
        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .developerLevel(DeveloperLevel.SENIOR)
                .memberId("memberId2").build();
        given(developerMakerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(juniorDeveloperDto, seniorDeveloperDto));

        mockMvc.perform(get("/developers").contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.[0].developerSkillType",
                                CoreMatchers.is((DeveloperSkillType.BACK_END.name())))
                )
                .andExpect(
                        jsonPath("$.[0].developerLevel",
                                CoreMatchers.is((DeveloperLevel.JUNIOR.name())))
                )
                .andExpect(
                        jsonPath("$.[0].memberId",
                                CoreMatchers.is("memberId1"))
                )
                .andExpect(
                        jsonPath("$.[1].developerSkillType",
                                CoreMatchers.is((DeveloperSkillType.FRONT_END.name())))
                )
                .andExpect(
                        jsonPath("$.[1].developerLevel",
                                CoreMatchers.is((DeveloperLevel.SENIOR.name())))
                )
                .andExpect(
                        jsonPath("$.[1].memberId",
                                CoreMatchers.is("memberId2"))
                );
    }

}
