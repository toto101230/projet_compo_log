package fr.asl.projet.model;

import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {
    Book findByTitle(String title);
    Book findByAuthor(String author);



}
