package fr.asl.projet.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "command")
    private List<CommandBook> books;

    @ManyToMany
    private List<Librarian> librarians;

    @ElementCollection
    private List<Boolean> validations;

    private int totalPrice;
    private int totalShippingPrice;
    private String date;
    private Integer status;
    private String cancellationReason;

    public Command() {
    }

    public Command(Client client, int totalPrice, int totalShippingPrice, String date) {
        this.client = client;
        this.books = new ArrayList<>();
        this.totalPrice = totalPrice;
        this.totalShippingPrice = totalShippingPrice;
        this.date = date;
        this.status = 0;
        this.cancellationReason = "";
        this.librarians = new ArrayList<>();
        this.validations = new ArrayList<>();


    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<CommandBook> getBooks() {
        return books;
    }

    public void setBooks(List<CommandBook> books) {
        this.books = books;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalShippingPrice() {
        return totalShippingPrice;
    }

    public void setTotalShippingPrice(int totalShippingPrice) {
        this.totalShippingPrice = totalShippingPrice;
    }

    public void addBook(CommandBook book) {
        this.books.add(book);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Librarian> getLibrarians() {
        return librarians;
    }

    public void setLibrarians(List<Librarian> librarians) {
        this.librarians = librarians;
    }

    public List<Boolean> getValidations() {
        return validations;
    }

    public void setValidations(List<Boolean> validations) {
        this.validations = validations;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}
