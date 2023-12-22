package fr.asl.projet.service;

import fr.asl.projet.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class Facade {
    @Autowired
    private LibrarianRegistrationRepository librarianRegistrationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LibrarianRepository librarianRepository;
    @Autowired
    private CommandRepository commandRepository;
    @Autowired
    private CommandBookRepository commandBookRepository;

    public Iterable<LibrarianRegistration> findAllLibrariansNoValidated() {
        return librarianRegistrationRepository.findAllByValidated(false);
    }

    public void validateLibrarian(String login) {
        LibrarianRegistration librarian = librarianRegistrationRepository.findByLogin(login);
        UserDTO librarianDTO = new UserDTO();
        librarianDTO.setLogin(login);
        librarianDTO.setPassword(librarian.getPassword());
        librarianDTO.setName(librarian.getName());
        librarianDTO.setAddress(librarian.getAddress());
        librarianDTO.setMail(librarian.getMail());
        userService.registerNewLibrarian(librarianDTO);
        librarian.setValidated(true);
        librarianRegistrationRepository.delete(librarian);
    }

    public void deleteLibrarian(String login) {
        LibrarianRegistration librarian = librarianRegistrationRepository.findByLogin(login);
        librarianRegistrationRepository.delete(librarian);
    }

    @Transactional
    public void addBook(String login, String title, String author, String editor, Integer pageNb, String state, Integer price, Integer shippingPrice, List<Integer> categories) {
        List<Category> categoriesList = (List<Category>) categoryRepository.findAllById(categories);
        Librarian librarian = librarianRepository.findLibrarianByLogin(login);
        bookRepository.save(new Book(title, author, editor, pageNb, state, price, shippingPrice, categoriesList, librarian));
    }

    public Iterable<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public boolean createClient(String login, String password, String name, String address, String mail) {
        if (clientRepository.findClientByLogin(login) != null || librarianRepository.findLibrarianByLogin(login) != null) {
            return false;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(login);
        userDTO.setPassword(password);
        userDTO.setName(name);
        userDTO.setAddress(address);
        userDTO.setMail(mail);
        userService.registerNewClient(userDTO);
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
        Client client = clientRepository.findClientByLogin(username);
        String strDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Command command = new Command(client, totalPrice, totalShippingPrice, strDate);
        commandRepository.save(command);
        for (Map.Entry<Book, Integer> entry : createBooks(toList(ids)).entrySet()) {
            CommandBook commandBook = new CommandBook(command, entry.getKey(), entry.getValue());
            command.addBook(commandBook);
            commandBookRepository.save(commandBook);
        }

        //for each book in the command, we add the librarian associated to the book to the list of librarians
        List<Librarian> librarians = new ArrayList<>();
        for (CommandBook commandBook : command.getBooks()) {
            if (!librarians.contains(commandBook.getBook().getLibrarian())) {
                librarians.add(commandBook.getBook().getLibrarian());
            }
        }
        command.setLibrarians(librarians);

        List<Boolean> validations = new ArrayList<>();
        for (Librarian librarian : librarians) {
            validations.add(false);
        }
        command.setValidations(validations);

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

    public boolean createLibrarian(String login, String password, String name, String address, String mail) {
        if (clientRepository.findClientByLogin(login) != null || librarianRepository.findLibrarianByLogin(login) != null) {
            return false;
        }
        librarianRegistrationRepository.save(new LibrarianRegistration(login, password, name, address, mail, "ROLE_LIBRARIAN"));
        return true;
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

    public Client findClientByLogin(String username) {
        Client client = clientRepository.findClientByLogin(username);
        Client clientDTO = new Client();
        clientDTO.setLogin(client.getLogin());
        clientDTO.setName(client.getName());
        clientDTO.setAddress(client.getAddress());
        clientDTO.setMail(client.getMail());
        return clientDTO;
    }

    public List<Command> findCommandesByClientLogin(String username) {
        return commandRepository.findAllByClient(clientRepository.findClientByLogin(username));
    }

    public void createCategory(String name) {
        categoryRepository.save(new Category(name));
    }

    public Iterable<Command> findAllCommandsNoValidated() {
        return commandRepository.findAllByStatus(false);
    }

    public void validateCommand(Integer idCommand, String login) {
        Command command = commandRepository.findById(idCommand).get();
        List<Boolean> validations = command.getValidations();
        validations.set(command.getLibrarians().indexOf(librarianRepository.findLibrarianByLogin(login)), true);
        command.setValidations(validations);
        if (!validations.contains(false)) {
            command.setStatus(true);
        }
        commandRepository.save(command);
    }

    public Iterable<Command> findAllCommands() {
        return commandRepository.findAll();
    }

    public Iterable<Command> findAllCommandsByLibrarian(String login) {
        return commandRepository.findAllByLibrarians(librarianRepository.findLibrarianByLogin(login));
    }
}
