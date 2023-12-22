package fr.asl.projet.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    @Query("SELECT c.id FROM Category c WHERE c.name LIKE %:name%")
    List<Integer> findAllIDByNameEndsWith(@Param("name") String name);

    Category findByName(String name);
}
