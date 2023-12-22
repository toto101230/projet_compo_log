package fr.asl.projet.service;

import fr.asl.projet.model.Client;
import fr.asl.projet.model.ClientRepository;
import fr.asl.projet.model.Librarian;
import fr.asl.projet.model.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws RuntimeException {
        Client client = clientRepository.findClientByLogin(login);
        Librarian librarian = librarianRepository.findLibrarianByLogin(login);
        if (client != null) {
            return new User(client.getLogin(), passwordEncoder().encode(client.getPassword()), getAuthorities(client.getRole()));
        } else if (librarian != null) {
            return new User(librarian.getLogin(), passwordEncoder().encode(librarian.getPassword()), getAuthorities(librarian.getRole()));
        } else {
            throw new UsernameNotFoundException("User not found with login: " + login);
        }
    }


    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static List<GrantedAuthority> getAuthorities (String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
}
