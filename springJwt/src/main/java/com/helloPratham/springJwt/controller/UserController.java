package com.helloPratham.springJwt.controller;
//
//import com.helloPratham.springJwt.entity.User;
//import com.helloPratham.springJwt.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//import java.util.List;
//
//@RestController
//@RequestMapping("/home")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("users")
//    public List<User> getUSer(){
//        System.out.println("Getting Users");
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/current-user")
//    public String getCurrentUSer(Principal principal){
//        return principal.getName();
//    }
//}


import com.helloPratham.springJwt.entity.User;
import com.helloPratham.springJwt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody @Valid User user) {
        String token = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.\n"+"Authorization"+ "Bearer " + token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> user = userService.getAllUsers();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
