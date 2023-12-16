package fr.asl.projet.controller;

import fr.asl.projet.model.Client;
import fr.asl.projet.model.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class IndexController {
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/add")
    public String add(@RequestParam String login, @RequestParam String password) {
        clientRepository.save(new Client(login, password, "nom", "adresse", "mail"));
        return "Ajout d'un utilisateur";
    }

    @GetMapping("/list")
    public Iterable<Client> all() {
        return clientRepository.findAll();
    }

    @GetMapping("/find/{id}")
    public Client find(@PathVariable Integer id) {
        return clientRepository.findById(id).get();
    }
}
