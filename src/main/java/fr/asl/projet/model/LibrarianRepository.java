package fr.asl.projet.model;

import org.springframework.data.repository.CrudRepository;

public interface LibrarianRepository extends CrudRepository<Librarian, Integer> {
        Librarian findLibrarianByLogin(String login);
}
