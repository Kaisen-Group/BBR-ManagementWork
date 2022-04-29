package com.kaisengroup.bbrmanagementwork.controller.controller;

import com.kaisengroup.bbrmanagementwork.controller.model.User;
import com.kaisengroup.bbrmanagementwork.controller.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.data.crossstore.ChangeSetPersister;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api/user")
public class SpringController {
 
    @Autowired
    private UserRepository userRepository;
        
    @GetMapping("/all")
    public List<User> findAllUsers() {
        return  userRepository.findAll();
    }
    @GetMapping("/index")
    public String showUserList(Model model) {
        model.addAttribute("users",userRepository.findAll());
        return "index";
    }
}

