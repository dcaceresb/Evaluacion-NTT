package com.dcaceresb.ntt_test.auth;

import com.dcaceresb.ntt_test.auth.dto.LoginDto;
import com.dcaceresb.ntt_test.auth.dto.RegisterDto;
import com.dcaceresb.ntt_test.phone.dto.CreatePhoneDto;
import com.dcaceresb.ntt_test.phone.dto.PhoneDto;
import com.dcaceresb.ntt_test.user.UserEntity;
import com.dcaceresb.ntt_test.user.UserRepository;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private  PasswordEncoder encoder;
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
    }
    @Test
    public void loginSuccess(){
        UserEntity entity = UserEntity.builder()
                .id("user_id")
                .password("hashed_pass")
                .build();
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(entity));
        when(encoder.matches(loginDto.getPassword(), entity.getPassword()))
                .thenReturn(true);

        UserDto user = this.service.login(loginDto);
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
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(entity));

        assertThrows(
                ResponseStatusException.class,
                () -> this.service.login(loginDto)
        );
    }
    @Test
    public void registerSuccess(){
        UserDto user = this.service.register(registerDto);
        assertNotNull(user);
    }
    @Test
    public void registerSuccessNoPhones(){
        registerDto.setPhones(null);
        UserDto user = this.service.register(registerDto);
        assertNotNull(user);
    }
    @Test
    public void registerEmailInUse(){
        when(userRepository.existByEmail(registerDto.getEmail()))
                .thenReturn(true);

        assertThrows(
                ResponseStatusException.class,
                () -> this.service.register(registerDto)
        );
    }

}
