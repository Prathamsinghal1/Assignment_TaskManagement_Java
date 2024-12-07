package com.helloPratham.springJwt.repository;
//
//import com.helloPratham.springJwt.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface UserRepository extends JpaRepository<User, String> {
//    public Optional<User> findByEmail(String email);
//}


import com.helloPratham.springJwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long Id);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
