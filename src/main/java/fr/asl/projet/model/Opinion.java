package fr.asl.projet.model;

import jakarta.persistence.*;

@Entity
public class Opinion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String login;
    private String comment;
    private Double note;
    @ManyToOne
    private CommandBook commandBook;

    public Opinion() {
    }

    public Opinion(String login, String comment, Double note, CommandBook commandBook) {
        this.login = login;
        this.comment = comment;
        this.note = note;
        this.commandBook = commandBook;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public CommandBook getBook() {
        return commandBook;
    }

    public void setBook(CommandBook commandBook) {
        this.commandBook = commandBook;
    }
}
