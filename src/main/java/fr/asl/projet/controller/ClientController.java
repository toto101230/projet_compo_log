package fr.asl.projet.controller;

import fr.asl.projet.model.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import fr.asl.projet.model.Client;

@Controller
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/addClient")
    public String addClient(@RequestParam String login, @RequestParam String password, @RequestParam String name, @RequestParam String address, @RequestParam String mail) {
        clientRepository.save(new Client(login, password, name, address, mail));
        return "redirect:/";
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

    @GetMapping("/addClient")
    public String addClient() {
        return "addClient";
    }
}
