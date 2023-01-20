package dev.oskarwiedeweg.todo_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration_time}")
    private Integer jwtExpirationTime;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    public boolean validateToken(String token, UserDetails userDetails) {
        String tokenSubject = getTokenSubject(token);
        return (tokenSubject.equals(userDetails.getUsername()) &&  !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime * 1000 * 60 * 24))
                .setIssuer(jwtIssuer)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String getTokenSubject(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims allClaims = getAllClaims(token);
        return claimsResolver.apply(allClaims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
