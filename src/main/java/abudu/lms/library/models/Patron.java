package abudu.lms.library.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Patron extends User {
    private final Set<Book> borrowedBooks; // Ensures no duplicate books
    private double fines;

    public Patron(int id, String firstName, String lastName, String username, String email, String password, LocalDateTime createdAt, Set<Role> roles, Set<Book> borrowedBooks, double fines) {
        super(id, firstName, lastName, username, email, password, createdAt, roles);
        this.borrowedBooks = borrowedBooks != null ? borrowedBooks : new HashSet<>();
        this.fines = fines;
        this.getRoles().add(new Role(1, ERole.ROLE_PATRON)); // Ensures role is set
    }

    // Getters
    public Set<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public double getFines() {
        return fines;
    }

    // Borrow a book
    public void borrowBook(Book book) {
        this.borrowedBooks.add(book);
    }

    // Return a book
    public void returnBook(Book book) {
        this.borrowedBooks.remove(book);
    }

    // Set fines with validation
    public void setFines(double fines) {
        if (fines < 0) {
            throw new IllegalArgumentException("Fines cannot be negative");
        }
        this.fines = fines;
    }
}
