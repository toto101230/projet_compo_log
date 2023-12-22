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
    @Autowired
    private OpinionRepository opinionRepository;

    public Iterable<LibrarianRegistration> findAllLibrarians() {
        return librarianRegistrationRepository.findAll();
    }

    // permet de créer un libraire quand un admin valide son inscription
    public void validateLibrarian(String login) {
        LibrarianRegistration librarian = librarianRegistrationRepository.findByLogin(login);
        UserDTO librarianDTO = new UserDTO();
        librarianDTO.setLogin(login);
        librarianDTO.setPassword(librarian.getPassword());
        librarianDTO.setName(librarian.getName());
        librarianDTO.setAddress(librarian.getAddress());
        librarianDTO.setMail(librarian.getMail());
        userService.registerNewLibrarian(librarianDTO);
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

    // permet de créer un client lors de son inscription
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


    // permet de créer une map pour le panier contenant les livres et leur quantité selon les ids des livres
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

    // permet de trier une map par ordre croissant des ids des objets
    private <T extends Identifiable> Map<T, Integer> trie(Map<T, Integer> originalMap) {
        Comparator<T> byIdComparator = Comparator.comparing(Identifiable::getId);
        Map<T, Integer> sortedMap = new TreeMap<>(byIdComparator);
        sortedMap.putAll(originalMap);
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

        // pour chaque livre de la commande, on ajoute le libraire associé au livre à la liste des libraires de la commande
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

    public boolean createCategory(String name) {
        if (categoryRepository.findByName(name) != null) {
            return false;
        }
        categoryRepository.save(new Category(name));
        return true;
    }

    public Map<Category, Integer> findAllCategoriesWithNumber() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        Map<Category, Integer> categoriesWithNumber = new HashMap<>();
        for (Category category : categories) {
            categoriesWithNumber.put(category, bookRepository.findAllByCategories(category).size());
        }
        categoriesWithNumber = trie(categoriesWithNumber);
        return categoriesWithNumber;
    }

    public boolean deleteCategory(int id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            return false;
        }
        if (!bookRepository.findAllByCategories(category.get()).isEmpty()) {
            return false;
        }
        categoryRepository.deleteById(id);
        return true;
    }

    public void validateCommand(Integer idCommand, String login) {
        Command command = commandRepository.findById(idCommand).get();
        List<Boolean> validations = command.getValidations();
        validations.set(command.getLibrarians().indexOf(librarianRepository.findLibrarianByLogin(login)), true);
        command.setValidations(validations);
        if (!validations.contains(false)) {
            command.setStatus(1);
        }
        commandRepository.save(command);
    }

    public void cancelCommand(Integer idCommand, String cancellationReason) {
        Command command = commandRepository.findById(idCommand).get();
        command.setStatus(-1);
        command.setCancellationReason(cancellationReason);
        commandRepository.save(command);
    }


    public Iterable<Command> findAllCommandsByLibrarian(String login) {
        return commandRepository.findAllByLibrarians(librarianRepository.findLibrarianByLogin(login));
    }

    public Iterable<Command> findAllCommandsOfPastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        return commandRepository.findAllByDateAfter(strDate);
    }

    public Command findCommandById(int id) {
        return commandRepository.findById(id).get();
    }

    public void addOpinion(String username, String comment, Double note, Integer commandBookId) {
        CommandBook commandBook = commandBookRepository.findById(commandBookId).get();
        Opinion opinion = new Opinion(username, comment, note, commandBook);
        commandBook.setOpinion(opinion);
        opinionRepository.save(opinion);
        commandBookRepository.save(commandBook);
        majNoteLibrarian(commandBook.getBook().getLibrarian());
    }

    private void majNoteLibrarian(Librarian librarian) {
        List<CommandBook> commandBooks = commandBookRepository.findAllByBook_Librarian(librarian);
        double totalNote = 0;
        int nbOpinions = 0;
        for (CommandBook commandBook : commandBooks) {
            if (commandBook.getOpinion() != null) {
                totalNote += commandBook.getOpinion().getNote();
                nbOpinions++;
            }
        }
        if (nbOpinions != 0) {
            double note = totalNote / nbOpinions;
            librarian.setNote(Math.round(note * 10.0) / 10.0);
        }
        librarianRepository.save(librarian);
    }

    public List<Opinion> findAllOpinionOfLibrarian(String username) {
        Librarian librarian = librarianRepository.findLibrarianByLogin(username);
        List<CommandBook> commandBooks = commandBookRepository.findAllByBook_Librarian(librarian);
        List<Opinion> opinions = new ArrayList<>();
        for (CommandBook commandBook : commandBooks) {
            if (commandBook.getOpinion() != null) {
                opinions.add(commandBook.getOpinion());
            }
        }
        return opinions;
    }

    public Double findNoteLibrarian(String username) {
        Librarian librarian = librarianRepository.findLibrarianByLogin(username);
        return librarian.getNote();
    }

    public void createAdmin(String login, String password, String name, String address, String mail, String role) {
        clientRepository.save(new Client(login, password, name, address, mail, role));
    }

    public List<Integer> findAllCategoryIDByNameEndsWith(String name) {
        return categoryRepository.findAllIDByNameEndsWith(name);
    }
}
