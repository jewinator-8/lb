package mk.ukim.finki.wp.lab.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mk.ukim.finki.wp.lab.service.BookService;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(name = "BookListServlet", urlPatterns = "")
@Component
public class BookListServlet extends HttpServlet {

    private final SpringTemplateEngine templateEngine;
    private final BookService bookService;

    public BookListServlet(SpringTemplateEngine templateEngine, BookService bookService) {
        this.templateEngine = templateEngine;
        this.bookService = bookService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String text = req.getParameter("text");
        String ratingParam = req.getParameter("rating");
        Double rating = null;
        if (ratingParam != null && !ratingParam.isBlank()) {
            try {
                rating = Double.parseDouble(ratingParam);
            } catch (NumberFormatException ignored) {
            }
        }

        var books = (text != null || rating != null)
                ? bookService.searchBooks(text, rating)
                : bookService.listAll();

        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);

        WebContext context = new WebContext(webExchange);
        context.setVariable("books", books);
        context.setVariable("text", text == null ? "" : text);
        context.setVariable("rating", rating == null ? "" : rating);

        templateEngine.process("listBooks.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String text = req.getParameter("text");
        String ratingParam = req.getParameter("rating");
        Double rating = null;
        if (ratingParam != null && !ratingParam.isBlank()) {
            try {
                rating = Double.parseDouble(ratingParam);
            } catch (NumberFormatException ignored) {}
        }

        var books = (text != null || rating != null)
                ? bookService.searchBooks(text, rating)
                : bookService.listAll();

        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);
        context.setVariable("books", books);
        context.setVariable("text", text == null ? "" : text);
        context.setVariable("rating", rating == null ? "" : rating);

        templateEngine.process("listBooks.html", context, resp.getWriter());
    }

}



/*Implementation outline — “store visited books in session and show last 3 visited”.

---

### **1. When user reserves a book → store it in session**

In your `BookReservationServlet` after successful reservation:

```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String bookTitle = req.getParameter("bookTitle");
    String readerName = req.getParameter("readerName");
    String readerAddress = req.getParameter("readerAddress");
    int numCopies = Integer.parseInt(req.getParameter("numCopies"));
    String clientIp = req.getRemoteAddr();

    // Create reservation
    BookReservation reservation = bookReservationService.placeReservation(
            bookTitle, readerName, readerAddress, numCopies
    );

    // === Store visited book in session ===
    HttpSession session = req.getSession();
    List<String> visited = (List<String>) session.getAttribute("visitedBooks");
    if (visited == null) {
        visited = new ArrayList<>();
    }

    // maintain latest 3 only
    visited.remove(bookTitle);            // remove if already exists
    visited.add(0, bookTitle);            // insert at top
    if (visited.size() > 3)
        visited = visited.subList(0, 3);  // keep 3

    session.setAttribute("visitedBooks", visited);
    // ====================================

    // Display confirmation page
    IWebExchange webExchange = JakartaServletWebApplication
            .buildApplication(getServletContext())
            .buildExchange(req, resp);
    WebContext context = new WebContext(webExchange);

    context.setVariable("reservation", reservation);
    context.setVariable("ipAddress", clientIp);

    templateEngine.process("reservationConfirmation.html", context, resp.getWriter());
}
```

---

### **2. In BookListServlet → read session and show last 3 visited**

At top of `doGet()` / `doPost()`:

```java
HttpSession session = req.getSession(false);
List<String> visitedBooks = new ArrayList<>();
if (session != null && session.getAttribute("visitedBooks") != null) {
    visitedBooks = (List<String>) session.getAttribute("visitedBooks");
}

context.setVariable("visitedBooks", visitedBooks);
```

---

### **3. In `listBooks.html` → display them**

Above the form:

```html
<div th:if="${visitedBooks != null and !visitedBooks.isEmpty()}">
  <h3>Last 3 visited books:</h3>
  <ul>
    <li th:each="title : ${visitedBooks}" th:text="${title}"></li>
  </ul>
</div>
```

---

### **4. Summary**

* **Session** holds persistent list of last reserved book titles.
* **BookReservationServlet** updates it each time a reservation is made.
* **BookListServlet** reads it and passes to template.
* **listBooks.html** renders it conditionally.

End result:
Each time you reserve, that book’s title is remembered; next time you load the homepage, you see up to 3 most recently reserved titles.

*/