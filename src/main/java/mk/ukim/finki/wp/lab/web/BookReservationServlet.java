package mk.ukim.finki.wp.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mk.ukim.finki.wp.lab.model.BookReservation;
import mk.ukim.finki.wp.lab.service.BookReservationService;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(name = "BookReservationServlet", urlPatterns = "/bookReservation")
@Component
public class BookReservationServlet extends HttpServlet {

    private final SpringTemplateEngine templateEngine;
    private final BookReservationService reservationService;

    public BookReservationServlet(SpringTemplateEngine templateEngine,
                                  BookReservationService reservationService) {
        this.templateEngine = templateEngine;
        this.reservationService = reservationService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String bookTitle = req.getParameter("bookTitle");
        String readerName = req.getParameter("readerName");
        String readerAddress = req.getParameter("readerAddress");
        int numCopies = Integer.parseInt(req.getParameter("numCopies"));
        String clientIp = req.getRemoteAddr();

        BookReservation reservation =
                reservationService.placeReservation(bookTitle, readerName, readerAddress, numCopies);

        // Build Thymeleaf context
        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);

        WebContext context = new WebContext(webExchange);
        context.setVariable("reservation", reservation);
        context.setVariable("clientIp", clientIp);

        // Render confirmation page
        templateEngine.process("reservationConfirmation.html", context, resp.getWriter());
    }
}
