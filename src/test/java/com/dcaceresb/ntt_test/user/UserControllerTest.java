package com.dcaceresb.ntt_test.user;


import com.dcaceresb.ntt_test.common.dto.ApiResponseDto;
import com.dcaceresb.ntt_test.user.dto.CreateUserDto;
import com.dcaceresb.ntt_test.user.dto.PhoneDto;
import com.dcaceresb.ntt_test.user.dto.UpdateUserDto;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    private final String testId = "some_id";

    //CREATE ENDPOINT
    @Test
    @WithMockUser(value = "spring")
    public void createUser_created() throws Exception{
        CreateUserDto data = this.buildCreate();
        when(service.create(any()))
                .thenReturn(UserDto.builder().build());
        this.mockMvc.perform(this.postRequest(data))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(value = "spring")
    public void createUser_badRequestEmail() throws Exception{
        CreateUserDto data = CreateUserDto.builder()
                .email("not_match")
                .password("pass")
                .name("name")
                .build();

        this.mockMvc.perform(this.postRequest(data))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser(value = "spring")
    public void createUser_badRequestPassword() throws Exception{
        CreateUserDto data = CreateUserDto.builder()
                .email("email@email.com")
                .password("not_match")
                .name("name")
                .build();

        this.mockMvc.perform(this.postRequest(data))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(value = "spring")
    public void createUser_conflict() throws Exception{
        CreateUserDto data = this.buildCreate();
        when(service.create(any()))
                .thenThrow(DataIntegrityViolationException.class);

        this.mockMvc.perform(this.postRequest(data))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    // FIND ALL CASE
    @Test
    @WithMockUser(value = "spring")
    public void findAll_success() throws Exception{
        when(service.findAll())
                .thenReturn(List.of(UserDto.builder().build()));

        this.mockMvc.perform(
                    get("/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer token")
                        .with(csrf())
                ).andDo(print())
                .andExpect(status().isOk());
    }

    //FIND BY ID CASES
    @Test
    @WithMockUser(value = "spring")
    public void findById_success() throws Exception{
        when(service.findById(testId))
                .thenReturn(UserDto.builder().build());

        this.mockMvc.perform(getRequest())
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    public void findById_notFound() throws Exception{
        this.mockMvc.perform(getRequest())
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    //PATCH CASES
    @Test
    @WithMockUser(value = "spring")
    public void patch_success() throws Exception{
        UpdateUserDto data = this.buildUpdate();
        when(service.update(testId, data))
                .thenReturn(UserDto.builder().build());

        this.mockMvc.perform(patchRequest(data))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    public void patch_NotFound() throws Exception{
        UpdateUserDto data = this.buildUpdate();
        when(service.update(testId, data))
                .thenThrow(EntityNotFoundException.class);

        this.mockMvc.perform(patchRequest(data))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "spring")
    public void patch_Conflict() throws Exception{
        UpdateUserDto data = this.buildUpdate();
        when(service.update(testId, data))
                .thenThrow(DataIntegrityViolationException.class);

        this.mockMvc.perform(patchRequest(data))
                .andDo(print())
                .andExpect(status().isConflict());
    }


    // DELETE CASES
    @Test
    @WithMockUser(value = "spring")
    public void delete_success() throws Exception{
        UpdateUserDto data = this.buildUpdate();
        when(service.delete(testId))
                .thenReturn(ApiResponseDto.builder().build());

        this.mockMvc.perform(deleteRequest())
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    public void delete_notFound() throws Exception{
        UpdateUserDto data = this.buildUpdate();
        when(service.delete(testId))
                .thenThrow(EntityNotFoundException.class);

        this.mockMvc.perform(deleteRequest())
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // BUILDERS
    MockHttpServletRequestBuilder getRequest(){
        String endpoint = String.format("/user/%s", testId);
        return  get(endpoint)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token")
                .with(csrf());
    }

    MockHttpServletRequestBuilder patchRequest(UpdateUserDto data) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();

        String endpoint = String.format("/user/%s", testId);
        return  patch(endpoint)
                .header("Authorization", "Bearer token")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(data))
                .header("Authorization", "Bearer token")
                .with(csrf());
    }

    MockHttpServletRequestBuilder deleteRequest() {

        String endpoint = String.format("/user/%s", testId);
        return  delete(endpoint)
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token")
                .with(csrf());
    }

    MockHttpServletRequestBuilder postRequest(CreateUserDto data) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();

        return  post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsString(data))
                .with(csrf());
    }

    private PhoneDto buildPhone(){
        return PhoneDto.builder()
                .number("123")
                .cityCode("1")
                .countryCode("56")
                .build();
    }

    private CreateUserDto buildCreate(){
        return CreateUserDto.builder()
                .name("John")
                .email("test@mail.com")
                .password("Password01")
                .phones(List.of(this.buildPhone()))
                .build();
    }

    private UpdateUserDto buildUpdate(){
        return UpdateUserDto.builder()
                .email("test@mail.com")
                .phones(List.of(this.buildPhone()))
                .build();
    }
}
