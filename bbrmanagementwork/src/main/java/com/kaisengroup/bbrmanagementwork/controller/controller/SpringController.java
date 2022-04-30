package com.kaisengroup.bbrmanagementwork.controller.controller;

import com.kaisengroup.bbrmanagementwork.controller.model.User;
import com.kaisengroup.bbrmanagementwork.controller.model.Work;
import com.kaisengroup.bbrmanagementwork.controller.repository.UserRepository;
import com.kaisengroup.bbrmanagementwork.controller.repository.WorkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
//import org.springframework.data.crossstore.ChangeSetPersister;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
//@RequestMapping("/bbr-workmanagement")
public class SpringController {
   
    @ModelAttribute(value = "work")
    public Work getWork()
    {
        return new Work();
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkRepository workRepository;
        
    @GetMapping("/all")
    public List<User> findAllUsers() {
        return  userRepository.findAll();
    }
    @GetMapping("/index")
    public String loadInfo(Model modelUser, Model modelWorks, Model modelWorkA, Model modelWorkB, HttpServletRequest req, Model modelUrl) {
        String baseUrl = "http://localhost:8080/bbr-workmanagement/login";
        modelUrl.addAttribute("baseUrl", baseUrl);
        modelUser.addAttribute("users",userRepository.findAll());
        modelWorks.addAttribute("works",workRepository.findAll());
        modelWorkA.addAttribute("worksA",workRepository.findByStatusTrue());
        modelWorkB.addAttribute("worksB",workRepository.findByStatusFalse());
        return "index";
    }
    @GetMapping("/login")
    public String login( HttpServletRequest req, Model modelUrl) {
        String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
        modelUrl.addAttribute("baseUrl", baseUrl);
        return "login";
    }
    @GetMapping("/archivio")
    public String archivio( ) {
       
        return "archivio.html";
    }
 
    @PostMapping("/addWork")
    public String addWork(@Valid @ModelAttribute("work") Work work, Model model) {
        LocalDate localDate = LocalDate.now();
        
       
        String data = localDate.toString();
        work.setData(data);
        work.setStatus(true);
      
        model.addAttribute("work", work); 
        workRepository.save(work);
      
        return "redirect:/index";
    }
  
}


