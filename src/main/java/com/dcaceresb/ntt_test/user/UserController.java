package com.dcaceresb.ntt_test.user;


import com.dcaceresb.ntt_test.common.dto.ApiResponseDto;
import com.dcaceresb.ntt_test.user.dto.CreateUserDto;
import com.dcaceresb.ntt_test.user.dto.UpdateUserDto;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    UserDto create(
            @Valid @RequestBody() CreateUserDto data
    ){
        return userService.create(data);
    }

    @GetMapping()
    List<UserDto> findAll(
            @RequestHeader("Authorization") String token
    ){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    UserDto findById(
            @RequestHeader("Authorization") String token,
            @PathVariable String id
    ){
        return userService.findById(id);
    }

    @PatchMapping("{id}")
    UserDto patch(
            @RequestHeader("Authorization") String token,
            @PathVariable String id,
            @Valid @RequestBody() UpdateUserDto data
    ){
        return userService.update(id, data);
    }

    @DeleteMapping("{id}")
    ApiResponseDto delete(
            @RequestHeader("Authorization") String token,
            @PathVariable String id
    ){
        return userService.delete(id);
    }
}
