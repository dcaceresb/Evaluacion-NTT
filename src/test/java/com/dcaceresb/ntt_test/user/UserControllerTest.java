package com.dcaceresb.ntt_test.user;


import com.dcaceresb.ntt_test.user.dto.PhoneDto;
import com.dcaceresb.ntt_test.user.dto.UpdateUserDto;
import com.dcaceresb.ntt_test.user.dto.UserDto;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(
        classes = {UserController.class}
)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    @WithMockUser(value = "spring")
    public void getUserInfo() throws Exception{
        when(service.findAuthenticated(any()))
                .thenReturn(UserEntity.builder().build());
        this.mockMvc.perform(this.getRequest())
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    public void patchUser() throws Exception{
        when(service.update(any(), any()))
                .thenReturn(UserDto.builder().build());
        this.mockMvc.perform(this.patchRequest())
                .andDo(print())
                .andExpect(status().isOk());
    }

    MockHttpServletRequestBuilder getRequest(){
        return  get("/user")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token")
                .with(csrf());
    }

    MockHttpServletRequestBuilder patchRequest() throws JsonProcessingException {
        PhoneDto phone = PhoneDto.builder()
                .number("123")
                .cityCode("1")
                .countryCode("56")
                .build();
        UpdateUserDto data = UpdateUserDto.builder()
                .email("test@mail.com")
                .password("Password01")
                .phones(List.of(phone))
                .build();
        ObjectMapper obj = new ObjectMapper();

        return  patch("/user/id")
                .header("Authorization", "Bearer token")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(data))
                .header("Authorization", "Bearer token")
                .with(csrf());
    }

}
