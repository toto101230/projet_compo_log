package fr.asl.projet.controller;

import fr.asl.projet.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {
    @Autowired
    private Facade facade;

    @GetMapping("/category")
    public String category(Model model) {
        model.addAttribute("categories", facade.findAllCategoriesWithNumber());
        return "category";
    }

    @PostMapping("/category")
    public String addCategory(Model model, @RequestParam String name) {
        boolean succes = facade.createCategory(name);
        if (succes) {
            model.addAttribute("success", "Catégorie ajoutée");
        } else {
            model.addAttribute("errorAdd", "Impossible d'ajouter cette catégorie car elle existe déjà");
        }
        model.addAttribute("categories", facade.findAllCategoriesWithNumber());
        return "category";
    }

    @GetMapping("/category/delete/{id}")
    public String deleteCategory(Model model, @PathVariable int id) {
        boolean succes = facade.deleteCategory(id);
        if(succes) {
            model.addAttribute("success", "Catégorie supprimée");
        } else {
            model.addAttribute("errorDelete", "Impossible de supprimer cette catégorie car elle est utilisée par un ou plusieurs livres");
        }
        model.addAttribute("categories", facade.findAllCategoriesWithNumber());
        return "category";
    }
}
