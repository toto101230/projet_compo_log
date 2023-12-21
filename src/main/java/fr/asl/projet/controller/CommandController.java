package fr.asl.projet.controller;

import fr.asl.projet.model.*;
import fr.asl.projet.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class CommandController {
    @Autowired
    private Facade facade;

    @PostMapping("/cart")
    public String cart(Model model, @RequestParam List<Integer> ids) {
        Map<Book, Integer> books = facade.createBooks(ids);
        model.addAttribute("books", books);
        model.addAttribute("ids", ids);
        model.addAttribute("totalPrice", facade.calculateTotalPrice(books));
        model.addAttribute("totalShippingPrice", facade.calculateTotalShippingPrice(books));
        return "cart";
    }

    @PostMapping("/command")
    public String command(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String ids, @RequestParam int totalPrice, @RequestParam int totalShippingPrice) {
        facade.createCommand(userDetails.getUsername(), ids, totalPrice, totalShippingPrice);
        return "command";
    }
}
