package abudu.lms.library.repository;

import abudu.lms.library.models.Reservation;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationRepositoryImplTest {
    private ReservationRepositoryImpl reservationRepository;
    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationRepositoryImpl();
        reservation1 = new Reservation(1, "Title1", "Author1", 1234567890L, 1, "2021-07-01", "Notes1");
        reservation2 = new Reservation(2, "Title2", "Author2", 1234567891L, 2, "2021-07-01", "Notes2");
    }

    @Test
    void testAddReservation() {
        reservationRepository.addReservation(reservation1);
        List<Reservation> reservations = reservationRepository.getAllReservations();
        assertEquals(1, reservations.size());
        assertTrue(reservations.contains(reservation1));
    }

    @Test
    void testGetAllReservations() {
        reservationRepository.addReservation(reservation1);
        reservationRepository.addReservation(reservation2);
        List<Reservation> reservations = reservationRepository.getAllReservations();
        assertEquals(2, reservations.size());
        assertTrue(reservations.contains(reservation1));
        assertTrue(reservations.contains(reservation2));
    }

    @Test
    void testUpdateReservation() {
        reservationRepository.addReservation(reservation1);
        reservation1.setTitle("Updated Title1");
        reservationRepository.updateReservation(reservation1);
        Reservation updatedReservation = reservationRepository.getAllReservations().get(0);
        assertEquals("Updated Title1", updatedReservation.getTitle());
    }

    @Test
    void testDeleteReservation() {
        reservationRepository.addReservation(reservation1);
        reservationRepository.deleteReservation(reservation1);
        List<Reservation> reservations = reservationRepository.getAllReservations();
        assertTrue(reservations.isEmpty());
    }

    @Test
    void testGetObservableReservations() {
        reservationRepository.addReservation(reservation1);
        reservationRepository.addReservation(reservation2);
        ObservableList<Reservation> observableReservations = reservationRepository.getObservableReservations();
        assertEquals(2, observableReservations.size());
    }

    @Test
    void testGetReservationQueue() {
        reservationRepository.addReservation(reservation1);
        reservationRepository.addReservation(reservation2);
        Queue<Reservation> reservationQueue = reservationRepository.getReservationQueue();
        assertEquals(2, reservationQueue.size());
        assertEquals(reservation1, reservationQueue.poll());
        assertEquals(reservation2, reservationQueue.poll());
    }

    @Test
    void testUpdateNonExistentReservation() {
        reservationRepository.updateReservation(reservation1);
        List<Reservation> reservations = reservationRepository.getAllReservations();
        assertTrue(reservations.isEmpty());
    }

    @Test
    void testDeleteNonExistentReservation() {
        reservationRepository.deleteReservation(reservation1);
        List<Reservation> reservations = reservationRepository.getAllReservations();
        assertTrue(reservations.isEmpty());
    }

    @Test
    void testAddDuplicateReservation() {
        reservationRepository.addReservation(reservation1);
        reservationRepository.addReservation(reservation1);
        List<Reservation> reservations = reservationRepository.getAllReservations();
        assertEquals(2, reservations.size());
    }
}