package mk.ukim.finki.wp.lab.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Book {

    private Long id;
    private String title;
    private String genre;
    private double averageRating;
    private Author author;

    // No-args constructor (needed by frameworks)
    public Book() {}

    // Constructor used in your DataHolder initialization
    public Book(String title, String genre, double rating, Author author) {
        this.id = (long) (Math.random() * 10000);
        this.title = title;
        this.genre = genre;
        this.averageRating = rating;
        this.author = author;
    }

    // getters/setters...
}