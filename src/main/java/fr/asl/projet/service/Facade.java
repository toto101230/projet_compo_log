package fr.asl.projet.service;

import fr.asl.projet.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Facade {
    @Autowired
    private LibrarianRegistrationRepository librarianRegistrationRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CommandRepository commandRepository;
    @Autowired
    private CommandBookRepository commandBookRepository;

    public Iterable<LibrarianRegistration> findAllLibrarians() {
        return librarianRegistrationRepository.findAll();
    }

    public void validateLibrarian(String login) {
        LibrarianRegistration librarian = librarianRegistrationRepository.findByLogin(login);
        ClientDTO librarianDTO = new ClientDTO();
        librarianDTO.setLogin(login);
        librarianDTO.setPassword(librarian.getPassword());
        librarianDTO.setName(librarian.getName());
        librarianDTO.setAddress(librarian.getAddress());
        librarianDTO.setMail(librarian.getMail());
        clientService.registerNewAccount(librarianDTO, "ROLE_LIBRARIAN");
        librarianRegistrationRepository.delete(librarian);
    }

    public void deleteLibrarian(String login) {
        LibrarianRegistration librarian = librarianRegistrationRepository.findByLogin(login);
        librarianRegistrationRepository.delete(librarian);
    }

    public void createBook(String title, String author, String editor, Integer pageNb, String state, Integer price, Integer shippingPrice, List<Integer> categories) {
        List<Category> categoriesList = (List<Category>) categoryRepository.findAllById(categories);
        System.out.println(categoriesList);
        bookRepository.save(new Book(title, author, editor, pageNb, state, price, shippingPrice, categoriesList));
    }

    public Iterable<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public boolean createClient(String login, String password, String name, String address, String mail) {
        if (clientRepository.findByLogin(login) != null) {
            return false;
        }
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setLogin(login);
        clientDTO.setPassword(password);
        clientDTO.setName(name);
        clientDTO.setAddress(address);
        clientDTO.setMail(mail);
        clientService.registerNewAccount(clientDTO, "ROLE_CLIENT");
        return true;
    }

    public Map<Book, Integer> createBooks(List<Integer> ids) {
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

    public int calculateTotalShippingPrice(Map<Book, Integer> books) {
        int totalShippingPrice = 0;
        for (Map.Entry<Book, Integer> entry : books.entrySet()) {
            totalShippingPrice += entry.getKey().getShippingPrice() * entry.getValue();
        }
        return totalShippingPrice;
    }

    public int calculateTotalPrice(Map<Book, Integer> books) {
        int totalPrice = 0;
        for (Map.Entry<Book, Integer> entry : books.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }

    public void createCommand(String username, String ids, int totalPrice, int totalShippingPrice) {
        Client client = clientRepository.findByLogin(username);
        Command command = new Command(client, totalPrice, totalShippingPrice);
        commandRepository.save(command);
        for (Map.Entry<Book, Integer> entry : createBooks(toList(ids)).entrySet()) {
            CommandBook commandBook = new CommandBook(command, entry.getKey(), entry.getValue());
            command.addBook(commandBook);
            commandBookRepository.save(commandBook);
        }
        commandRepository.save(command);
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

    public Iterable<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public void createLibrarian(String login, String password, String name, String address, String mail) {
        librarianRegistrationRepository.save(new LibrarianRegistration(login, password, name, address, mail, "ROLE_LIBRARIAN"));
    }

    public Iterable<Book> findBooksByTitle(String search) {
        return bookRepository.findByTitleContainingIgnoreCase(search);
    }

    public Iterable<Book> findBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public Iterable<Book> findBooksByPageNbBetween(int pageNbMin, int pageNbMax) {
        return bookRepository.findByPageNbBetween(pageNbMin, pageNbMax);
    }

    public List<Book> findBooksByCategories(List<Category> categories) {
        // on garde tous les livres qui ont au moins une des catégories sélectionnées
        List<Book> booksByCategories = new ArrayList<>();
        for (Category category : categories) {
            List<Book> booksByCategory = bookRepository.findAllByCategories(category);
            for (Book book : booksByCategory) {
                if (!booksByCategories.contains(book)) {
                    booksByCategories.add(book);
                }
            }
        }
        return booksByCategories;
    }
}
