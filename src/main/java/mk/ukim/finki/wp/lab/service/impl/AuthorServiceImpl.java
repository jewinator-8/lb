package mk.ukim.finki.wp.lab.service.impl;

import mk.ukim.finki.wp.lab.model.Author;
import mk.ukim.finki.wp.lab.repository.AuthorRepository;
import mk.ukim.finki.wp.lab.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repo;
    public AuthorServiceImpl(AuthorRepository repo) {this.repo = repo;}

    @Override
    public List<Author> findAll() {
        return repo.findAll();
    }
}
