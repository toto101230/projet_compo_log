package fr.asl.projet.controller;

import fr.asl.projet.model.Book;
import fr.asl.projet.model.BookRepository;
import fr.asl.projet.model.Category;
import fr.asl.projet.model.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/addBook")
    public String addBook(@RequestParam String title, @RequestParam String author, @RequestParam String editor,
                          @RequestParam Integer pageNb, @RequestParam String state, @RequestParam Integer price,
                          @RequestParam Integer shippingPrice, @RequestParam List<Integer> categories) {
        List<Category> categoriesList = (List<Category>) categoryRepository.findAllById(categories);
        System.out.println(categoriesList);
        bookRepository.save(new Book(title, author, editor, pageNb, state, price, shippingPrice, categoriesList));
        return "redirect:/";
    }

    @GetMapping("/addBook")
    public String addBook(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "addBook";
    }
}
