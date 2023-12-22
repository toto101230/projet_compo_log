package fr.asl.projet.controller;

import fr.asl.projet.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LibrarianController {
    @Autowired
    private Facade facade;

    @PostMapping("/addLibrarian")
    public String addLibrarian(Model model, @RequestParam String login, @RequestParam String password, @RequestParam String name, @RequestParam String address, @RequestParam String mail) {
        boolean succes = facade.createLibrarian(login, password, name, address, mail);
        if (succes) {
            return "redirect:/";
        } else {
            model.addAttribute("error", "Nom d'utilisateur déjà utilisé");
            return "addLibrarian";
        }
    }

    @GetMapping("/validateCommand")
    public String validateCommand(Model model, @RequestParam String login ) {
        model.addAttribute("commands", facade.findAllCommandsByLibrarian(login));
        return "validateCommand";
    }

    @PostMapping("/validateCommand")
    public String validateCommand(Model model, @RequestParam int idCommand, @RequestParam String login) {
        facade.validateCommand(idCommand, login);
        model.addAttribute("commands", facade.findAllCommandsByLibrarian(login));
        return "validateCommand";
    }

    @GetMapping("/addLibrarian")
    public String addLibrarian() {
        return "addLibrarian";
    }
}
