package fr.asl.projet.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {
    Book findByTitle(String title);
    Book findByAuthor(String author);

    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByPageNbBetween(int pageNbMin, int pageNbMax);
    List<Book> findAllByCategories(Category category);
}
