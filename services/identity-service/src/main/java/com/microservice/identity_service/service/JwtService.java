package com.microservice.identity_service.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // 定义常量SECRET，用于JWT签名，值为一个Base64编码的字符串。
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    /**
     * 验证JWT Token的有效性。
     *
     * @param token 需要验证的JWT Token
     */
    public void validateToken(final String token) {
        // 使用签名密钥解析JWT，并验证其合法性
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    /**
     * 生成JWT Token，包含给定的用户名。
     *
     * @param userName 需要包含在JWT中的用户名
     * @return 生成的JWT Token
     */
    public String generateToken(String userName, String email, Integer userId) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("userName", userName);
        claims.put("email", email);
        claims.put("userId", userId);
        return createToken(claims, userName, jwtExpirationInMs);
    }

    /**
     * 创建JWT Token，设置声明（claims）、主题（subject）、签发时间（issuedAt）和过期时间（expiration）。
     *
     * @param claims JWT中的自定义声明
     * @param userName JWT的主题，通常为用户名
     * @return 生成的JWT Token
     */
    private String createToken(Map<String, Object> claims, String userName, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)  // 设置自定义声明
                .setSubject(userName)  // 设置JWT的主题（通常是用户名）
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 设置JWT的签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // 设置JWT的过期时间，当前时间加30分钟
                .signWith(getSignKey(), SignatureAlgorithm.HS256)  // 使用HS256算法和签名密钥进行签名
                .compact();  // 生成并返回JWT字符串
    }

    /**
     * 获取用于签名JWT的密钥。使用Base64解码字符串，并返回一个适合HS256算法的密钥对象。
     *
     * @return 用于签名的密钥对象
     */
    private Key getSignKey() {
        // 使用Base64解码SECRET字符串，并生成适用于HMAC SHA256签名算法的密钥
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // 验证 Refresh Token 是否有效
    private final long jwtExpirationInMs = 120000;   // Access Token 1800000 过期时间 (例如30分钟) 120000  2 minutes
    private final long refreshTokenExpirationInMs = 604800000; // Refresh Token 过期时间 (7天)

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            System.out.println("Refresh token has expired.");
            return false;
        } catch (Exception ex) {
            System.out.println("Invalid refresh token.");
            return false;
        }
    }
    // 从Token中提取用户名
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // 生成 Refresh Token
    public String generateRefreshToken(String username, Integer userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userName", username);
        map.put("userId", userId);
        return createToken(map, username, refreshTokenExpirationInMs);
    }
    // 生成新的 Access Token
//    public String generateToken(String username) {
//        Map<String, Object> claims = new HashMap<>();
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs)) // 设置过期时间
//                .signWith(SignatureAlgorithm.HS256, getSignKey())
//                .compact();
//    }
}
