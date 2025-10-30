package mk.ukim.finki.wp.lab.service.impl;


import mk.ukim.finki.wp.lab.model.BookReservation;
import mk.ukim.finki.wp.lab.repository.BookReservationRepository;
import mk.ukim.finki.wp.lab.service.BookReservationService;
import org.springframework.stereotype.Service;

@Service
public class BookReservationServiceImpl implements BookReservationService {
    private final BookReservationRepository repo;
    public BookReservationServiceImpl(BookReservationRepository repo) { this.repo = repo; }

    @Override
    public BookReservation placeReservation(String bookTitle, String readerName, String readerAddress, int numberOfCopies) {
        return repo.save(new BookReservation(
                bookTitle,
                readerName,
                readerAddress,
                (long) numberOfCopies
        ));
    }
}
