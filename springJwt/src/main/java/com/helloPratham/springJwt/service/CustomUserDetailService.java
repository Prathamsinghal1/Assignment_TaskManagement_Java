package com.helloPratham.springJwt.service;


import com.helloPratham.springJwt.repository.UserRepository;
import com.helloPratham.springJwt.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(),  // Using phoneNumber from your User entity
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
