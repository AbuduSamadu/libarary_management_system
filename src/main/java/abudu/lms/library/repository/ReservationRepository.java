package abudu.lms.library.repository;

import abudu.lms.library.models.Reservation;
import java.util.List;

public interface ReservationRepository {
    void addReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    void updateReservation(Reservation reservation);
    void deleteReservation(Reservation reservation);
}