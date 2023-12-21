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

    private int totalPrice;

    private int totalShippingPrice;

    public Command() {
    }

    public Command(Client client, int totalPrice, int totalShippingPrice) {
        this.client = client;
        this.books = new ArrayList<>();
        this.totalPrice = totalPrice;
        this.totalShippingPrice = totalShippingPrice;
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
}
