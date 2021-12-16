package kr.irumdev.spring.developermaker.controller;

import kr.irumdev.spring.developermaker.dto.DeveloperDto;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeveloperMakerController.class)
class DeveloperMakerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeveloperMakerService developerMakerService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);

    @Test
    void GetAllDevelopers() throws Exception {
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
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(
                        jsonPath("$.[0].developerSkillType",
                                CoreMatchers.is((DeveloperSkillType.BACK_END.name())))
                )
                .andExpect(
                        jsonPath("$.[0].developerLevel",
                                CoreMatchers.is((DeveloperLevel.JUNIOR.name())))
                )
                .andExpect(
                        jsonPath("$.[1].developerSkillType",
                                CoreMatchers.is((DeveloperSkillType.FRONT_END.name())))
                )
                .andExpect(
                        jsonPath("$.[1].developerLevel",
                                CoreMatchers.is((DeveloperLevel.SENIOR.name())))
                );
    }

}
