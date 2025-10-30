package mk.ukim.finki.wp.lab.repository;

import mk.ukim.finki.wp.lab.model.BookReservation;
import org.springframework.stereotype.Repository;

public interface BookReservationRepository {
    BookReservation save(BookReservation reservation);
}