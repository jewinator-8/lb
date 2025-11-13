package mk.ukim.finki.wp.lab.bootstrap;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.lab.model.Author;
import mk.ukim.finki.wp.lab.model.Book;
import mk.ukim.finki.wp.lab.model.BookReservation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {
    public static List<Book> books = new ArrayList<>();
    public static List<Author> authors = new ArrayList<>();
    public static List<BookReservation> reservations = new ArrayList<>();
    @PostConstruct
    public void init() {
        if (!books.isEmpty()) return;
        // --- create authors ---
        Author a1 = new Author("Mirce", "Mirceski", "USA", "Pisatel");
        Author a2 = new Author("Ana", "Petrovska", "Macedonia", "Pisatel");
        Author a3 = new Author("Marko", "Stojanovski", "Serbia", "Pisatel");

        authors.add(a1);
        authors.add(a2);
        authors.add(a3);

        books.add(new Book("Book 1", "Programming", 4.7, a1));
        books.add(new Book("Book 2", "Fantasy", 4.1, a2));
        books.add(new Book("Book 3", "Sci-Fi", 4.8, a3));
        books.add(new Book("Book 4", "Romance", 3.9, a1));
        books.add(new Book("Book 5", "Horror", 4.2, a2));
        books.add(new Book("Book 6", "Drama", 3.7, a3));
        books.add(new Book("Book 7", "Travel", 4.0, a1));
        books.add(new Book("Book 8", "Adventure", 4.3, a2));
        books.add(new Book("Book 9", "History", 3.5, a3));
        books.add(new Book("Book 10", "Thriller", 4.6, a1));
    }
}



/*
Minimal structure. No softening.

You need:

field likes in Author

service + repository methods

controller with /authors view

button → POST → increments like count

view page lists authors + like button

Everything remains in-memory.

1. Extend Author model
public class Author {

    private Long id;
    private String name;
    private String surname;
    private String country;
    private String biography;

    private int likes; // new

    public Author() {}

    public Author(String name, String surname, String country, String biography) {
        this.id = (long) (Math.random() * 10000);
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.biography = biography;
        this.likes = 0;
    }

    // getters/setters…
}

2. Update AuthorRepository

Add method:

void incrementLikes(Long id);

Implementation:
@Repository
public class InMemoryAuthorRepository implements AuthorRepository {

    @Override
    public List<Author> findAll() {
        return DataHolder.authors;
    }

    @Override
    public Optional<Author> findById(Long id) {
        return DataHolder.authors.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    @Override
    public void incrementLikes(Long id) {
        findById(id).ifPresent(a -> a.setLikes(a.getLikes() + 1));
    }
}


Update interface:

public interface AuthorRepository {
    List<Author> findAll();
    Optional<Author> findById(Long id);
    void incrementLikes(Long id);
}

3. AuthorService

Interface:

public interface AuthorService {
    List<Author> findAll();
    void likeAuthor(Long id);
}


Implementation:

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repo;

    public AuthorServiceImpl(AuthorRepository repo) {
        this.repo = repo;
    }

    public List<Author> findAll() {
        return repo.findAll();
    }

    public void likeAuthor(Long id) {
        repo.incrementLikes(id);
    }
}

4. AuthorController

New controller under mk.ukim.finki.wp.lab.web.controller.

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public String getAuthors(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "authors";
    }

    @PostMapping("/like/{id}")
    public String likeAuthor(@PathVariable Long id) {
        authorService.likeAuthor(id);
        return "redirect:/authors";
    }
}

5. authors.html

src/main/resources/templates/authors.html

<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>Authors</title>
</head>
<body>

<h1>Authors</h1>

<table border="1" width="100%">
    <tr>
        <th>Name</th>
        <th>Country</th>
        <th>Likes</th>
        <th>Actions</th>
    </tr>

    <tr th:each="a : ${authors}">
        <td th:text="${a.name + ' ' + a.surname}"></td>
        <td th:text="${a.country}"></td>
        <td th:text="${a.likes}"></td>

        <td>
            <form th:action="@{'/authors/like/' + ${a.id}}"
                  method="post"
                  style="display:inline;">
                <button type="submit">Like</button>
            </form>
        </td>
    </tr>
</table>

</body>
</html>

6. Add link to authors page (optional)

In your navbar or listBooks.html:

<a href="/authors">Authors</a>


This gives:

/authors → list of all authors

Each shows name, country, likes count

"Like" button increments likes in memory

Minimal, correct, fully working.
 */