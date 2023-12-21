package fr.asl.projet.controller;

import fr.asl.projet.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LibrarianController {
    @Autowired
    private Facade facade;

    @PostMapping("/addLibrarian")
    public String addLibrarian(@RequestParam String login, @RequestParam String password, @RequestParam String name, @RequestParam String address, @RequestParam String mail) {
        facade.createLibrarian(login, password, name, address, mail);
        return "redirect:/";
    }

    @GetMapping("/addLibrarian")
    public String addLibrarian() {
        return "addLibrarian";
    }
}
