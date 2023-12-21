package fr.asl.projet.controller;

import fr.asl.projet.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Autowired
    private Facade facade;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("books", facade.findAllBooks());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
