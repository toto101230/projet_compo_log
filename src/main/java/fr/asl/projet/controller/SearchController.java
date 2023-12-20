package fr.asl.projet.controller;

import fr.asl.projet.model.Book;
import fr.asl.projet.model.BookRepository;
import fr.asl.projet.model.Category;
import fr.asl.projet.model.CategoryRepository;
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
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/search")
    public String search(Model model, @RequestParam String search) {
        model.addAttribute("books", bookRepository.findByTitleContainingIgnoreCase(search));
        model.addAttribute("search", search);
        model.addAttribute("categoriesAll", categoryRepository.findAll());
        return "search";
    }

    @PostMapping("/searchAdvanced")
    public String searchAdvanced(Model model, @RequestParam String search, String author, String pageNbMax, String pageNbMin, @RequestParam List<Category> categories) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(search);
        model.addAttribute("search", search);
        model.addAttribute("categoriesAll", categoryRepository.findAll());
        if (author != null && !author.isEmpty()) {
            books.retainAll(bookRepository.findByAuthorContainingIgnoreCase(author));
            model.addAttribute("author", author);
        }
        if (pageNbMax != null && pageNbMin != null && !pageNbMax.isEmpty() && !pageNbMin.isEmpty()) {
            int pageNbMaxInt = Integer.parseInt(pageNbMax);
            int pageNbMinInt = Integer.parseInt(pageNbMin);
            books.retainAll(bookRepository.findByPageNbBetween(pageNbMinInt, pageNbMaxInt));
            model.addAttribute("pageNbMax", pageNbMax);
            model.addAttribute("pageNbMin", pageNbMin);
        }
        if (categories != null && !categories.isEmpty()) {
            // on garde tous les livres qui ont au moins une des catégories sélectionnées
            List<Book> booksByCategories = new ArrayList<>();
            for (Category category : categories) {
                List<Book> booksByCategory = bookRepository.findAllByCategories(category);
                for (Book book : booksByCategory) {
                    if (!booksByCategories.contains(book)) {
                        booksByCategories.add(book);
                    }
                }
            }
            books.retainAll(booksByCategories);
            model.addAttribute("categories", categories);
        }
        model.addAttribute("books", books);
        return "search";
    }

}
