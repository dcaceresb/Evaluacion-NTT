package com.dcaceresb.ntt_test.auth;

import com.dcaceresb.ntt_test.auth.dto.LoginDto;
import com.dcaceresb.ntt_test.auth.dto.RegisterDto;
import com.dcaceresb.ntt_test.common.jwt.JwtService;
import com.dcaceresb.ntt_test.phone.dto.CreatePhoneDto;
import com.dcaceresb.ntt_test.user.UserEntity;
import com.dcaceresb.ntt_test.user.UserService;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private AuthService service;

    private final String email = "email@mail.com";
    private LoginDto loginDto;
    private RegisterDto registerDto;

    @BeforeEach
    public void setup(){
        loginDto = LoginDto.builder()
                .email(email)
                .password("Password01")
                .build();
        CreatePhoneDto phone = CreatePhoneDto.builder()
                .countryCode("56")
                .cityCode("1")
                .number("123456")
                .build();
        registerDto = RegisterDto.builder()
                .email("email@mail.com")
                .password("Password01")
                .phones(List.of(phone))
                .build();
        ReflectionTestUtils.setField(jwtService, "secret", "5hUJxB9tjvibPsgtYTd9ypq5zYHKaAaD");
        ReflectionTestUtils.setField(jwtService, "duration", 600000);
        ReflectionTestUtils.setField(service, "regexEmail", "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        ReflectionTestUtils.setField(service, "regexPassword", "[A-Z)][a-z]*[0-9]{2}");
    }
    @Test
    public void loginSuccess(){
        UserEntity entity = UserEntity.builder()
                .id("user_id")
                .password("hashed_pass")
                .build();
        when(userService.findByEmail(email))
                .thenReturn(entity);
        when(encoder.matches(loginDto.getPassword(), entity.getPassword()))
                .thenReturn(true);

        UserEntity user = this.service.login(loginDto);
        assertNotNull(user);
        assertEquals(entity.getId(), user.getId());
    }
    @Test
    public void loginNoUser(){
        assertThrows(
                ResponseStatusException.class,
                () -> this.service.login(loginDto)
        );
    }
    @Test
    public void loginInvalidCredentials(){
        UserEntity entity = UserEntity.builder()
                .id("user_id")
                .build();
        when(userService.findByEmail(email))
                .thenReturn(entity);

        assertThrows(
                ResponseStatusException.class,
                () -> this.service.login(loginDto)
        );
    }
    @Test
    public void registerSuccess(){
        when(userService.create(any()))
                .thenReturn(this.buildUserEntity());
        UserDto user = this.service.register(registerDto);
        assertNotNull(user);
    }
    @Test
    public void registerSuccessNoPhones(){
        when(userService.create(any()))
                .thenReturn(this.buildUserEntity());
        registerDto.setPhones(null);
        UserDto user = this.service.register(registerDto);
        assertNotNull(user);
    }
    @Test
    public void registerEmailInUse(){
        when(userService.create(any()))
                .thenThrow(
                        new ResponseStatusException(HttpStatus.CONFLICT)
                );

        assertThrows(
                ResponseStatusException.class,
                () -> this.service.register(registerDto)
        );
    }

    private UserEntity buildUserEntity(){
        return UserEntity.builder()
                .id("some_id")
                .password("pass")
                .build();
    }

}
