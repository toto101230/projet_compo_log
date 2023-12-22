package fr.asl.projet.service;

import fr.asl.projet.model.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
public class UserService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    public Client registerNewClient(UserDTO accountDto) {
        Client client = new Client();
        client.setLogin(accountDto.getLogin());
        client.setPassword(accountDto.getPassword());
        client.setName(accountDto.getName());
        client.setAddress(accountDto.getAddress());
        client.setMail(accountDto.getMail());
        client.setRole("ROLE_CLIENT");
        return clientRepository.save(client);
    }

    public Librarian registerNewLibrarian(UserDTO accountDto) {
        Librarian librarian = new Librarian();
        librarian.setLogin(accountDto.getLogin());
        librarian.setPassword(accountDto.getPassword());
        librarian.setName(accountDto.getName());
        librarian.setAddress(accountDto.getAddress());
        librarian.setMail(accountDto.getMail());
        librarian.setNote(-1.0);
        librarian.setBooks(new ArrayList<>());
        librarian.setCommands(new ArrayList<>());
        librarian.setRole("ROLE_LIBRARIAN");
        return librarianRepository.save(librarian);
    }
}
