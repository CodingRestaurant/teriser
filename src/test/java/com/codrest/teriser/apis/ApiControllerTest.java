/*
 * Author : Hyeokwoo Kwon
 * Filename : ApiControllerTest.java
 * Desc :
 */
package com.codrest.teriser.apis;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiControllerTest {
    private MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    @DisplayName("서비스 확인 성공 테스트")
    void getServiceInfoTest1() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/info/fishfish")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        Map<String, List<String>> response = new HashMap<>();
        response.put("myMethod", List.of(new String[]{}));
        response.put("myMethod2", List.of(new String[]{"String"}));
        response.put("myMethod3", List.of(new String[]{"Integer", "Double"}));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiController.class))
                .andExpect(handler().methodName("getServiceInfo"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(response)));
    }

    @Test
    @Order(2)
    @DisplayName("서비스 확인 실패 테스트 (존재하지 않는 프로젝트)")
    void getServiceInfoTest2() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/info/my-project-5432978")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ApiController.class))
                .andExpect(handler().methodName("getServiceInfo"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error.status", is(404)));
    }

    @Test
    @Order(3)
    @DisplayName("인자 없는 함수 실행 성공 테스트")
    void runCommandTest1() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/run/fishfish/myMethod")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiController.class))
                .andExpect(handler().methodName("runCommand"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is("succeed")));
    }

    @Test
    @Order(4)
    @DisplayName("인자 없는 함수 실행 실패 테스트 (존재하지 않는 프로젝트)")
    void runCommandTest2() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/run/my-fishfish-528763/myMethod")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(handler().handlerType(ApiController.class))
                .andExpect(handler().methodName("runCommand"))
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @Order(5)
    @DisplayName("인자 없는 함수 실행 실패 테스트 (존재하지 않는 함수)")
    void runCommandTest3() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/run/fishfish/champongMethod")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(handler().handlerType(ApiController.class))
                .andExpect(handler().methodName("runCommand"))
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @Order(6)
    @DisplayName("인자 있는 함수 실행 성공 테스트 (post 요청)")
    void runCommandTest4() throws Exception {
        ResultActions result = mockMvc.perform(post("/api/run/fishfish/myMethod3")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"Integer\": \"1\", \"Double\": \"2.2\"}"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiController.class))
                .andExpect(handler().methodName("runCommandWithParams"))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @Order(7)
    @DisplayName("인자 있는 함수 실행 성공 테스트 (get 요청)")
    void runCommandTest5() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/run/fishfish/myMethod3/param?Integer=1&Double=2.2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiController.class))
                .andExpect(handler().methodName("runCommandWithParams"))
                .andExpect(jsonPath("$.success", is(true)));
    }
}
