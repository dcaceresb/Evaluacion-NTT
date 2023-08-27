package com.dcaceresb.ntt_test.user;

import com.dcaceresb.ntt_test.phone.PhoneEntity;
import com.dcaceresb.ntt_test.phone.dto.PhoneDto;
import com.dcaceresb.ntt_test.phone.dto.PhoneMapper;
import com.dcaceresb.ntt_test.user.dto.UpdateUserDto;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import com.dcaceresb.ntt_test.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PhoneMapper phoneMapper;
    private final PasswordEncoder encoder;

    public UserDto update(String token, UpdateUserDto data){
        UserEntity user = this.findAuthenticated(token);
        String email = data.getEmail();
        String password = data.getPassword();
        List<PhoneDto> phones = data.getPhones();
        if(email != null && !email.equals(user.getEmail())){
            if(this.userRepository.existByEmail(email)){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email en uso");
            }
            user.setEmail(email);
        }
        if(password != null){
            user.setPassword(encoder.encode(password));
        }
        if(!phones.isEmpty()){
            List<PhoneEntity> phonesMapped = phones.stream().map(this.phoneMapper::toEntity).toList();
            List<PhoneEntity> phoneSaved = user.getPhones();
            for(PhoneEntity ent : phonesMapped){
                ent.setUser(user);
                phoneSaved.remove(ent);
                phoneSaved.add(ent);
            }
            user.setPhones(phoneSaved);
        }

        this.userRepository.save(user);
        return this.userMapper.toDto(user);

    }

    public UserEntity findAuthenticated(String token){
        if(token==null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be logged in");
        }
        token = token.split(" ")[1];
        Optional<UserEntity> opt = this.userRepository.findByToken(token);
        if(opt.isEmpty()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token not valid, must login again");
        }
        return opt.get();
    }
}
