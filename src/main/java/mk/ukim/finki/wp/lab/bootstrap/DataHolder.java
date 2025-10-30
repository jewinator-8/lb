package mk.ukim.finki.wp.lab.bootstrap;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.lab.model.Book;
import mk.ukim.finki.wp.lab.model.BookReservation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {
    public static List<Book> books = new ArrayList<>();
    public static List<BookReservation> reservations = new ArrayList<>();
    @PostConstruct
    public void init() {
        if (!books.isEmpty()) return;
        books.add(new Book("Book 1","Programming",4.7));
        books.add(new Book("Book 2","Software",4.5));
        books.add(new Book("Book 3","Programming",4.6));
        books.add(new Book("Book 4","Programming",4.7));
        books.add(new Book("Book 5","Software",4.5));
        books.add(new Book("Book 6","Programming",4.6));
        books.add(new Book("Book 7","Programming",4.7));
        books.add(new Book("Book 8","Software",4.5));
        books.add(new Book("Book 9","Programming",4.6));
        books.add(new Book("Book 10","Programming",4.7));
    }
}
