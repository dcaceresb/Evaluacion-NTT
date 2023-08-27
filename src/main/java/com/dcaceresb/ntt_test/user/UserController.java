package com.dcaceresb.ntt_test.user;


import com.dcaceresb.ntt_test.user.dto.UpdateUserDto;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    UserEntity getProfile(
            @RequestHeader("Authorization") String token
    ){
        return userService.findAuthenticated(token);
    }

    @PatchMapping()
    UserDto patch(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody() UpdateUserDto data
    ){
        return userService.update(token, data);
    }
}
