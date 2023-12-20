package fr.asl.projet.controller;

import fr.asl.projet.model.Book;
import fr.asl.projet.model.BookRepository;
import fr.asl.projet.model.Client;
import fr.asl.projet.model.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/addClient")
    public String addClient(@RequestParam String login, @RequestParam String password, @RequestParam String name, @RequestParam String address, @RequestParam String mail) {
        clientRepository.save(new Client(login, password, name, address, mail));
        return "redirect:/";
    }

    @PostMapping("/addBook")
    public String addBook(@RequestParam String title, @RequestParam String author, @RequestParam String editor, @RequestParam Integer pageNb, @RequestParam String state, @RequestParam Integer price, @RequestParam Integer shippingPrice) {
        bookRepository.save(new Book(title, author, editor, pageNb, state, price, shippingPrice));
        return "redirect:/";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "index";
    }


    @GetMapping("/list")
    public Iterable<Client> all() {
        return clientRepository.findAll();
    }

    @GetMapping("/find/{id}")
    public Client find(@PathVariable Integer id) {
        return clientRepository.findById(id).get();
    }

    @GetMapping("/addClient")
    public String addClient() {
        return "addClient";
    }

    @GetMapping("/addBook")
    public String addBook() {
        return "addBook";
    }
}
