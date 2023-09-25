package com.dcaceresb.ntt_test.auth;

import com.dcaceresb.ntt_test.auth.dto.LoginDto;
import com.dcaceresb.ntt_test.auth.dto.RegisterDto;
import com.dcaceresb.ntt_test.user.UserEntity;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public UserEntity login(
            @Valid @RequestBody LoginDto data
    ){
        return this.service.login(data);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserDto register(
            @RequestBody RegisterDto data
    ){
        return this.service.register(data);
    }
}
