/*
 * Author : Kasania, Hyeokwoo Kwon
 * Filename : ApiController.java
 * Desc :
 */
package com.codrest.teriser.apis;

import com.codrest.teriser.errors.NotFoundException;
import com.codrest.teriser.projects.Project;
import com.codrest.teriser.projects.ProjectService;
import com.codrest.teriser.utils.ApiUtils;
import com.codrest.teriser.utils.HttpClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ProjectService projectService;

    /**
     * 일반 사용자가 Teriser에 등록된 서비스를 확인하기 위해 접속하는 endpoint.
     * 해당 Project에 등록된 command 목록 및 요청에 필요한 데이터 목록 출력.
     *
     * @param projectName
     * @return
     */
    @GetMapping(path = "/{projectName}")
    @ResponseStatus(HttpStatus.OK)
    public ApiUtils.ApiResult<String> getServiceInfo(@PathVariable String projectName) {

        Project project = projectService.findByName(projectName).orElseThrow(() -> new NotFoundException("Project " + projectName + " does not exist."));

        String requestURL = ConnectionServerURL.of(project.getConnectionServerName(), projectName);
        String responseFromCS = new HttpClient().get(requestURL, MediaType.APPLICATION_JSON);
        if (Objects.isNull(responseFromCS) || responseFromCS.length() < 1) {
            throw new NotFoundException("Failed to get info of the project.");
        }

        return ApiUtils.success(responseFromCS);
    }

    /**
     * 일반 사용자가 Teriser에 등록된 서비스를 이용하기 위해 접속하는 endpoint.
     *
     * @param projectName
     * @param methodName
     * @return
     */
    @GetMapping(path = "/{projectName}/{methodName}")
    public ResponseEntity<?> getCommandInfo(@PathVariable String projectName, @PathVariable String methodName) {

        Project project = projectService.findByName(projectName).orElseThrow(() -> new NotFoundException("Project " + projectName + " does not exist."));
        if (!project.getConnectionServerConnected()) {
            throw new NotFoundException("Project " + projectName + " does not exist.");
        }

        String requestURL = ConnectionServerURL.of(project.getConnectionServerName(), projectName, methodName);
        String responseFromCS = new HttpClient().get(requestURL, MediaType.APPLICATION_JSON);
        if (Objects.isNull(responseFromCS) || responseFromCS.length() < 1) {
            throw new NotFoundException("Method " + methodName + " not found.");
        }

        String responseBody = "{" + "\n" +
                "\"success\":true,\n" +
                "\"response\":" + responseFromCS + ",\n" +
                "\"error\":null" + "\n" +
                "}";
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseBody);
    }

    /**
     * 일반 사용자가 데이터의 입력과 함께 Teriser에 등록된 서비스를 이용하기 위해 접속하는 endpoint.
     *
     * @param projectName
     * @param methodName
     * @param parameters  post 요청 데이터
     * @return
     */
    @PostMapping(path = "/{projectName}/{methodName}")
    @ResponseStatus(HttpStatus.OK)
    public ApiUtils.ApiResult<String> runCommandWithParams(@PathVariable String projectName, @PathVariable String methodName, @RequestBody String parameters) {

        log.info("runCommandWithParam (projectName: " + projectName + ", methodName: " + methodName + ")");

        Project project = projectService.findByName(projectName).orElseThrow(() -> new NotFoundException("Project " + projectName + " does not exist."));
        if (!project.getConnectionServerConnected()) {
            throw new NotFoundException("Project " + projectName + " does not exist.");
        }

        // Connection Server에 Run Command (POST) 요청
        String requestURL = ConnectionServerURL.of(project.getConnectionServerName(), projectName, methodName);
        String responseFromCS = new HttpClient().post(requestURL, parameters, MediaType.APPLICATION_JSON);
        if (Objects.isNull(responseFromCS) || responseFromCS.length() < 1) {
            throw new NotFoundException("Method " + methodName + " not found.");
        }

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(responseFromCS);
        String result = jsonObject.get("data").getAsString();

        return ApiUtils.success(result);
    }
}
