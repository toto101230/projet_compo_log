package fr.asl.projet.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommandRepository extends CrudRepository<Command, Integer> {
    List<Command> findAllByClient(Client client);

    List<Command> findAllByStatus(boolean status);
}
