/*
 * Author: Seokjin Yoon, Hyeokwoo Kwon
 * Filename: ProjectController.java
 * Desc:
 */

package com.codrest.teriser.projects;

import com.codrest.teriser.developers.Developer;
import com.codrest.teriser.developers.DeveloperService;
import com.codrest.teriser.errors.NotFoundException;
import com.codrest.teriser.errors.UnauthorizedException;
import com.codrest.teriser.security.JwtAuthentication;
import com.codrest.teriser.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.codrest.teriser.utils.ApiUtils.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("projects")
public class ProjectController {
    private final ProjectService projectService;
    private final DeveloperService developerService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping(path = "{projectName}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<ProjectDto> findByName(@PathVariable String projectName) {
        return success(
                projectService.findByName(projectName)
                        .map(ProjectDto::new)
                        .orElseThrow(() -> new NotFoundException("Could not found project for " + projectName + "."))
        );
    }

    //존재해선 안됨, or 권한처리 할 것
    @GetMapping
    public ApiResult<List<ProjectDto>> findAll() {
        return success(
                projectService.findAll()
                        .stream()
                        .map(ProjectDto::new)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<ProjectDto> addProject(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @Valid @RequestBody ProjectRequest request
    ) {
        if (Objects.isNull(authentication)) {
            throw new UnauthorizedException("Invalid token");
        }

        Long developerId = authentication.id;
        Developer developer = findDeveloperById(developerId);
        String title = request.getTitle();
        Project project = projectService.addProject(developer, title);

        ProjectDto projectDto = new ProjectDto(project);

        log.info("addProject (developerId: " + developerId + ", projectName: " + projectDto.getName() + ")");

        return success(projectDto);
    }

    @DeleteMapping("{projectName}")
    public ResponseEntity<?> removeProject(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable String projectName
    ) {
        log.info("removeProject (developerId: " + authentication.id + ", projectName: " + projectName + ")");
        projectService.removeProject(projectName, findDeveloperById(authentication.id));

        return ResponseEntity.noContent().build();
    }

    /**
     * 프로젝트에 할당되어 있던 토큰을 만료시키고, 새로운 토큰을 발급하는 엔드포인트입니다.
     *
     * @param authentication
     * @param projectName
     * @return
     */
    @GetMapping(path = "{projectName}/token")
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<ProjectDto> reissueToken(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable String projectName
    ) {
        log.info("tokenInfo (developerId: " + authentication.id + ")");
        Project project = projectService.findByName(projectName)
                .orElseThrow(() -> new NotFoundException("Could not found project for " + projectName + "."));

        Long developerId = authentication.id;
        Developer developer = findDeveloperById(developerId);
        if (!project.getOwner().equals(developer)) {
            throw new NotFoundException("Could not found project for " + projectName + ".");
        }

        log.info("expired token: " + project.getClientToken());

        projectService.updateToken(project);

        log.info("new token: " + project.getClientToken());

        return success(
                projectService.findByName(projectName)
                        .map(ProjectDto::new)
                        .orElseThrow(() -> new NotFoundException("Could not found project for " + projectName + "."))
        );
    }

    private Developer findDeveloperById(Long developerId) throws NotFoundException {
        return developerService.findById(developerId)
                .orElseThrow(() -> new NotFoundException("Could not found developer for " + developerId + "."));
    }
}
