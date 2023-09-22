package com.dcaceresb.ntt_test.auth;

import com.dcaceresb.ntt_test.auth.dto.LoginDto;
import com.dcaceresb.ntt_test.auth.dto.RegisterDto;
import com.dcaceresb.ntt_test.common.jwt.JwtService;
import com.dcaceresb.ntt_test.phone.PhoneEntity;
import com.dcaceresb.ntt_test.user.UserEntity;
import com.dcaceresb.ntt_test.user.UserRepository;
import com.dcaceresb.ntt_test.user.UserService;
import com.dcaceresb.ntt_test.user.dto.CreateUserDto;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import com.dcaceresb.ntt_test.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService{
    @Value("${regex.email}")
    private String regexEmail;
    @Value("${regex.password}")
    private String regexPassword;

    private final PasswordEncoder encoder;
    private final UserService userService;
    private final JwtService jwtService;

    public UserDto register(RegisterDto data){
        System.out.println("entra");
        System.out.println(regexEmail);
        System.out.println(regexPassword);
        if(!data.getEmail().matches(regexEmail)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email must be a valid value");
        }
        if(!data.getPassword().matches(regexPassword)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must have 2 digits, 1 Uppercase and many letters"
            );
        }
        String encoded = encoder.encode(data.getPassword());
        CreateUserDto createData = CreateUserDto.builder()
                .email(data.getEmail())
                .password(encoded)
                .phones(data.getPhones())
                .build();

        UserEntity user = this.userService.create(createData);
        String token = this.jwtService.generate(user);
        user.setToken(token);
        user.setLastLogin(new Date());
        this.userService.save(user);
        System.out.println(user);
        return UserMapper.INSTANCE.toDto(user);
    }

    public UserDto login(LoginDto data){
        UserEntity user = userService.findByEmail(data.getEmail());
        if(user==null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales invalidas");
        }
        boolean valid = encoder.matches(data.getPassword(), user.getPassword());
        if(!valid){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales invalidas");
        }

        String token = UUID.randomUUID().toString();

        user.setToken(token);
        user.setLastLogin(new Date());
        user.setUpdatedAt(new Date());
        userService.save(user);
        return UserMapper.INSTANCE.toDto(user);
    }
}
