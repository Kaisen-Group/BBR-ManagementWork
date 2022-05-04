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
// @RequestMapping("/bbr-workmanagement")
public class SpringController {

    @ModelAttribute(value = "work")
    public Work getWork() {
        return new Work();
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkRepository workRepository;

    @GetMapping("/all")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/index")
    public String loadInfo(Model modelUser, Model modelWorks, Model modelWorkA, Model modelWorkB,
            HttpServletRequest req, Model modelUrl) {
        String baseUrl = "http://localhost:8080/bbr-workmanagement/login";
        modelUrl.addAttribute("baseUrl", baseUrl);
        modelUser.addAttribute("users", userRepository.findAll());
        modelWorks.addAttribute("works", workRepository.findAll());
        modelWorkA.addAttribute("worksA", workRepository.findByStatusTrue());
        modelWorkB.addAttribute("worksB", workRepository.findByStatusFalse());
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest req, Model modelUrl) {
        String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
                + req.getContextPath();
        modelUrl.addAttribute("baseUrl", baseUrl);
        return "login";
    }

    @GetMapping("/workDashboard")
    public String archivio() {

        return "workDashboard.html";
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

    @GetMapping("/work/active/dashboard/{id}")
    public String loadWorkActive(@PathVariable("id") int id, Model modelWorks) {
        workRepository.findById(id).ifPresent(o -> modelWorks.addAttribute("work", o));
        // System.out.println("id: " + id);
        return "workDashboard";
    }
    @GetMapping("/work/delete/{id}")
    public String deleteWork(@PathVariable("id") int id, Model modelWorks) {
        Work work = workRepository.findById(id).get();
        workRepository.delete(work);
        return "redirect:/index";
    }
    @GetMapping("/work/update-status/{id}")
    public String updateStatusWork(@PathVariable("id") int id, Model modelWorks) {
        Work work = workRepository.findById(id).get();
        work.setStatus(false);
        workRepository.save(work);
        return "redirect:/index";
    }
    @PostMapping("/work/update/{id}")
    public String updateWork(@PathVariable("id") int id, @Valid @ModelAttribute("work") Work work, Model model) {
        Work workT = workRepository.findById(id).get();
        String data = workT.getData().toString();
        Boolean status = workT.getStatus();
        model.addAttribute("work", work);
        workT = work;
        workT.setData(data);
        workT.setStatus(status);
        workRepository.save(workT);

        return "redirect:/work/active/dashboard/{id}";
    }
    @GetMapping("/work/archivied/dashboard/{id}")
    public String loadWorkArchivied(@PathVariable("id") int id, Model modelWorks) {
        workRepository.findById(id).ifPresent(o -> modelWorks.addAttribute("work", o));
        // System.out.println("id: " + id);
        return "archiviedWork";
    }
    @GetMapping("/work/archivied/update-status/{id}")
    public String updateStatusArchiviedWork(@PathVariable("id") int id, Model modelWorks) {
        Work work = workRepository.findById(id).get();
        work.setStatus(true);
        workRepository.save(work);
        return "redirect:/index";
    }
    @GetMapping("/work/archivied")
    public String loadInfoArchivied(Model modelWorkB) {
        modelWorkB.addAttribute("worksB", workRepository.findByStatusFalse());
        return "archivio";
    }
    
    }


