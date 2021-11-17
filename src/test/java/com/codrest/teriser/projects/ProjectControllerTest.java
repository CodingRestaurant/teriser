/*
 * Author : Hyeokwoo Kwon
 * Filename : ProjectControllerTest.java
 * Desc :
 */
package com.codrest.teriser.projects;

import com.codrest.teriser.configures.JwtTokenConfigure;
import com.codrest.teriser.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectControllerTest {

    private MockMvc mockMvc;
    private JwtTokenConfigure jwtTokenConfigure;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    public void setJwtTokenConfigure(JwtTokenConfigure jwtTokenConfigure) {
        this.jwtTokenConfigure = jwtTokenConfigure;
    }

    @Test
    @Order(1)
    @WithMockJwtAuthentication
    @DisplayName("프로젝트 목록 조회 성공 테스트")
    void findAllSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(get("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ProjectController.class))
                .andExpect(handler().methodName("findAll"))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @Order(2)
    @WithMockJwtAuthentication
    @DisplayName("토큰 발급 성공 테스트")
    void tokenInfoSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(get("/projects/1/token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ProjectController.class))
                .andExpect(handler().methodName("tokenInfo"));
    }

    @Test
    @Order(3)
    @WithMockJwtAuthentication
    @DisplayName("프로젝트 추가 성공 테스트")
    void addProjectSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"my-title\"}"));

        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(ProjectController.class))
                .andExpect(handler().methodName("addProject"))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @Order(4)
    @DisplayName("프로젝트 추가 실패 테스트 (인증 토큰 없음)")
    void addProjectFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"my-title\"}"));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ProjectController.class))
                .andExpect(handler().methodName("addProject"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)));
    }

    @Test
    @Order(5)
    @WithMockJwtAuthentication
    @DisplayName("프로젝트 추가 실패 테스트 (유효하지 않은 토큰)")
    void addProjectFailureTest2() throws Exception {
        ResultActions result = mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"my-title\"}")
                .header(jwtTokenConfigure.getHeader(), "jetkick-" + randomAlphanumeric(60)));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ProjectController.class))
                .andExpect(handler().methodName("addProject"))
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @Order(6)
    @WithMockJwtAuthentication
    @DisplayName("프로젝트 정보 조회 성공 테스트")
    void findByIdSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(get("/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ProjectController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @Order(7)
    @WithMockJwtAuthentication
    @DisplayName("프로젝트 제거 성공 테스트")
    void removeProjectSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(delete("/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(handler().handlerType(ProjectController.class))
                .andExpect(handler().methodName("removeProject"));
    }
}
