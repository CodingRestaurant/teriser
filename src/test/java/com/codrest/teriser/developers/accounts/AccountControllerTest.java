/*
 * AccountControllerTest.java
 * Author : 박찬형
 * Created Date : 2021-08-28
 */
package com.codrest.teriser.developers.accounts;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountControllerTest {
    private MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    @DisplayName("register request success")
    void registerSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"registeruser\",\"email\":\"registeruser@naver.com\"}"));

        result.andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("register"));
    }

    @Test
    @Order(2)
    @DisplayName("register request failure(null request)")
    void registerFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/account")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)));
    }

    @Test
    @Order(3)
    @DisplayName("register request failure(null email)")
    void registerFailureTest2() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"registeruser\"}"));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)));
    }

    @Test
    @Order(4)
    @DisplayName("register request failure(wrong email)")
    void registerFailureTest3() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"registeruser\",\"email\":\"register.com\"}"));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)));
    }

    @Test
    @Order(5)
    @DisplayName("register request failure(empty name)")
    void registerFailureTest4() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\",\"email\":\"register@gmail.com\"}"));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)));
    }

    @Test
    @Order(6)
    @DisplayName("register request failure(name length > 12)")
    void registerFailureTest5() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"sizeoversizeover\",\"email\":\"register@gmail.com\"}"));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)));
    }

    @Test
    @Order(7)
    @DisplayName("register request failure(already exist email)")
    void registerFailureTest6() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"confilct\",\"email\":\"tester12@naver.com\"}"));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(409)));
    }

    @Test
    @Order(8)
    @DisplayName("register request failure(already requested register)")
    void registerFailureTest7() throws Exception {
        ResultActions result = mockMvc.perform(post("/developers/account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"register2\",\"email\":\"register@gmail.com\"}"));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(409)));
    }

    @Test
    @Order(9)
    @DisplayName("deactivate request failure(not authenticated)")
    void deactivateFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(delete("/developers/account")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("deactivate"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)));
    }

    @Test
    @Order(10)
    @WithMockJwtAuthentication(id = 5L, password = "delete")
    @DisplayName("deactivate request failure(already requested deactivate)")
    void deactivateFailureTest2() throws Exception {
        ResultActions result = mockMvc.perform(delete("/developers/account")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("deactivate"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(409)));
    }

    @Test
    @Order(11)
    @WithMockJwtAuthentication(id = 2L, name = "ddd")
    @DisplayName("deactivate request success")
    void deactivateSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(delete("/developers/account")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("deactivate"));
    }

    @Test
    @Order(12)
    @DisplayName("verify account failure(empty token)")
    void verifyFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/developers/account/")
                        .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(405)));
    }

    @Test
    @Order(13)
    @DisplayName("verify account failure(wrong token)")
    void verifyFailureTest2() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/developers/account/zzzzzzzzzzzzzzzzzz")
                        .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("verify"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(404)));
    }

    @Test
    @Order(14)
    @DisplayName("verify account failure(expired token)")
    void verifyFailureTest3() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/developers/account/qwerty")
                        .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("verify"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(403)));
    }

    @Test
    @Order(15)
    @WithMockJwtAuthentication(id = 3L)
    @DisplayName("verify account success(register)")
    void verifySuccessTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/developers/account/asljfelijslei")
                        .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("verify"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));

        result = mockMvc.perform(get("/developers")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.email.address", is("register@gmail.com")));
    }

    @Test
    @Order(16)
    @DisplayName("verify account success(deactivate)")
    void deactivateTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/developers/account/asljfelijslei3")
                        .accept(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(AccountController.class))
                .andExpect(handler().methodName("verify"));
    }
}
