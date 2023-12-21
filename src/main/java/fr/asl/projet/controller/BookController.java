package fr.asl.projet.controller;

import fr.asl.projet.service.Facade;
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
    private Facade facade;

    @PostMapping("/addBook")
    public String addBook(@RequestParam String title, @RequestParam String author, @RequestParam String editor,
                          @RequestParam Integer pageNb, @RequestParam String state, @RequestParam Integer price,
                          @RequestParam Integer shippingPrice, @RequestParam List<Integer> categories) {
        facade.createBook(title, author, editor, pageNb, state, price, shippingPrice, categories);
        return "redirect:/";
    }

    @GetMapping("/addBook")
    public String addBook(Model model) {
        model.addAttribute("categories", facade.findAllCategories());
        return "addBook";
    }
}
