package fr.asl.projet;

import fr.asl.projet.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    @Autowired
    private Facade facade;

    @Override
    public void run(String... args) {
        facade.createAdmin("admin", "admin", "", "", "", "ROLE_ADMIN");
        facade.createLibrarian("librarian", "librarian", "", "", "");
        facade.validateLibrarian("librarian");

        facade.createCategory("Fantastique");
        facade.createCategory("Science-fiction");
        facade.createCategory("Histoire");
        facade.createCategory("Philosophie");
        facade.createCategory("Science");
        facade.createCategory("Biographie");

        facade.addBook("librarian", "Le seigneur des anneaux", "J.R.R. Tolkien", "Editor1", 500, "Bon", 100, 10, facade.findAllCategoryIDByNameEndsWith("Fantastique"));
        facade.addBook("librarian", "Le seigneur des anneaux", "J.R.R. Tolkien", "Editor1", 20, "Bon", 100, 10, facade.findAllCategoryIDByNameEndsWith("Fantastique"));
        facade.addBook("librarian", "Sapiens", "Yuval Noah Harari", "Editor2", 120, "Bon", 100, 10, facade.findAllCategoryIDByNameEndsWith("Histoire"));
        facade.addBook("librarian", "Le meilleur des mondes", "Aldous Huxley", "Editor3", 800, "Bon", 100, 10, facade.findAllCategoryIDByNameEndsWith("Science-fiction"));
        facade.addBook("librarian", "Le mythe de Sisyphe", "Albert Camus", "Editor5", 200, "Bon", 100, 10, facade.findAllCategoryIDByNameEndsWith("phie"));
        facade.addBook("librarian", "La peste", "Albert Camus", "Editor5", 200, "Bon", 100, 10, facade.findAllCategoryIDByNameEndsWith("phie"));
    }
}
