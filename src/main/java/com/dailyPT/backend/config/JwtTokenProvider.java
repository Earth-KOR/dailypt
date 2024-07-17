package com.dailyPT.backend.config;

import com.dailyPT.backend.common.enums.Role;
import com.dailyPT.backend.common.exception.BadRequestException;
import com.dailyPT.backend.user.domain.User;
import com.dailyPT.backend.user.repository.jpa.UserJpaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private static final String ROLES_KEY = "roles";

    private String secretKey;

    private Long tokenValidMilliseconds;
    private final UserJpaRepository userJpaRepository;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey, @Value("${spring.jwt.valid-duration}") Duration validDuration, UserJpaRepository userRepository) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.tokenValidMilliseconds = validDuration.toMillis();
        this.userJpaRepository = userRepository;
    }


    public String createToken(Long userId, Role role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put(ROLES_KEY, role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken() {
        Claims claims = Jwts.claims().setSubject(UUID.randomUUID().toString());
        Date now = new Date();
        Integer twoWeeks = 14;
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMilliseconds * twoWeeks))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUserId(String jwtToken) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody().getSubject();
    }

    public String getUserRoles(String jwtToken) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody().get(ROLES_KEY, String.class);
    }

    public boolean validateToken(String jwtToken) {
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean validateRoleOfUser(String jwtToken) {
        User userRole = userJpaRepository.findById(Long.parseLong(this.getUserId(jwtToken))).orElseThrow(() -> new BadRequestException("사용자가 존재하지 않습니다."));
        return this.getUserRoles(jwtToken).equals(userRole.getRole());
    }
}
