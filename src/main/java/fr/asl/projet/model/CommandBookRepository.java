package fr.asl.projet.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommandBookRepository extends CrudRepository<CommandBook, Integer> {

    List<CommandBook> findAllByBook_Librarian(Librarian librarian);
}
