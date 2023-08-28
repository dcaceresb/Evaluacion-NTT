package com.dcaceresb.ntt_test.auth;

import com.dcaceresb.ntt_test.auth.dto.LoginDto;
import com.dcaceresb.ntt_test.auth.dto.RegisterDto;
import com.dcaceresb.ntt_test.phone.PhoneEntity;
import com.dcaceresb.ntt_test.user.UserEntity;
import com.dcaceresb.ntt_test.user.UserRepository;
import com.dcaceresb.ntt_test.user.dto.UserDto;
import com.dcaceresb.ntt_test.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserDto register(RegisterDto data){
        if(userRepository.existByEmail(data.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email en uso");
        }
        UserEntity user = UserMapper.INSTANCE.registerToEntity(data);
        String encoded = encoder.encode(data.getPassword());
        String token = UUID.randomUUID().toString();

        user.setPassword(encoded);
        user.setToken(token);
        user.setLastLogin(Instant.now());
        List<PhoneEntity> phones =  user.getPhones();
        if(phones != null){
            for(PhoneEntity phone : phones){
                phone.setUser(user);
            }
        }

        userRepository.save(user);
        return UserMapper.INSTANCE.toDto(user);
    }

    public UserDto login(LoginDto data){
        Optional<UserEntity> user = userRepository.findByEmail(data.getEmail());
        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales invalidas");
        }
        UserEntity entity = user.get();
        boolean valid = encoder.matches(data.getPassword(), entity.getPassword());
        if(!valid){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales invalidas");
        }

        String token = UUID.randomUUID().toString();

        entity.setToken(token);
        entity.setLastLogin(Instant.now());
        entity.setUpdatedAt(Instant.now());
        userRepository.save(entity);
        return UserMapper.INSTANCE.toDto(entity);
    }
}
