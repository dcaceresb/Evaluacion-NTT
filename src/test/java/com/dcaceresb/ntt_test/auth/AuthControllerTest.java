package com.dcaceresb.ntt_test.auth;


import com.dcaceresb.ntt_test.auth.dto.LoginDto;
import com.dcaceresb.ntt_test.auth.dto.RegisterDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(
        classes = {AuthController.class}
)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService service;

    @Test
    @WithMockUser(value = "spring")
    public void login() throws Exception{
        LoginDto data = LoginDto.builder()
                .email("email@mail.com")
                .password("Password01")
                .build();
        this.mockMvc.perform(
                this.postRequest("login", data))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    public void register() throws Exception{
        RegisterDto data = RegisterDto.builder()
                .email("email@mail.com")
                .password("Password01")
                .build();
        this.mockMvc.perform(
                this.postRequest("register",data))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    MockHttpServletRequestBuilder postRequest(String path, Object body) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();
        return  post("/auth/"+path)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(body))
                .with(csrf());
    }
}
