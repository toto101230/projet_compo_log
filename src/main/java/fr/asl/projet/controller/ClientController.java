package fr.asl.projet.controller;

import fr.asl.projet.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientController {
    @Autowired
    private Facade facade;

    @PostMapping("/addClient")
    public String addClient(Model model, @RequestParam String login, @RequestParam String password, @RequestParam String name, @RequestParam String address, @RequestParam String mail) {
        boolean success = facade.createClient(login, password, name, address, mail);
        if(success) {
            return "login";
        }else {
            model.addAttribute("error", "Nom d'utilisateur déjà utilisé");
            return "addClient";
        }
    }

    @GetMapping("/addClient")
    public String addClient() {
        return "addClient";
    }

    @GetMapping("/account")
    public String account(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("client", facade.findClientByLogin(userDetails.getUsername()));
        model.addAttribute("commands", facade.findCommandesByClientLogin(userDetails.getUsername()));
        return "account";
    }
}
