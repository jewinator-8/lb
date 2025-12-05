package mk.ukim.finki.wp.lab.service;

import mk.ukim.finki.wp.lab.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> listAll();
    List<Book> searchBooks(String text, Double rating);
    Book  findById(Long id);
    void deleteById(Long id);
    Book create(String title, String genre, Double rating, Long authorId);
    Book update(Long id, String title, String genre, Double rating, Long authorId);
}
