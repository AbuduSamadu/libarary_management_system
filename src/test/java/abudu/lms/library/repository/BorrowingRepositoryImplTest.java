package abudu.lms.library.repository;

import abudu.lms.library.models.Borrowing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BorrowingRepositoryImplTest {
    private BorrowingRepositoryImpl borrowingRepository;
    private Borrowing borrowing1;
    private Borrowing borrowing2;

    @BeforeEach
    void setUp() {
        borrowingRepository = new BorrowingRepositoryImpl();
        borrowing1 = new Borrowing(1, "Book1", "Author1", 1234567890L, 1, "2021-07-01", "");
        borrowing2 = new Borrowing(2, "Book2", "Author2", 1234567891L, 2, "2021-07-01", "");
    }

    @Test
    void testAddBorrowing() {
        borrowingRepository.addBorrowing(borrowing1);
        List<Borrowing> borrowings = borrowingRepository.getAllBorrowings();
        assertEquals(1, borrowings.size());
        assertTrue(borrowings.contains(borrowing1));
    }

    @Test
    void testGetAllBorrowings() {
        borrowingRepository.addBorrowing(borrowing1);
        borrowingRepository.addBorrowing(borrowing2);
        List<Borrowing> borrowings = borrowingRepository.getAllBorrowings();
        assertEquals(2, borrowings.size());
        assertTrue(borrowings.contains(borrowing1));
        assertTrue(borrowings.contains(borrowing2));
    }

    @Test
    void testUpdateBorrowing() {
        borrowingRepository.addBorrowing(borrowing1);
        borrowing1.setTitle("Updated Book1");
        borrowingRepository.updateBorrowing(borrowing1);
        Borrowing updatedBorrowing = borrowingRepository.getAllBorrowings().get(0);
        assertEquals("Updated Book1", updatedBorrowing.getTitle());
    }

    @Test
    void testDeleteBorrowing() {
        borrowingRepository.addBorrowing(borrowing1);
        borrowingRepository.deleteBorrowing(borrowing1);
        List<Borrowing> borrowings = borrowingRepository.getAllBorrowings();
        assertTrue(borrowings.isEmpty());
    }

    @Test
    void testGetObservableBorrowings() {
        borrowingRepository.addBorrowing(borrowing1);
        borrowingRepository.addBorrowing(borrowing2);
        assertEquals(2, borrowingRepository.getObservableBorrowings().size());
    }
}