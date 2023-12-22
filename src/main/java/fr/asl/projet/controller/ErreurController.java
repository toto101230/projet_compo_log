package fr.asl.projet.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErreurController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error", "Page non trouvée");
                model.addAttribute("code", statusCode);
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", "Erreur interne du serveur");
                model.addAttribute("code", statusCode);
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("error", "Accès refusé");
                model.addAttribute("code", statusCode);
            }
            // Autres codes d'erreur
            else {
                model.addAttribute("error", "Erreur inconnue");
                model.addAttribute("code", statusCode);
            }
        }

        return "errorView";
    }
}
