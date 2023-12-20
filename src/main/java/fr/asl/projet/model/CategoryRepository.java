package fr.asl.projet.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    List<Category> findAllByName(String name);

    List<Category> findAllByNameEndsWith(String name);
}
