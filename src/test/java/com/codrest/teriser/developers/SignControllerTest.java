/*
 * SignControllerTest.java
 * Author : 박찬형
 * Created Date : 2021-08-29
 */
package com.codrest.teriser.developers;

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


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SignControllerTest {
    private MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc){
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    @DisplayName("request login token success")
    void loginSuccessTest1() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/sign")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"hffg1590@gmail.com\"}"));

        result.andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(handler().handlerType(SignController.class))
                .andExpect(handler().methodName("login"));
    }

    @Test
    @Order(2)
    @DisplayName("login success")
    void loginSuccessTest2() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/sign")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester12@naver.com\", \"loginToken\":\"1234\"}"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(SignController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.name", is("tester")))
                .andExpect(jsonPath("$.response.email.address", is("tester12@naver.com")));
    }

    @Test
    @Order(3)
    @DisplayName("login failure(request is null)")
    void loginFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/sign")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(SignController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)));
    }

    @Test
    @Order(4)
    @DisplayName("login failure(not exist email)")
    void loginFailureTest2() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/sign")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"notexist@naver.com\", \"loginToken\":\"1234\"}"));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(SignController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(404)));
    }

    @Test
    @Order(5)
    @DisplayName("login failure(login token length > 132)")
    void loginFailureTest3() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/sign")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester12@naver.com\", \"loginToken\":\"" + "1234".repeat(34) + "\"}"));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(SignController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)));
    }

    @Test
    @Order(6)
    @DisplayName("login failure(wrong login token)")
    void loginFailureTest4() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/sign")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester12@naver.com\", \"loginToken\":\"wrong\"}"));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(SignController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)));
    }

    @Test
    @Order(7)
    @DisplayName("login failure(deactivated or not register email verified developer)")
    void loginFailureTest5() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/sign")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"register@gmail.com\", \"loginToken\":\"ABCDEFGH\"}"));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(SignController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)));
    }

    @Test
    @Order(8)
    @WithMockJwtAuthentication
    @DisplayName("logout success")
    void logoutSuccessTest() throws Exception{
        ResultActions result = mockMvc.perform(delete("/developers/sign")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(SignController.class))
                .andExpect(handler().methodName("logout"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @Order(9)
    @DisplayName("logout failure(not authenticated)")
    void logoutFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(delete("/developers/sign")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(SignController.class))
                .andExpect(handler().methodName("logout"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)));
    }
}
