package fr.asl.projet.controller;

import fr.asl.projet.model.Client;
import fr.asl.projet.model.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/add")
    public String add(String login, String password) {
        clientRepository.save(new Client(login, password, "nom", "adresse", "mail"));
        return "Ajout d'un utilisateur";
    }

    @GetMapping("/list")
    public Iterable<Client> all() {
        return clientRepository.findAll();
    }

    @GetMapping("/find/{id}")
    public Client find(Integer id) {
        return clientRepository.findById(id).get();
    }
}
