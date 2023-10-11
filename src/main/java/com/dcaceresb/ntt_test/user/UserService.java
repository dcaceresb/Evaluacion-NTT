package com.dcaceresb.ntt_test.user;

import com.dcaceresb.ntt_test.common.dto.ApiResponseDto;
import com.dcaceresb.ntt_test.common.jwt.JwtService;
import com.dcaceresb.ntt_test.user.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public void save(UserEntity user){
        this.userRepository.save(user);
    }

    public UserEntity findByEmail(String email){
        Optional<UserEntity> opt = this.userRepository.findByEmail(email);
        return opt.orElse(null);
    }
    public UserDto create(CreateUserDto data){
        UserEntity user = UserMapper.INSTANCE.createToEntity(data);
        String encoded = this.encoder.encode(data.getPassword());
        user.setPassword(encoded);
        UserEntity created = this.userRepository.save(user);
        // now with the id set, generate the token
        String token = jwtService.generate(user);
        created.setToken(token);
        created.setLastLogin(new Date());
        this.userRepository.save(created);
        return UserMapper.INSTANCE.toDto(created);
    }

    public List<UserDto> findAll(){
        List<UserEntity> users = this.userRepository.findAll();
        return users.stream().map(UserMapper.INSTANCE::toDto).toList();
    }

    public UserDto findById(String id){
        Optional<UserEntity> user = this.userRepository.findById(id);
        return UserMapper.INSTANCE.toDto(user.orElse(null));
    }

    public ApiResponseDto delete(String id){
        Optional<UserEntity> userOpt = this.userRepository.findById(id);
        UserEntity user = userOpt.orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);
        return ApiResponseDto.builder()
                .message("User "+id+" deleted successfully")
                .status(200)
                .build();
    }


    public UserDto update(String id, UpdateUserDto data){
        UserEntity user = this.userRepository.findById(id).orElseThrow(
                EntityNotFoundException::new
        );
        String email = data.getEmail();
        String password = data.getPassword();
        List<PhoneDto> phones = data.getPhones();
        if(email != null && !email.equals(user.getEmail())){
            if(this.userRepository.existByEmail(email)){
                throw new DataIntegrityViolationException("Email en uso");
            }
            user.setEmail(email);
        }
        if(password != null){
            user.setPassword(encoder.encode(password));
        }
        if(phones != null && !phones.isEmpty()){
            user.setPhones(phones);
        }

        user.setUpdatedAt(new Date());
        this.userRepository.save(user);
        return UserMapper.INSTANCE.toDto(user);

    }

}
