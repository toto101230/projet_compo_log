package fr.asl.projet.controller;

import fr.asl.projet.model.Book;
import fr.asl.projet.model.Category;
import fr.asl.projet.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private Facade facade;

    @PostMapping("/search")
    public String search(Model model, @RequestParam String search) {
        model.addAttribute("books", facade.findBooksByTitle(search));
        model.addAttribute("search", search);
        model.addAttribute("categoriesAll", facade.findAllCategories());
        return "search";
    }

    @PostMapping("/searchAdvanced")
    public String searchAdvanced(Model model, @RequestParam String search, String author, String pageNbMax, String pageNbMin, @RequestParam(required = false) List<Category> categories) {
        List<Book> books = (List<Book>) facade.findBooksByTitle(search);
        model.addAttribute("search", search);
        model.addAttribute("categoriesAll", facade.findAllCategories());
        // on enlève les livres si l'auteur ne correspond pas
        if (author != null && !author.isEmpty()) {
            books.retainAll((List<Book>) facade.findBooksByAuthor(author));
            model.addAttribute("author", author);
        }
        // on enlève les livres si le nombre de pages ne correspond pas
        if (pageNbMax != null && pageNbMin != null && !pageNbMax.isEmpty() && !pageNbMin.isEmpty()) {
            books.retainAll((List<Book>) facade.findBooksByPageNbBetween(Integer.parseInt(pageNbMin), Integer.parseInt(pageNbMax)));
            model.addAttribute("pageNbMax", pageNbMax);
            model.addAttribute("pageNbMin", pageNbMin);
        }
        // on enlève les livres si les catégories ne correspondent pas
        if (categories != null && !categories.isEmpty()) {
            books.retainAll(facade.findBooksByCategories(categories));
            model.addAttribute("categories", categories);
        }else {
            model.addAttribute("categories", new ArrayList<Category>());
        }
        model.addAttribute("books", books);
        return "search";
    }

}
