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
