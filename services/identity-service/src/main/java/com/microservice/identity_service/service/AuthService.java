package com.microservice.identity_service.service;


import com.microservice.identity_service.dto.TokenRefreshRequest;
import com.microservice.identity_service.dto.TokenRefreshResponse;
import com.microservice.identity_service.dto.UserResponse;
import com.microservice.identity_service.entity.UserCredential;
import com.microservice.identity_service.repository.UserCredentialRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserCredentialRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(UserCredential credential){
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        this.repository.save(credential);
        return "user added to the system";
    }

    public String generateToken(String userName, String email, Integer id){

        return this.jwtService.generateToken(userName, email, id);
    }
    public void volidateToken(String token){
        this.jwtService.validateToken(token);
    }

    public UserResponse findByEmail(String email) {
        Optional<UserCredential> userCredential = this.repository.findByEmail(email);

        if (userCredential.isPresent()) {
            UserCredential user = userCredential.get();
            return new UserResponse(user.getId(), user.getEmail(), user.getPassword(), user.getName());
        }

        return null;
    }

    public ResponseEntity<?> refreshToken(TokenRefreshRequest request) {
            String refreshToken = request.getRefreshToken();
        // 验证Refresh Token
        if (jwtService.validateRefreshToken(refreshToken)) {
            Claims claims = jwtService.getAllClaimsFromToken(refreshToken);
            String username = claims.get("username", String.class);
            Integer userId = claims.get("userId", Integer.class);

             Optional<UserCredential> user = this.repository.findById(userId);

             if (user != null){
                 // 生成新的Access Token
                 String newAccessToken = jwtService.generateToken(username, user.get().getEmail(),userId);
                 return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken));
             }else {
                 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
             }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is invalid or expired.");
        }
    }

    public String generateRefreshToken(String username, Integer userId) {
        return jwtService.generateRefreshToken(username, userId);
    }
}
