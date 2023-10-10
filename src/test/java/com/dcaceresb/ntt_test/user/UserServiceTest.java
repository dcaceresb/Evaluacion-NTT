package com.dcaceresb.ntt_test.user;



import com.dcaceresb.ntt_test.common.dto.ApiResponseDto;
import com.dcaceresb.ntt_test.common.jwt.JwtService;
import com.dcaceresb.ntt_test.user.dto.CreateUserDto;
import com.dcaceresb.ntt_test.user.dto.PhoneDto;
import com.dcaceresb.ntt_test.user.dto.UpdateUserDto;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.client.ExpectedCount.twice;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private UserService service;

    private final String token = "Bearer tokencito";
    private final String id = "user_id";
    private UserEntity testUser;

    @BeforeEach
    public void setUp() {
        testUser = UserEntity.builder()
                .id(id)
                .email("test@mail.com")
                .token("tokencito")
                .phones(new ArrayList<>())
                .build();

        service = new UserService(
                repository,
                encoder,
                jwtService
        );
    }

    @Test
    public void findByTokenSuccess() {
        when(repository.findByToken("tokencito"))
                .thenReturn(Optional.of(testUser));

        UserEntity user = this.service.findAuthenticated(token);
        assertNotNull(user);
        assertEquals(testUser.getId(), user.getId());
    }

    @Test
    public void findByTokenInvalid() {
        when(repository.findByToken(anyString()))
                .thenReturn(Optional.empty());
        assertThrows(
                ResponseStatusException.class,
                () -> this.service.findAuthenticated("not_valid_token")
        );
        assertThrows(
                ResponseStatusException.class,
                () -> this.service.findAuthenticated("Bearer not_valid")
        );
    }

    @Test
    public void findByTokenNoAuth() {
        assertThrows(
                ResponseStatusException.class,
                () -> this.service.findAuthenticated(null)
        );
        assertThrows(
                ResponseStatusException.class,
                () -> this.service.findAuthenticated("")
        );
    }

    @Test
    public void updateSuccess(){
        when(repository.findById(id))
                .thenReturn(Optional.of(testUser));
        when(encoder.encode(anyString()))
                .thenReturn("hashed_pass");

        PhoneDto phone = PhoneDto.builder()
                .cityCode("1")
                .countryCode("59")
                .number("1234563")
                .build();

        UpdateUserDto data = UpdateUserDto.builder()
                .email("new_email@mail.com")
                .password("newPassword")
                .phones(List.of(phone))
                .build();
        this.service.update(id, data);
        then(repository).should().save(any());
    }

    @Test
    public void updateEmailInUse(){
        when(repository.findById(id))
                .thenReturn(Optional.of(testUser));
        when(repository.existByEmail("new_email@mail.com"))
                .thenReturn(true);

        UpdateUserDto data = UpdateUserDto.builder()
                .email("new_email@mail.com")
                .password("newPassword")
                .build();
        assertThrows(
                ResponseStatusException.class,
                () ->  this.service.update(id, data)
        );
    }

    @Test
    public void updateNoUser(){
        UpdateUserDto data = UpdateUserDto.builder()
                .build();
        assertThrows(
                ResponseStatusException.class,
                () ->  this.service.update(id, data)
        );
    }

    @Test
    public void updateNoData(){
        when(repository.findById(id))
                .thenReturn(Optional.of(testUser));

        UpdateUserDto data = UpdateUserDto.builder()
                .build();
        this.service.update(id, data);
        then(repository).should().save(any());
    }
    @Test
    public void updateSameEmail(){
        when(repository.findById(id))
                .thenReturn(Optional.of(testUser));

        UpdateUserDto data = UpdateUserDto.builder()
                .email(testUser.getEmail())
                .build();
        this.service.update(id, data);
        then(repository).should().save(any());
    }

    @Test
    public void updatePhonesEmptyArray(){
        when(repository.findById(id))
                .thenReturn(Optional.of(testUser));

        UpdateUserDto data = UpdateUserDto.builder()
                .phones(new ArrayList<>())
                .build();
        this.service.update(id, data);
        then(repository).should().save(any());
    }

    @Test
    public void updateSavedPhonesNull(){
        testUser.setPhones(null);
        when(repository.findById(id))
                .thenReturn(Optional.of(testUser));
        PhoneDto phone = PhoneDto.builder()
                .cityCode("1")
                .countryCode("59")
                .number("1234563")
                .build();
        UpdateUserDto data = UpdateUserDto.builder()
                .phones(List.of(phone))
                .build();
        this.service.update(id, data);
        then(repository).should().save(any());
    }


    // DELETE METHOD TESTS

    @Test
    public void deleteSuccess(){
        when(repository.findById(id))
                .thenReturn(Optional.of(testUser));
        ApiResponseDto res = this.service.delete(id);
        then(repository).should().delete(testUser);
        assertEquals(200,res.getStatus());
    }
    @Test
    public void deleteNoUser(){
        assertThrows(
                ResponseStatusException.class,
                () ->  this.service.delete(id)
        );
    }

    // FIND BY ID METHOD TESTS
    @Test
    public void findIdSuccess(){
        when(repository.findById(id))
                .thenReturn(Optional.of(testUser));
        UserDto userDto = this.service.findById(id);
        then(repository).should().findById(id);
        assertEquals(id, userDto.getId());
    }
    @Test
    public void findIdNoUser(){
        assertThrows(
                ResponseStatusException.class,
                () ->  this.service.findById(id)
        );
    }

    // FIND ALL METHOD TESTS
    @Test
    public void findAllTest() {
        when(repository.findAll())
                .thenReturn(List.of(testUser));
        List<UserDto> users = this.service.findAll();
        then(repository).should().findAll();
        assertEquals(1, users.size());
        assertFalse(users.isEmpty());
    }

    // SAVE METHOD TESTS
    @Test
    public void saveTest() {
        this.service.save(testUser);
        then(repository).should().save(testUser);
    }

    // CREATE METHOD TESTS
    @Test
    public void createSuccess() {
        CreateUserDto data = CreateUserDto.builder()
                .password("pass")
                .build();
        when(repository.save(any()))
                .thenReturn(testUser);
        when(repository.save(any()))
                .thenReturn(testUser);
        UserDto userDto = this.service.create(data);
        then(repository).should().save(testUser);
        assertNotNull(userDto);

    }

    @Test
    public void createDuplicated() {
        CreateUserDto data = CreateUserDto.builder()
                .email(testUser.getEmail())
                .password("pass")
                .build();
        when(repository.save(any()))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(
                ResponseStatusException.class,
                () ->  this.service.create(data)
        );
    }

    // FIND BY EMAIL METHOD TESTS
    @Test
    public void findByEmail_Success() {
        when(repository.findByEmail("email"))
                .thenReturn(Optional.of(testUser));

        UserEntity user = this.service.findByEmail("email");
        assertNotNull(user);
        assertEquals(testUser, user);
    }

    @Test
    public void findByEmail_Null() {
        UserEntity user = this.service.findByEmail("email");
        assertNull(user);
    }

}