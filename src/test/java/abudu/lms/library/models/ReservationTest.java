package abudu.lms.library.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ReservationTest {
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation(1, "title", "author", 1234567890, 1, "2021-08-01", "notes");
    }

    @Test
    void getId() {
        assertEquals(1, reservation.getId());
    }

    @Test
    void setId() {
        reservation.setId(2);
        assertEquals(2, reservation.getId());
    }

    @Test
    void getTitle() {
        assertEquals("title", reservation.getTitle());
    }

    @Test
    void setTitle() {
        reservation.setTitle("new title");
        assertEquals("new title", reservation.getTitle());
    }

    @Test
    void getAuthor() {
        assertEquals("author", reservation.getAuthor());
    }

    @Test
    void setAuthor() {
        reservation.setAuthor("new author");
        assertEquals("new author", reservation.getAuthor());
    }

    @Test
    void getIsbn() {
        assertEquals(1234567890, reservation.getIsbn());
    }

    @Test
    void setIsbn()
    {
        reservation.setIsbn(1234567891);
        assertEquals(1234567891, reservation.getIsbn());
    }

    @Test
    void getUserId() {
        assertEquals(1, reservation.getUserId());
    }

    @Test
    void setUserId() {
        reservation.setUserId(2);
        assertEquals(2, reservation.getUserId());
    }

    @Test
    void getReservationDate() {
        assertEquals("2021-08-01", reservation.getReservationDate());
    }

    @Test
    void setReservationDate() {
        reservation.setReservationDate("2021-08-02");
        assertEquals("2021-08-02", reservation.getReservationDate());
    }

    @Test
    void getNotes() {
        assertEquals("notes", reservation.getNotes());
    }

    @Test
    void setNotes() {
        reservation.setNotes("new notes");
        assertEquals("new notes", reservation.getNotes());
    }

    @Test
    void isActive() {
        assertTrue(reservation.isActive());
    }

    @Test
    void setActive() {
        reservation.setActive(false);
        assertFalse(reservation.isActive());
    }
}