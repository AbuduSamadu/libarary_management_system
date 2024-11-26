package abudu.lms.library.repository;

import abudu.lms.library.models.Borrowing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BorrowingRepositoryImpl implements BorrowingRepository {

    private final Map<Integer, Borrowing> borrowings;

    public BorrowingRepositoryImpl() {
        this.borrowings = new HashMap<>();
    }

    @Override
    public void addBorrowing(Borrowing borrowing) {
        borrowings.put(borrowing.getId(), borrowing);
    }

    @Override
    public List<Borrowing> getAllBorrowings() {
        return new ArrayList<>(borrowings.values());
    }

    @Override
    public void updateBorrowing(Borrowing borrowing) {
        borrowings.put(borrowing.getId(), borrowing);
    }

    @Override
    public void deleteBorrowing(Borrowing borrowing) {
        borrowings.remove(borrowing.getId());
    }

    public ObservableList<Borrowing> getObservableBorrowings() {
        return FXCollections.observableArrayList(borrowings.values());
    }
}