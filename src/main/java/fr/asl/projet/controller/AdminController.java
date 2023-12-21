package fr.asl.projet.controller;

import fr.asl.projet.model.ClientDTO;
import fr.asl.projet.model.LibrarianRegistration;
import fr.asl.projet.model.LibrarianRegistrationRepository;
import fr.asl.projet.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @Autowired
    private LibrarianRegistrationRepository librarianRegistrationRepository;

    @Autowired
    private ClientService clientService;

    @GetMapping("/validateLibrarian")
    public String validateLibrarian(Model model) {
        model.addAttribute("librarians", librarianRegistrationRepository.findAll());
        return "validateLibrarian";
    }

    @PostMapping("/validateLibrarian")
    public String validateLibrarian(Model model, @RequestParam String login) {
        LibrarianRegistration librarian = librarianRegistrationRepository.findByLogin(login);
        ClientDTO librarianDTO = new ClientDTO();
        librarianDTO.setLogin(login);
        librarianDTO.setPassword(librarian.getPassword());
        librarianDTO.setName(librarian.getName());
        librarianDTO.setAddress(librarian.getAddress());
        librarianDTO.setMail(librarian.getMail());
        clientService.registerNewAccount(librarianDTO, "ROLE_LIBRARIAN");
        librarianRegistrationRepository.delete(librarian);
        model.addAttribute("librarians", librarianRegistrationRepository.findAll());
        return "validateLibrarian";
    }

    @PostMapping("/deleteLibrarian")
    public String deleteLibrarian(Model model, @RequestParam String login) {
        LibrarianRegistration librarian = librarianRegistrationRepository.findByLogin(login);
        librarianRegistrationRepository.delete(librarian);
        model.addAttribute("librarians", librarianRegistrationRepository.findAll());
        return "validateLibrarian";
    }
}
