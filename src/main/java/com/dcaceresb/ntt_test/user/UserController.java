package com.dcaceresb.ntt_test.user;


import com.dcaceresb.ntt_test.common.dto.ApiResponseDto;
import com.dcaceresb.ntt_test.user.dto.CreateUserDto;
import com.dcaceresb.ntt_test.user.dto.UpdateUserDto;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Value("${regex.password}")
    private String regexPsw;
    @Value("${regex.email}")
    private String regexEmail;

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED, reason = "Created")
    UserDto create(
            @Valid @RequestBody() CreateUserDto data
    ){
        this.validateRegex(data.getEmail(), data.getPassword());
        try{
            return userService.create(data);
        }catch (
        DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email duplicado");
        }
    }

    @GetMapping()
    List<UserDto> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    UserDto findById(@PathVariable String id){
        UserDto user = this.userService.findById(id);
        if(user==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    @PatchMapping("{id}")
    UserDto patch(
            @PathVariable String id,
            @Valid @RequestBody() UpdateUserDto data
    ){
        this.validateRegex(data.getEmail(), data.getPassword());
        try{
            return userService.update(id, data);
        }catch (EntityNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }catch (DataIntegrityViolationException ex){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email en uso");
        }
    }

    @DeleteMapping("{id}")
    ApiResponseDto delete(@PathVariable String id){
        try{
            return userService.delete(id);
        }catch (EntityNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    private void validateRegex(String email, String password){
        if(email != null && !email.matches(regexEmail)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email field must be a valid email");
        }
        if(password != null && !password.matches(regexPsw)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must complete all requirements");
        }
    }
}
