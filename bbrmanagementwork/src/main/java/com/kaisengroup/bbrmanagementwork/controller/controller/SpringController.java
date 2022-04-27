package com.kaisengroup.bbrmanagementwork.controller.controller;

import com.kaisengroup.bbrmanagementwork.controller.model.User;
import com.kaisengroup.bbrmanagementwork.controller.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work/")
public class SpringController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("user")
    public List<User> getAllUser(){
        return  userRepository.findAll();
    }

    @GetMapping("user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Integer userId){
        User user=userRepository.findById(userId)
                .orElseThrow( () -> new IllegalArgumentException(""));
        return ResponseEntity.ok().body(user);

    }

    @PostMapping("user")
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }


}
