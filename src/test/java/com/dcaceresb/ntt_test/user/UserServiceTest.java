package com.dcaceresb.ntt_test.user;


import com.dcaceresb.ntt_test.phone.dto.PhoneDto;
import com.dcaceresb.ntt_test.phone.dto.PhoneMapper;
import com.dcaceresb.ntt_test.user.dto.UpdateUserDto;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import com.dcaceresb.ntt_test.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService service;

    private final String token = "Bearer tokencito";
    private UserEntity testUser;

    @BeforeEach
    public void setUp() {
        testUser = UserEntity.builder()
                .id("user_id")
                .email("test@mail.com")
                .token("tokencito")
                .phones(new ArrayList<>())
                .build();

        service = new UserService(
                repository,
                encoder
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
        when(encoder.encode(anyString()))
                .thenReturn("hashed_pass");
        when(repository.findByToken("tokencito"))
                .thenReturn(Optional.of(testUser));

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
        this.service.update(token, data);
        then(repository).should().save(any());
    }

    @Test
    public void updateEmailInUse(){
        when(repository.existByEmail("new_email@mail.com"))
                .thenReturn(true);
        when(repository.findByToken("tokencito"))
                .thenReturn(Optional.of(testUser));

        UpdateUserDto data = UpdateUserDto.builder()
                .email("new_email@mail.com")
                .password("newPassword")
                .build();
        assertThrows(
                ResponseStatusException.class,
                () ->  this.service.update(token, data)
        );
    }

    @Test
    public void updateNoData(){
        when(repository.findByToken("tokencito"))
                .thenReturn(Optional.of(testUser));

        UpdateUserDto data = UpdateUserDto.builder()
                .build();
        this.service.update(token, data);
        then(repository).should().save(any());
    }
    @Test
    public void updateSameEmail(){
        when(repository.findByToken("tokencito"))
                .thenReturn(Optional.of(testUser));

        UpdateUserDto data = UpdateUserDto.builder()
                .email(testUser.getEmail())
                .build();
        this.service.update(token, data);
        then(repository).should().save(any());
    }

    @Test
    public void updatePhonesEmptyArray(){
        when(repository.findByToken("tokencito"))
                .thenReturn(Optional.of(testUser));

        UpdateUserDto data = UpdateUserDto.builder()
                .phones(new ArrayList<>())
                .build();
        this.service.update(token, data);
        then(repository).should().save(any());
    }

    @Test
    public void updateSavedPhonesNull(){
        testUser.setPhones(null);
        when(repository.findByToken("tokencito"))
                .thenReturn(Optional.of(testUser));
        PhoneDto phone = PhoneDto.builder()
                .cityCode("1")
                .countryCode("59")
                .number("1234563")
                .build();
        UpdateUserDto data = UpdateUserDto.builder()
                .phones(List.of(phone))
                .build();
        this.service.update(token, data);
        then(repository).should().save(any());
    }
}