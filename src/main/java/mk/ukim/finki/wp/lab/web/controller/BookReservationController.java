package mk.ukim.finki.wp.lab.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.wp.lab.service.BookReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/reservations")
public class BookReservationController {

    private final BookReservationService reservationService;

    public BookReservationController(BookReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public String makeReservation(@RequestParam String bookTitle,
                                  @RequestParam String readerName,
                                  @RequestParam String readerAddress,
                                  @RequestParam int numCopies,
                                  HttpServletRequest req,
                                  Model model) {

        var ip = req.getRemoteAddr();
        var reservation = reservationService.placeReservation(bookTitle, readerName, readerAddress, numCopies);

        model.addAttribute("reservation", reservation);
        model.addAttribute("ipAddress", ip);

        return "reservationConfirmation";
    }
}
