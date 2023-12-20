package fr.asl.projet.controller;

import fr.asl.projet.model.Book;
import fr.asl.projet.model.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/addBook")
    public String addBook(@RequestParam String title, @RequestParam String author, @RequestParam String editor, @RequestParam Integer pageNb, @RequestParam String state, @RequestParam Integer price, @RequestParam Integer shippingPrice) {
        bookRepository.save(new Book(title, author, editor, pageNb, state, price, shippingPrice));
        return "redirect:/";
    }

    @GetMapping("/addBook")
    public String addBook() {
        return "addBook";
    }

}
