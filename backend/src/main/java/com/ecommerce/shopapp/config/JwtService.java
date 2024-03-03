package com.ecommerce.shopapp.config;

import com.ecommerce.shopapp.exceptions.DataNotFoundException;
import com.ecommerce.shopapp.exceptions.UnauthorizedException;
import com.ecommerce.shopapp.models.TokenType;
import com.ecommerce.shopapp.models.User;
import com.ecommerce.shopapp.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.ecommerce.shopapp.models.TokenType.*;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepo;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.at-exp}")
    private long atExp;

    @Value("${jwt.rt-exp}")
    private long rtExp;

    public String extractEmail(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error when extract email: " + e);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token, String tokenType, UserDetails userDetails) {
        String username = extractEmail(token);
        return username != null
                && username.equals(userDetails.getUsername())
                && extractTokenType(token).equals(tokenType)
                && !isExpiredToken(token);
    }

    private boolean isExpiredToken(String token) {
        return extractExpiration(token).before(new Date());
    }

    private String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("token_type", String.class));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(ACCESS_TYPE, new HashMap<>(), userDetails, atExp);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(REFRESH_TYPE, new HashMap<>(), userDetails, rtExp);
    }

    public String generateResetToken(UserDetails userDetails) {
        return buildToken(RESET_TYPE, new HashMap<>(), userDetails, atExp);
    }

    private String buildToken(String tokenType,
                              Map<String, Object> extraClaims,
                              UserDetails userDetails,
                              Long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .claim("token_type", tokenType)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public User getTokenFromHeader(HttpServletRequest request, String tokenType) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String userEmail;

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException();
        }

        token = authHeader.substring(7);
        userEmail = extractEmail(token);

        if (userEmail == null) {
            throw new UnauthorizedException();
        }

        User user = userRepo.findByEmail(userEmail).
                orElseThrow(() -> new DataNotFoundException("Email not found"));

        if (!isValidToken(token, tokenType, user)) {
            throw new UnauthorizedException();
        }

        return user;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

