package fr.asl.projet.model;

import org.springframework.data.repository.CrudRepository;

public interface LibrarianRegistrationRepository extends CrudRepository<LibrarianRegistration, Integer> {
    LibrarianRegistration findByLogin(String login);
}
