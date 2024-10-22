//package com.microservice.identity_service.config;
//
//import com.microservice.identity_service.entity.UserCredential;
//import com.microservice.identity_service.repository.UserCredentialRepository;
//import org.hibernate.annotations.Comment;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class CustomUserDetailsService implements UserDetailsService {
//    @Autowired
//    private UserCredentialRepository repository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<UserCredential> credential =  this.repository.findByName(username);
//        return credential.map(CustomUserDetails::new).orElseThrow(()->new UsernameNotFoundException("user not found with the name "+ username));
//    }
//}
