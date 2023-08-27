package com.dcaceresb.ntt_test.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    @Query("select u from UserEntity u where upper(u.email) = upper(?1) and u.isActive = true")
    Optional<UserEntity> findByEmail(String email);

    @Query("select u from UserEntity u where u.token = ?1 and u.isActive = true")
    Optional<UserEntity> findByToken(String token);

    @Query("select (count(u) > 0) from UserEntity u where u.email = ?1")
    boolean existByEmail(String email);





}
