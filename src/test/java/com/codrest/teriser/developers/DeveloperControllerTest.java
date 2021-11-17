/*
 * DeveloperControllerTest.java
 * Author : 박찬형
 * Created Date : 2021-08-09
 */
package com.codrest.teriser.developers;

import com.codrest.teriser.configures.JwtTokenConfigure;
import com.codrest.teriser.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeveloperControllerTest {
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
    @DisplayName("get developer information success")
    void getDeveloperInformationSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(get("/developers")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(DeveloperController.class))
                .andExpect(handler().methodName("getDeveloperInformation"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.email.address", is("tester12@naver.com")))
                .andExpect(jsonPath("$.response.projects").isArray())
                .andExpect(jsonPath("$.response.projects[0].title", is("fish")));
    }

    @Test
    @Order(2)
    @DisplayName("get developer information failure(not authenticated)")
    void getDeveloperInformationFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(get("/developers")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(DeveloperController.class))
                .andExpect(handler().methodName("getDeveloperInformation"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)));
    }

    @Test
    @Order(3)
    @DisplayName("get developer information failure(not valid jwt token)")
    void getDeveloperInformationFailureTest2() throws Exception {
        ResultActions result = mockMvc.perform(get("/developers")
                .accept(MediaType.APPLICATION_JSON)
                .header(jwtTokenConfigure.getHeader(), "Bearer " + randomAlphanumeric(60)));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(DeveloperController.class))
                .andExpect(handler().methodName("getDeveloperInformation"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)));
    }
}
