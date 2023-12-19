package fr.asl.projet.controller;

import fr.asl.projet.model.Client;
import fr.asl.projet.model.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/add")
    @ResponseBody
    public String add(@RequestParam String login, @RequestParam String password) {
        clientRepository.save(new Client(login, password, "nom", "adresse", "mail"));
        return "index";
    }

    @GetMapping("/list")
    @ResponseBody
    public Iterable<Client> all() {
        return clientRepository.findAll();
    }

    @GetMapping("/find/{id}")
    @ResponseBody
    public Client find(@PathVariable Integer id) {
        return clientRepository.findById(id).get();
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
