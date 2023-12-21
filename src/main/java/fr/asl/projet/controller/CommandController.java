package fr.asl.projet.controller;

import fr.asl.projet.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class CommandController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CommandRepository commandRepository;
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/cart")
    public String cart(Model model, @RequestParam List<Integer> ids) {
        Map<Book, Integer> books = createBooks(ids);
        model.addAttribute("books", books);
        model.addAttribute("ids", ids);
        model.addAttribute("totalPrice", calculateTotalPrice(books));
        model.addAttribute("totalShippingPrice", calculateTotalShippingPrice(books));
        return "cart";
    }

    private int calculateTotalShippingPrice(Map<Book, Integer> books) {
        int totalShippingPrice = 0;
        for (Map.Entry<Book, Integer> entry : books.entrySet()) {
            totalShippingPrice += entry.getKey().getShippingPrice() * entry.getValue();
        }
        return totalShippingPrice;
    }

    private int calculateTotalPrice(Map<Book, Integer> books) {
        int totalPrice = 0;
        for (Map.Entry<Book, Integer> entry : books.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }

    private Map<Book, Integer> createBooks(List<Integer> ids) {
        Map<Book, Integer> books = new HashMap<>();
        for (Integer id : ids) {
            Optional<Book> book = bookRepository.findById(id);
            if (book.isPresent()) {
                if (books.containsKey(book.get())) {
                    books.put(book.get(), books.get(book.get()) + 1);
                } else {
                    books.put(book.get(), 1);
                }
            }
        }
        books = trie(books);
        return books;
    }


    private Map<Book, Integer> trie(Map<Book, Integer> books) {
        Comparator<Book> byIdComparator = Comparator.comparing(Book::getId);
        Map<Book, Integer> sortedMap = new TreeMap<>(byIdComparator);
        sortedMap.putAll(books);
        return new LinkedHashMap<>(sortedMap);
    }

    @PostMapping("/command")
    public String command(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String ids, @RequestParam int totalPrice, @RequestParam int totalShippingPrice) {
        Client client = clientRepository.findByLogin(userDetails.getUsername());
        Command command = new Command(client, totalPrice, totalShippingPrice);
        for (Map.Entry<Book, Integer> entry : createBooks(toList(ids)).entrySet()) {
            CommandBook commandBook = new CommandBook(command, entry.getKey(), entry.getValue());
            command.addBook(commandBook);
        }
        commandRepository.save(command);
        return "command";
    }

    private List<Integer> toList(String ids) {
        List<Integer> idsList = new ArrayList<>();
        ids = ids.substring(1, ids.length() - 1);
        String[] idsArray = ids.split(", ");
        for (String id : idsArray) {
            idsList.add(Integer.parseInt(id));
        }
        return idsList;
    }
}
