package fr.asl.projet.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Book implements Identifiable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;
    private String author;
    private String editor;
    private Integer pageNb;
    private String state;
    private Integer price;
    private Integer shippingPrice;
    @ManyToMany
    private List<Category> categories;
    @ManyToOne
    private Librarian librarian;

    public Book() {
    }

    public Book(String title, String author, String editor, Integer pageNb, String state, Integer price, Integer shippingPrice, List<Category> categories, Librarian librarian) {
        this.title = title;
        this.author = author;
        this.editor = editor;
        this.pageNb = pageNb;
        this.state = state;
        this.price = price;
        this.shippingPrice = shippingPrice;
        this.categories = categories;
        this.librarian = librarian;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Integer getPageNb() {
        return pageNb;
    }

    public void setPageNb(Integer pageNb) {
        this.pageNb = pageNb;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(Integer shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Librarian getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }
}
