/*
 * Author : Hyeokwoo Kwon
 * Filename : ConnectionController.java
 * Desc :
 */

package com.codrest.teriser.connections;

import com.codrest.teriser.projects.ProjectService;
import com.codrest.teriser.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/connection")
public class ConnectionController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ConnectionServerInfo connectionServerInfo = ConnectionServerInfo.instance;
    private final ProjectService projectService;

    /**
     * Connection Server가 아닌, Client로부터 들어오는 요청입니다.
     * 응답으로 가장 원활한 Connection Server의 이름을 리턴합니다.
     *
     * @param token
     * @return
     */
    @GetMapping("/address")
    public ApiUtils.ApiResult<String> connectionServerAddress(
            @RequestParam String token
    ) {
        log.info("connectionServerAddress (project token: " + token + ")");

        projectService.findByToken(token);
        return ApiUtils.success(
                connectionServerInfo.optimalConnectionServerName()
        );
    }

    /**
     * connection server가 보내 준 client의 토큰이 valid한지 확인합니다.
     *
     * @param requestBody
     * @return
     */
    @PostMapping("/project")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiUtils.ApiResult<String> setProjectRunning(
            @RequestBody Map<String, String> requestBody
    ) {

        String clientToken = requestBody.get("Token");
        String serverToken = requestBody.get("ServerToken");
        String projectName = projectService.setConnectionServer(clientToken, serverToken);
        log.info("setProjectRunning (project name: " + projectName + ", server token: " + serverToken + ")");
        return ApiUtils.success(projectName);
    }

    /**
     * connection server가 보내 준 종료된 client의 token이 valid한지 검사한 뒤,
     * connection server와의 연결을 해제합니다.
     * project의 실행이 중지되면 해당 프로젝트가 실행 중이던 connection server에게서 연결을 해제합니다.
     *
     * @param projectName
     * @return
     */
    @DeleteMapping("/project")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiUtils.ApiResult<String> setProjectIdle(
            @RequestParam String projectName,
            @RequestParam String token
    ) {
        log.info("setProjectIdle (project name: " + projectName + ", connection server token: " + token + ")");

        return ApiUtils.success(projectService.removeConnectionServer(projectName, token));
    }
}
