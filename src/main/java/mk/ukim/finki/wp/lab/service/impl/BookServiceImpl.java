package mk.ukim.finki.wp.lab.service.impl;

import mk.ukim.finki.wp.lab.model.Book;
import mk.ukim.finki.wp.lab.repository.BookRepository;
import mk.ukim.finki.wp.lab.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl  implements BookService {
    private final BookRepository repo;

    public BookServiceImpl(BookRepository repo) { this.repo = repo; }


    @Override
    public List<Book> listAll() {
        return repo.findAll();
    }

    @Override
    public List<Book> searchBooks(String text, Double rating) {
        return repo.searchBooks(text, rating);
    }
}
