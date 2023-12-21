package fr.asl.projet.service;

import fr.asl.projet.model.Client;
import fr.asl.projet.model.ClientDTO;
import fr.asl.projet.model.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ClientService{
    @Autowired
    private ClientRepository clientRepository;

    public Client registerNewAccount(ClientDTO clientDto, String role) {

        Client client = new Client();
        client.setLogin(clientDto.getLogin());
        client.setPassword(clientDto.getPassword());
        client.setName(clientDto.getName());
        client.setAddress(clientDto.getAddress());
        client.setMail(clientDto.getMail());
        client.setRole(role);

        return clientRepository.save(client);
    }
}
