package fr.asl.projet.model;

import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Integer> {

    Client findClientByLogin(String login);

}
