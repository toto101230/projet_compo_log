package fr.asl.projet.controller;

import fr.asl.projet.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    @Autowired
    private Facade facade;

    @GetMapping("/validateLibrarian")
    public String validateLibrarian(Model model) {
        model.addAttribute("librarians", facade.findAllLibrarians());
        return "validateLibrarian";
    }

    @GetMapping("/recapCommand")
    public String recapCommand(Model model) {
        model.addAttribute("commands", facade.findAllCommandsOfPastMonth());
        return "recapCommand";
    }

    @PostMapping("/validateLibrarian")
    public String validateLibrarian(Model model, @RequestParam String login) {
        facade.validateLibrarian(login);
        model.addAttribute("librarians", facade.findAllLibrarians());
        return "validateLibrarian";
    }

    @PostMapping("/deleteLibrarian")
    public String deleteLibrarian(Model model, @RequestParam String login) {
        facade.deleteLibrarian(login);
        model.addAttribute("librarians", facade.findAllLibrarians());
        return "validateLibrarian";
    }
}
