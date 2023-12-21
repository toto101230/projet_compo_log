package fr.asl.projet.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    List<Category> findAllByName(String name);

    List<Category> findAllByNameEndsWith(String name);

    int findIdByName(String name);

    @Query("SELECT c.id FROM Category c WHERE c.name LIKE %:name%")
    List<Integer> findAllIDByNameEndsWith(@Param("name") String name);
}
