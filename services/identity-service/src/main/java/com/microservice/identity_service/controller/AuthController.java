package com.microservice.identity_service.controller;

import com.microservice.identity_service.dto.AuthRequest;
import com.microservice.identity_service.dto.TokenRefreshRequest;
import com.microservice.identity_service.dto.TokenResponse;
import com.microservice.identity_service.dto.UserResponse;
import com.microservice.identity_service.entity.UserCredential;
import com.microservice.identity_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/identity")
public class AuthController {
    @Autowired
    private AuthService service;

//    @Autowired
//    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredential user){

        return this.service.saveUser(user);
    }

//    @PostMapping("/login")
//    public String getToken(@RequestBody AuthRequest user){
//        Authentication authentication = this.authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
//
//        if(authentication.isAuthenticated()){
//            return this.service.generateToken(user.getUserName());
//        }else{
//            throw new RuntimeException("invalid access");
//        }
//    }
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
    // 1. Try to find the user by email
    UserResponse user = service.findByEmail(authRequest.getEmail());

    // 2. If user doesn't exist, return response with error message
    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user is not exist!");
    }

    // 3. Validate password
    boolean isPasswordMatch = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
    if (!isPasswordMatch) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("password not match!");
    }

    String token = this.service.generateToken(user.getUsername(), user.getEmail(), user.getId());
    String refreshToken = this.service.generateRefreshToken(user.getUsername(), user.getId());

    return ResponseEntity.ok(new TokenResponse(token, refreshToken));
}
    // 处理刷新Token的请求
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwtToken(@RequestBody TokenRefreshRequest request) {

        return this.service.refreshToken(request);

    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token){
        this.service.volidateToken(token);
        return "Token is valid";
    }
}
