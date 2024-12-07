package com.helloPratham.springJwt.controller;

import com.helloPratham.springJwt.entity.JwtRequest;
import com.helloPratham.springJwt.entity.JwtResponse;
import com.helloPratham.springJwt.entity.User;
import com.helloPratham.springJwt.security.JwtUtil;
import com.helloPratham.springJwt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getPhoneNumber(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhoneNumber());
        String token = this.helper.generateToken(userDetails.getUsername());  // Ensure proper token generation

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .phoneNumber(userDetails.getUsername())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private void doAuthenticate(String phoneNumber, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(phoneNumber, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password !!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> exceptionHandler(BadCredentialsException e) {
        logger.error("Authentication error: " + e.getMessage());
        return new ResponseEntity<>("Invalid Credentials!", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/create-user")
    public String createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
