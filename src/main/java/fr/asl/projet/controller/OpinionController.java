package fr.asl.projet.controller;

import fr.asl.projet.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OpinionController {
    @Autowired
    private Facade facade;

    @PostMapping("/addOpinion")
    public String addOpinion(@AuthenticationPrincipal UserDetails userDetails, Double note, String comment, Integer commandBookId) {
        facade.addOpinion(userDetails.getUsername(), comment, note, commandBookId);
        return "redirect:/account";
    }

    @GetMapping("/opinion")
    public String opinion(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("opinions", facade.findAllOpinionOfLibrarian(userDetails.getUsername()));
        model.addAttribute("noteFinal", facade.findNoteLibrarian(userDetails.getUsername()));
        return "opinion";
    }
}
