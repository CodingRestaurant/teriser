/*
 * Author : Hyeokwoo Kwon
 * Filename : ConnectionControllerTest.java
 * Desc :
 */

package com.codrest.teriser.connections;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConnectionControllerTest {

    private MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    @DisplayName("connection server 추가 성공 테스트")
    void addressSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(get("/connection/address?token=TestToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ConnectionController.class))
                .andExpect(handler().methodName("address"))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @Order(2)
    @DisplayName("project 추가 성공 테스트")
    void addProjectSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(post("/connection/project")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"Token\":\"TestToken\"}"));

        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(ConnectionController.class))
                .andExpect(handler().methodName("addProject"))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @Order(3)
    @DisplayName("project 제거 성공 테스트")
    void deleteProjectSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(delete("/connection/project?token=TestToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(handler().handlerType(ConnectionController.class))
                .andExpect(handler().methodName("deleteProject"))
                .andExpect(jsonPath("$.success", is(true)));
    }
}
