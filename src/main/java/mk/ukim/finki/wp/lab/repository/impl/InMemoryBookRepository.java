package mk.ukim.finki.wp.lab.repository.impl;

import mk.ukim.finki.wp.lab.bootstrap.DataHolder;
import mk.ukim.finki.wp.lab.model.Author;
import mk.ukim.finki.wp.lab.model.Book;
import mk.ukim.finki.wp.lab.repository.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryBookRepository implements BookRepository {

    @Override
    public List<Book> findAll() {
        return DataHolder.books;
    }

    @Override
    public List<Book> searchBooks(String text, Double rating) {
        return DataHolder.books.stream().filter(c -> c.getTitle().equals(text) && c.getAverageRating() >= rating).toList();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return DataHolder.books.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    @Override
    public Book save(Book b) {
        DataHolder.books.add(b);
        return b;
    }

    @Override
    public Book update(Long id, String title, String genre, Double rating, Author a) {
        var book = findById(id).orElse(null);
        if (book == null) return null;
        book.setTitle(title);
        book.setGenre(genre);
        book.setAverageRating(rating);
        book.setAuthor(a);
        return book;
    }



    @Override
    public void deleteById(Long id) {
        DataHolder.books.removeIf(b -> b.getId().equals(id));
    }
}
