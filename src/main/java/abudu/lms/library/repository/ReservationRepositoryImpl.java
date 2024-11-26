package abudu.lms.library.repository;

import abudu.lms.library.models.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ReservationRepositoryImpl implements ReservationRepository {

    private final List<Reservation> reservations;
    private final Queue<Reservation> reservationQueue;

    public ReservationRepositoryImpl() {
        this.reservations = new ArrayList<>();
        this.reservationQueue = new LinkedList<>();
    }

    @Override
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservationQueue.add(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    @Override
    public void updateReservation(Reservation reservation) {
        int index = reservations.indexOf(reservation);
        if (index >= 0) {
            reservations.set(index, reservation);
        }
    }

    @Override
    public void deleteReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservationQueue.remove(reservation);
    }

    public ObservableList<Reservation> getObservableReservations() {
        return FXCollections.observableArrayList(reservations);
    }

    public Queue<Reservation> getReservationQueue() {
        return new LinkedList<>(reservationQueue);
    }
}