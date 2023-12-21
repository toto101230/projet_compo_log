package fr.asl.projet;

import fr.asl.projet.model.*;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void run(String... args) throws Exception {
        clientRepository.save(new Client("admin", "admin", "", "", "", "ROLE_ADMIN"));

        categoryRepository.save(new Category("Fantastique"));
        categoryRepository.save(new Category("Science-fiction"));
        categoryRepository.save(new Category("Histoire"));
        categoryRepository.save(new Category("Philosophie"));
        categoryRepository.save(new Category("Science"));
        categoryRepository.save(new Category("Biographie"));


        bookRepository.save(new Book("Le seigneur des anneaux", "J.R.R. Tolkien", "Editor1", 500, "Bon", 100, 10, categoryRepository.findAllByName("Fantastique")));
        bookRepository.save(new Book("Le seigneur des anneaux", "J.R.R. Tolkien", "Editor1", 20, "Bon", 100, 10, categoryRepository.findAllByName("Fantastique")));
        bookRepository.save(new Book("Sapiens", "Yuval Noah Harari", "Editor2", 120, "Bon", 100, 10, categoryRepository.findAllByName("Histoire")));
        bookRepository.save(new Book("Le meilleur des mondes", "Aldous Huxley", "Editor3", 800, "Bon", 100, 10, categoryRepository.findAllByName("Science-fiction")));
        bookRepository.save(new Book("Le mythe de Sisyphe", "Albert Camus", "Editor5", 200, "Bon", 100, 10, categoryRepository.findAllByNameEndsWith("ie")));
        bookRepository.save(new Book("La peste", "Albert Camus", "Editor5", 200, "Bon", 100, 10, categoryRepository.findAllByName("Philosophie")));
    }
}
