package fr.asl.projet.controller;

import fr.asl.projet.model.Client;
import fr.asl.projet.model.ClientDTO;
import fr.asl.projet.model.ClientRepository;
import fr.asl.projet.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @PostMapping("/addClient")
    public String addClient(Model model, @RequestParam String login, @RequestParam String password, @RequestParam String name, @RequestParam String address, @RequestParam String mail) {
        if(clientRepository.findByLogin(login) != null) {
            model.addAttribute("error", "Nom d'utilisateur déjà utilisé");
            return "addClient";
        }

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setLogin(login);
        clientDTO.setPassword(password);
        clientDTO.setName(name);
        clientDTO.setAddress(address);
        clientDTO.setMail(mail);
        clientService.registerNewAccount(clientDTO, "ROLE_ADMIN");

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
