package com.dcaceresb.ntt_test.common.jwt;

import com.dcaceresb.ntt_test.user.UserEntity;
import com.dcaceresb.ntt_test.user.UserRepository;
import com.dcaceresb.ntt_test.user.UserService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository repository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.duration}")
    private int duration;
    public String generate(UserEntity user){

        System.out.println(secret);
        System.out.println(duration);

        long current = System.currentTimeMillis();
        Date issued = new Date(current);
        Date expired = new Date(current+duration);
        Map<String, Object> claims = new HashMap<>();
        System.out.println(user.getId());
        claims.put("id", user.getId());
        claims.put("user", user);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issued)
                .setExpiration(expired)
                .signWith(
                        SignatureAlgorithm.HS512,
                        secret.getBytes()
                ).compact();
        return token;
    }

    public Claims validate(String token){
        Jwt<Header, Claims> decoded = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJwt(token);
        System.out.println(decoded.getBody());
        Optional<UserEntity> user = this.repository.findByToken(token);
        if(user.isEmpty()){
            throw new ExpiredJwtException(
                    decoded.getHeader(),
                    decoded.getBody(),
                    "Token is deprecated, use the latest or login again"
            );
        }
        return decoded.getBody();
    }
}
