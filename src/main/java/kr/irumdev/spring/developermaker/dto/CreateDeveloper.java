package kr.irumdev.spring.developermaker.dto;

import kr.irumdev.spring.developermaker.code.StatusCode;
import kr.irumdev.spring.developermaker.entity.Developer;
import kr.irumdev.spring.developermaker.type.DeveloperLevel;
import kr.irumdev.spring.developermaker.type.DeveloperSkillType;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateDeveloper {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request {
        @NotNull
        private DeveloperLevel developerLevel;
        @NotNull
        private DeveloperSkillType developerSkillType;
        @NotNull
        @Min(0)
        @Max(20)
        private Integer experienceYears;

        @NotNull
        @Size(min = 3, max = 50, message="memberId size must be between 3 to 50")
        private String memberId;
        @NotNull
        @Size(min = 3, max =20, message="name size must be between 3 to 20")
        private String name;

        @Min(18)
        private Integer age;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private DeveloperLevel developerLevel;
        private DeveloperSkillType developerSkillType;
        private Integer experienceYears;
        private String memberId;
        private String name;
        private StatusCode statusCode;

        public static Response fromEntity(@NonNull Developer developer){
            return Response.builder()
                    .developerLevel(developer.getDeveloperLevel())
                    .developerSkillType(developer.getDeveloperSkillType())
                    .experienceYears(developer.getExperienceYears())
                    .memberId(developer.getMemberId())
                    .name(developer.getName())
                    .statusCode(developer.getStatusCode())
                    .build();
        }
    }
}
