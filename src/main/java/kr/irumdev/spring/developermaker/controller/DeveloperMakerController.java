package kr.irumdev.spring.developermaker.controller;

import kr.irumdev.spring.developermaker.dto.CreateDeveloper;
import kr.irumdev.spring.developermaker.dto.DeveloperDetailDto;
import kr.irumdev.spring.developermaker.dto.DeveloperDto;
import kr.irumdev.spring.developermaker.dto.EditDeveloper;
import kr.irumdev.spring.developermaker.service.DeveloperMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DeveloperMakerController {
    private final DeveloperMakerService developerMakerService;


    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return developerMakerService.getAllDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(
            @PathVariable String memberId
    ) {
        log.info("GET /developer/{} HTTP/1.1", memberId);

        return developerMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(
            @Valid @RequestBody CreateDeveloper.Request request
            ) {
        log.info("request : {}", request);

        return developerMakerService.createDeveloper(request);
    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(
            @PathVariable String memberId,
            @Valid @RequestBody EditDeveloper.Request request
    ) {
        log.info("PUT /developer/{} HTTP/1.1", memberId);

        return developerMakerService.editDeveloper(memberId, request);
    }
}
