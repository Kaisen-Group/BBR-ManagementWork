package com.kaisengroup.bbrmanagementwork.controller.controller;

import com.kaisengroup.bbrmanagementwork.controller.model.FilterClient;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.expr.NewArray;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    @ModelAttribute(value = "cliente")
    public FilterClient getcliente() {
       return new FilterClient();
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
    public String loadInfoArchivied(Model modelWorkB, Model modelClienti) {
        modelWorkB.addAttribute("worksB", workRepository.findByStatusFalse());
        modelClienti.addAttribute("clienti", workRepository.findDistinctCliente( ));
        return "archivio";
    }
    /*@GetMapping("/work/archivied/filter/{denominazione}")
    public String filterAchivied(@RequestParam(value = "denominazione") String den, Model modelWorks) {
       
        return "archivio";
    }*/
    @PostMapping("/work/archivied/filter")
    public String filterArchiviedWork(@Valid @ModelAttribute("cliente") FilterClient cliente, Model model,RedirectAttributes name,RedirectAttributes type,RedirectAttributes clienteC) {
       
        model.addAttribute("cliente", cliente);
        //return "redirect:/work/archivied/filter?search:" + cliente.getName() + "?type:" + cliente.getType() + "?cliente:" + cliente.getCliente();
       // name.addAttribute("name",cliente);
       // type.addAttribute("type", cliente.getType());
       // clienteC.addAttribute("cliente", cliente.getCliente());
        //return "redirect:/work/archivied/filter";
        return "redirect:/work/archivied/filter?search=" + cliente.getName() + "&type=" + cliente.getType() + "&cliente=" + cliente.getCliente();
    }
    @GetMapping("/work/archivied/filter")
    public String loadInfoArchiviedFilter(HttpServletRequest request,Model modelClienti, Model modelWorkB) {
        modelClienti.addAttribute("clienti", workRepository.findDistinctCliente( ));
        String search = request.getParameter("search");
        String type = request.getParameter("type");
        String cliente = request.getParameter("cliente");
        System.out.println(search + "+" + type + "+" + cliente);
        List <Work> workSearched = new ArrayList<Work>();
        List <Work> workArchivied = new ArrayList<Work>();
        workArchivied = workRepository.findByStatusFalse();
        if(!search.equals("")){
            for (Work work : workArchivied) {
                if(work.getName().toLowerCase().contains(search)){
                    workSearched.add(work);
                }
            }
        }
        modelWorkB.addAttribute("worksB", workSearched);//change
        return "archivio";
    }
    }


