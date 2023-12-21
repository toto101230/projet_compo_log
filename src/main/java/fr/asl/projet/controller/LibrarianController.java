package fr.asl.projet.controller;

import fr.asl.projet.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LibrarianController {
    @Autowired
    private LibrarianRegistrationRepository librarianRegistrationRepository;

    @PostMapping("/addLibrarian")
    public String addLibrarian(@RequestParam String login, @RequestParam String password, @RequestParam String name, @RequestParam String address, @RequestParam String mail) {
        librarianRegistrationRepository.save(new LibrarianRegistration(login, password, name, address, mail, "ROLE_LIBRARIAN"));

        return "redirect:/";
    }

    @GetMapping("/addLibrarian")
    public String addLibrarian() {
        return "addLibrarian";
    }
}
