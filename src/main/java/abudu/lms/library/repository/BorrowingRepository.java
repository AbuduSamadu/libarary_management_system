package abudu.lms.library.repository;

import abudu.lms.library.models.Borrowing;
import java.util.List;

public interface BorrowingRepository {
    void addBorrowing(Borrowing borrowing);
    List<Borrowing> getAllBorrowings();
    void updateBorrowing(Borrowing borrowing);
    void deleteBorrowing(Borrowing borrowing);
}