package mk.ukim.finki.wp.lab.service.impl;

import mk.ukim.finki.wp.lab.model.Author;
import mk.ukim.finki.wp.lab.model.Book;
import mk.ukim.finki.wp.lab.repository.AuthorRepository;
import mk.ukim.finki.wp.lab.repository.BookRepository;
import mk.ukim.finki.wp.lab.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Book> listAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> searchBooks(String text, Double rating) {
        if (text == null && rating == null) {
            return bookRepository.findAll();
        }

        return bookRepository.findAll().stream()
                .filter(b ->
                        (text == null || b.getTitle().toLowerCase().contains(text.toLowerCase())) &&
                                (rating == null || b.getAverageRating() >= rating)
                )
                .toList();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book create(String title, String genre, Double rating, Long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow();

        Book b = new Book();
        b.setTitle(title);
        b.setGenre(genre);
        b.setAverageRating(rating);
        b.setAuthor(author);

        return bookRepository.save(b);
    }

    @Override
    public Book update(Long id, String title, String genre, Double rating, Long authorId) {
        Book book = bookRepository.findById(id).orElseThrow();
        Author author = authorRepository.findById(authorId).orElseThrow();

        book.setTitle(title);
        book.setGenre(genre);
        book.setAverageRating(rating);
        book.setAuthor(author);

        return bookRepository.save(book);
    }
}
