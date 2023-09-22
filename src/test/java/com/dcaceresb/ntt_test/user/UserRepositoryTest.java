package com.dcaceresb.ntt_test.user;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private final String email = "test@mail.com";
    private final String token = "tokencito";
    private final String tokenDeactivated = "tokencito2";

    @BeforeEach
    public void setup(){
        UserEntity testUser = UserEntity.builder()
                .email(email)
                .token(token)
                .isActive(true)
                .build();
        UserEntity userDeactivated = UserEntity.builder()
                .email("test_deactivated@mail.com")
                .token(tokenDeactivated)
                .isActive(false)
                .build();
        entityManager.persist(testUser);
        entityManager.persist(userDeactivated);
        entityManager.flush();
    }

    @Test
    public void findByEmail(){
        Optional<UserEntity> found = this.userRepository.findByEmail(email);
        Optional<UserEntity> notExist = this.userRepository.findByEmail("no existe");

        assertTrue(found.isPresent());
        assertTrue(notExist.isEmpty());
    }

    @Test
    public void findActiveByToken(){
        Optional<UserEntity> found = this.userRepository.findByToken(token);
        Optional<UserEntity> deactivated = this.userRepository.findByToken(tokenDeactivated);
        Optional<UserEntity> notExist = this.userRepository.findByToken("no existe");

        assertTrue(found.isPresent());
        assertTrue(deactivated.isEmpty());
        assertTrue(notExist.isEmpty());
    }

    @Test
    public void existByEmail(){
        assertTrue(
                this.userRepository.existByEmail(email)
        );
        assertFalse(
                this.userRepository.existByEmail("no existe")
        );
    }

    @Test
    public void constrainsEquals(){
        UserEntity entity = UserEntity.builder()
                .email(email)
                .isActive(true)
                .token("duplicated")
                .build();
        assertThrows(
                DataIntegrityViolationException.class,
                () -> this.userRepository.saveAndFlush(entity)
        );
    }



}
